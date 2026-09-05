
-- Task [ 1 ]
SELECT * FROM USERS;
SELECT * FROM USERS_DETAILS;
SELECT * FROM FRIENDS;
SELECT * FROM POSTS;

SELECT u.NAME AS user_name, ud.ADDRESS, ud.PHONE
FROM USERS u
JOIN USERS_DETAILS ud ON u.USER_DETAILS_ID = ud.id;

SELECT u.NAME AS user_name, p.HEADER, p.CONTENT
FROM POSTS p
JOIN USERS u ON p.USER_ID = u.id;

SELECT u.NAME AS user_name, f.NAME AS friend_name
FROM USER_FRIENDS uf
JOIN USERS u ON uf.USER_ID = u.id
JOIN FRIENDS f ON uf.FRIEND_ID = f.id;


 --------------------------------------------------------

-- Task [ 2 ]
SELECT * FROM USERS;

SELECT u.NAME, u.AGE, ud.ADDRESS, ud.PHONE
FROM USERS u
JOIN USERS_DETAILS ud ON u.USER_DETAILS_ID = ud.id
WHERE u.NAME = 'Mostafa';

SELECT u.NAME AS user_name, f.NAME AS friend_name
FROM USER_FRIENDS uf
JOIN USERS u ON uf.USER_ID = u.id
JOIN FRIENDS f ON uf.FRIEND_ID = f.id
WHERE u.NAME = 'Hana';

SELECT u.NAME AS user_name, p.HEADER, p.CONTENT
FROM POSTS p
JOIN USERS u ON p.USER_ID = u.id
WHERE u.NAME = 'Karim';


 --------------------------------------------------------

-- Task [ 3 ]
SELECT * FROM USERS;

SELECT u.NAME, u.AGE, ud.ADDRESS, ud.PHONE
FROM USERS u
JOIN USERS_DETAILS ud ON u.USER_DETAILS_ID = ud.id
WHERE u.NAME = 'Tarek';

SELECT u.NAME AS user_name, f.NAME AS friend_name
FROM USER_FRIENDS uf
JOIN USERS u ON uf.USER_ID = u.id
JOIN FRIENDS f ON uf.FRIEND_ID = f.id
WHERE u.NAME = 'Laila';

SELECT u.NAME AS user_name, p.HEADER, p.CONTENT
FROM POSTS p
JOIN USERS u ON p.USER_ID = u.id
WHERE u.NAME = 'Sameh';


 --------------------------------------------------------

-- Task [ 4 ]
SELECT u.ID, u.NAME, ud.ADDRESS, ud.PHONE
FROM USERS u
JOIN USERS_DETAILS ud ON u.USER_DETAILS_ID = ud.id
WHERE u.ID = 1;

 --------------------------------------------------------

-- Task [ 5 ]
SELECT ID, NAME, USER_TYPE, ACCESS_LEVEL, SUBSCRIPTION_TYPE 
FROM USERS 
WHERE USER_TYPE IN ('ADMIN', 'REGULAR');

 --------------------------------------------------------


ALTER TABLE USERS ADD USER_TYPE VARCHAR2(31 CHAR) DEFAULT 'USER' NOT NULL;
