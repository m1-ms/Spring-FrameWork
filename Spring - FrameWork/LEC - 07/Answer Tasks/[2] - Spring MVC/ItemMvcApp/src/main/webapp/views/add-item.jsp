<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add item</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Oswald:wght@500;600&family=IBM+Plex+Mono:wght@400;500&family=Work+Sans:wght@400;500&display=swap" rel="stylesheet">
    <style>
        :root { --paper: #E9E6DD; --paper-card: #F3F1EA; --ink: #22252B; --ink-muted: #6B6A63; --line: #B7AF9C; --stamp: #A8342A; --stamp-dark: #7A2620; }
        * { box-sizing: border-box; }
        body { background: var(--paper); color: var(--ink); font-family: 'Work Sans', sans-serif; margin: 0; padding: 48px 24px; }
        .page { max-width: 480px; margin: 0 auto; }
        h1 { font-family: 'Oswald', sans-serif; font-weight: 600; font-size: 26px; margin: 0 0 24px; }
        .error-banner { border: 1px dashed var(--stamp); color: var(--stamp-dark); font-size: 14px; padding: 12px 16px; margin-bottom: 20px; }
        .card { background: var(--paper-card); border: 1px dashed var(--line); padding: 32px; }
        .field { margin-bottom: 22px; }
        .field label { display: block; font-size: 13px; color: var(--ink-muted); margin-bottom: 6px; }
        .field input { width: 100%; border: none; border-bottom: 1px solid var(--line); background: transparent; padding: 8px 2px; font-size: 16px; color: var(--ink); font-family: 'Work Sans', sans-serif; }
        .field input:focus { outline: none; border-bottom: 1px solid var(--stamp); }
        .field.numeric input { font-family: 'IBM Plex Mono', monospace; }
        .stamp-btn { display: inline-block; font-family: 'Oswald', sans-serif; font-size: 15px; color: #fff; background: var(--stamp); border: 2px solid var(--stamp); outline: 1px solid var(--stamp); outline-offset: 3px; padding: 10px 20px; cursor: pointer; transform: rotate(-1.5deg); margin-top: 8px; }
        .stamp-btn:hover { background: var(--stamp-dark); border-color: var(--stamp-dark); }
        .back-link { display: inline-block; margin-top: 28px; font-size: 14px; color: var(--ink-muted); text-decoration: none; }
        .back-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="page">
    <h1>Add item</h1>

    <c:if test="${not empty errorMessage}">
        <div class="error-banner">${errorMessage}</div>
    </c:if>

    <c:if test="${not empty validationErrors}">
        <div class="error-banner">
            <c:forEach var="err" items="${validationErrors}">
                <div>${err}</div>
            </c:forEach>
        </div>
    </c:if>

    <div class="card">
        <form action="${pageContext.request.contextPath}/items/save" method="post">
            <div class="field">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" value="${item.name}" required>
            </div>
            <div class="field numeric">
                <label for="price">Price</label>
                <input type="number" step="0.01" id="price" name="price" value="${item.price}" required>
            </div>
            <div class="field numeric">
                <label for="quantity">Quantity</label>
                <input type="number" id="quantity" name="quantity" value="${item.quantity}" required>
            </div>
            <button type="submit" class="stamp-btn">Save item</button>
        </form>
    </div>
    <a class="back-link" href="${pageContext.request.contextPath}/items">Back to list</a>
</div>
</body>
</html>