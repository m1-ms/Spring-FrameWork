
UPDATE customers SET enabled = 1, blocked = 0 WHERE email = 'hacker@test.com';

UPDATE customers SET enabled = 0 WHERE email = 'hacker@test.com';

UPDATE customers SET enabled = 1, blocked = 1 WHERE email = 'hacker@test.com';

UPDATE customers SET blocked = 0 WHERE email = 'hacker@test.com';

DELETE FROM customer_roles WHERE customer_id = 2;

INSERT INTO customer_roles (customer_id, role_id) VALUES (2, 3);


---

DELETE FROM customer_roles WHERE customer_id = (SELECT id FROM customers WHERE email = 'admin@test.com');
INSERT INTO customer_roles (customer_id, role_id)
SELECT c.id, r.id FROM customers c CROSS JOIN roles r WHERE c.email = 'admin@test.com' AND r.name = 'ADMIN';

DELETE FROM customer_roles WHERE customer_id = (SELECT id FROM customers WHERE email = 'manager@test.com');
INSERT INTO customer_roles (customer_id, role_id)
SELECT c.id, r.id FROM customers c CROSS JOIN roles r WHERE c.email = 'manager@test.com' AND r.name = 'BANK_MANAGER';

DELETE FROM customer_roles WHERE customer_id = (SELECT id FROM customers WHERE email = 'auditor@test.com');
INSERT INTO customer_roles (customer_id, role_id)
SELECT c.id, r.id FROM customers c CROSS JOIN roles r WHERE c.email = 'auditor@test.com' AND r.name = 'AUDITOR';

---

SELECT c.email, r.name AS role_name
FROM customers c
JOIN customer_roles cr ON cr.customer_id = c.id
JOIN roles r ON r.id = cr.role_id
ORDER BY c.email;

SELECT id, full_name, email, phone FROM customers;

SELECT id, account_number, balance, currency, status FROM accounts ORDER BY id;
SELECT id, from_account_id, to_account_id, amount, status, created_by FROM transfers;



------------------------------------------------------------

--  clean-up

DROP TABLE transfers CASCADE CONSTRAINTS PURGE;
 DROP TABLE accounts CASCADE CONSTRAINTS PURGE;
 DROP TABLE customer_roles CASCADE CONSTRAINTS PURGE;
 DROP TABLE role_permissions CASCADE CONSTRAINTS PURGE;
 DROP TABLE customers CASCADE CONSTRAINTS PURGE;
 DROP TABLE roles CASCADE CONSTRAINTS PURGE;
 DROP TABLE permissions CASCADE CONSTRAINTS PURGE;

-- PART 1: TABLES

CREATE TABLE permissions (
    id    NUMBER(19,0) GENERATED AS IDENTITY,
    name  VARCHAR2(50 CHAR) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_permissions_name UNIQUE (name)
);

CREATE TABLE roles (
    id    NUMBER(19,0) GENERATED AS IDENTITY,
    name  VARCHAR2(50 CHAR) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_roles_name UNIQUE (name)
);

CREATE TABLE role_permissions (
    role_id        NUMBER(19,0) NOT NULL,
    permission_id  NUMBER(19,0) NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_rp_role       FOREIGN KEY (role_id)       REFERENCES roles (id),
    CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id) REFERENCES permissions (id)
);

CREATE TABLE customers (
    id          NUMBER(19,0) GENERATED AS IDENTITY,
    full_name   VARCHAR2(100 CHAR) NOT NULL,
    email       VARCHAR2(100 CHAR) NOT NULL,
    password    VARCHAR2(100 CHAR) NOT NULL,
    phone       VARCHAR2(20 CHAR),
    enabled     NUMBER(1,0) NOT NULL CHECK (enabled IN (0, 1)),
    blocked     NUMBER(1,0) NOT NULL CHECK (blocked IN (0, 1)),
    created_at  TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_customers_email UNIQUE (email)
);

CREATE TABLE customer_roles (
    customer_id  NUMBER(19,0) NOT NULL,
    role_id      NUMBER(19,0) NOT NULL,
    PRIMARY KEY (customer_id, role_id),
    CONSTRAINT fk_cr_customer FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT fk_cr_role     FOREIGN KEY (role_id)     REFERENCES roles (id)
);

CREATE TABLE accounts (
    id              NUMBER(19,0) GENERATED AS IDENTITY,
    account_number  VARCHAR2(30 CHAR) NOT NULL,
    balance         NUMBER(19,2) NOT NULL,
    currency        VARCHAR2(3 CHAR) NOT NULL,
    status          VARCHAR2(20 CHAR) NOT NULL
                    CHECK (status IN ('ACTIVE', 'BLOCKED', 'CLOSED')),
    customer_id     NUMBER(19,0) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_accounts_number   UNIQUE (account_number),
    CONSTRAINT fk_accounts_customer FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE TABLE transfers (
    id               NUMBER(19,0) GENERATED AS IDENTITY,
    amount           NUMBER(19,2) NOT NULL,
    status           VARCHAR2(20 CHAR) NOT NULL
                     CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'COMPLETED', 'CANCELLED')),
    created_at       TIMESTAMP(6),
    created_by       NUMBER(19,0) NOT NULL,
    from_account_id  NUMBER(19,0) NOT NULL,
    to_account_id    NUMBER(19,0) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_transfers_created_by FOREIGN KEY (created_by)      REFERENCES customers (id),
    CONSTRAINT fk_transfers_from       FOREIGN KEY (from_account_id) REFERENCES accounts (id),
    CONSTRAINT fk_transfers_to         FOREIGN KEY (to_account_id)   REFERENCES accounts (id)
);

-- PART 2: BASE DATA

-- 1. Permissions
MERGE INTO permissions t
USING (
    SELECT 'ACCOUNT_READ'     AS name FROM dual UNION ALL
    SELECT 'ACCOUNT_CREATE'           FROM dual UNION ALL
    SELECT 'ACCOUNT_UPDATE'           FROM dual UNION ALL
    SELECT 'ACCOUNT_DELETE'           FROM dual UNION ALL
    SELECT 'TRANSFER_CREATE'          FROM dual UNION ALL
    SELECT 'TRANSFER_READ'            FROM dual UNION ALL
    SELECT 'TRANSFER_APPROVE'         FROM dual UNION ALL
    SELECT 'CUSTOMER_READ'            FROM dual UNION ALL
    SELECT 'CUSTOMER_UPDATE'          FROM dual UNION ALL
    SELECT 'REPORT_VIEW'              FROM dual
) s ON (t.name = s.name)
WHEN NOT MATCHED THEN INSERT (name) VALUES (s.name);

-- 2. Roles (stored without the ROLE_ prefix; the prefix is added when authorities are built)
MERGE INTO roles t
USING (
    SELECT 'ADMIN'        AS name FROM dual UNION ALL
    SELECT 'BANK_MANAGER'         FROM dual UNION ALL
    SELECT 'CUSTOMER'             FROM dual UNION ALL
    SELECT 'AUDITOR'              FROM dual
) s ON (t.name = s.name)
WHEN NOT MATCHED THEN INSERT (name) VALUES (s.name);

-- 3. Role -> permissions (ADMIN 10, BANK_MANAGER 6, CUSTOMER 3, AUDITOR 3 = 22 rows)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON (
       (r.name = 'ADMIN' AND p.name IN (
            'ACCOUNT_READ', 'ACCOUNT_CREATE', 'ACCOUNT_UPDATE', 'ACCOUNT_DELETE',
            'TRANSFER_CREATE', 'TRANSFER_READ', 'TRANSFER_APPROVE',
            'CUSTOMER_READ', 'CUSTOMER_UPDATE', 'REPORT_VIEW'))
    OR (r.name = 'BANK_MANAGER' AND p.name IN (
            'ACCOUNT_READ', 'ACCOUNT_UPDATE',
            'TRANSFER_READ', 'TRANSFER_APPROVE',
            'CUSTOMER_READ', 'CUSTOMER_UPDATE'))
    OR (r.name = 'CUSTOMER' AND p.name IN (
            'ACCOUNT_READ', 'TRANSFER_CREATE', 'TRANSFER_READ'))
    OR (r.name = 'AUDITOR' AND p.name IN (
            'ACCOUNT_READ', 'TRANSFER_READ', 'REPORT_VIEW'))
)
WHERE NOT EXISTS (
    SELECT 1 FROM role_permissions rp
    WHERE rp.role_id = r.id AND rp.permission_id = p.id
);

-- 4. Initial administrator (demo credentials: admin@test.com / Pass12345)
--    The password is a BCrypt hash. Change it before using this anywhere real.
MERGE INTO customers c
USING (SELECT 'admin@test.com' AS email FROM dual) s
ON (c.email = s.email)
WHEN NOT MATCHED THEN INSERT (full_name, email, password, phone, enabled, blocked, created_at)
VALUES ('System Admin', s.email,
        '$2a$10$xIyzxwJ1MKdFjF/5R0Dfn.8wd3RXlv00T0c8VCfSVrzuQYaIY9tbK',
        '01000000000', 1, 0, SYSTIMESTAMP);

INSERT INTO customer_roles (customer_id, role_id)
SELECT c.id, r.id
FROM customers c
JOIN roles r ON r.name = 'ADMIN'
WHERE c.email = 'admin@test.com'
  AND NOT EXISTS (
      SELECT 1 FROM customer_roles cr
      WHERE cr.customer_id = c.id AND cr.role_id = r.id
  );

COMMIT;

-- Quick checks
-- SELECT COUNT(*) FROM permissions;       -- 10
-- SELECT COUNT(*) FROM roles;             -- 4
-- SELECT COUNT(*) FROM role_permissions;  -- 22
-- SELECT email FROM customers;            -- admin@test.com



SELECT * FROM customers ORDER BY id;

SELECT id, email FROM customers WHERE email IN
('ahmed@test.com','hacker@test.com','third@test.com','admin@test.com','manager@test.com','auditor@test.com')
ORDER BY id;




