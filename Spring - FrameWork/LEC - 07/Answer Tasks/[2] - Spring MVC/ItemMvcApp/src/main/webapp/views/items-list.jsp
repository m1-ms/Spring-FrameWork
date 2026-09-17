<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Item manifest</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Oswald:wght@500;600&family=IBM+Plex+Mono:wght@400;500&family=Work+Sans:wght@400;500&display=swap" rel="stylesheet">
    <style>
        :root {
            --paper: #E9E6DD;
            --paper-card: #F3F1EA;
            --ink: #22252B;
            --ink-muted: #6B6A63;
            --line: #B7AF9C;
            --stamp: #A8342A;
            --stamp-dark: #7A2620;
            --tag-bg: #F2DFB8;
            --tag-text: #6B4A12;
        }
        * { box-sizing: border-box; }
        body { background: var(--paper); color: var(--ink); font-family: 'Work Sans', sans-serif; margin: 0; padding: 48px 24px; }
        .page { max-width: 640px; margin: 0 auto; }
        .manifest-header { display: flex; justify-content: space-between; align-items: baseline; border-bottom: 2px solid var(--ink); padding-bottom: 12px; margin-bottom: 24px; }
        .manifest-header h1 { font-family: 'Oswald', sans-serif; font-weight: 600; font-size: 28px; margin: 0; }
        .count-tag { font-family: 'IBM Plex Mono', monospace; font-size: 13px; background: var(--tag-bg); color: var(--tag-text); padding: 4px 10px; border-radius: 999px; }
        .stamp-btn { display: inline-block; font-family: 'Oswald', sans-serif; font-size: 15px; color: #fff; background: var(--stamp); text-decoration: none; padding: 10px 20px; border: 2px solid var(--stamp); outline: 1px solid var(--stamp); outline-offset: 3px; transform: rotate(-1.5deg); margin: 8px 6px 32px 0; }
        .stamp-btn:hover { background: var(--stamp-dark); border-color: var(--stamp-dark); }
        table { width: 100%; border-collapse: collapse; }
        thead th { text-align: left; font-family: 'Oswald', sans-serif; font-weight: 500; font-size: 14px; color: var(--ink-muted); border-bottom: 2px solid var(--ink); padding: 8px 6px; }
        tbody td { padding: 14px 6px; border-bottom: 1px dashed var(--line); font-size: 15px; vertical-align: middle; }
        .id-cell, .price-cell { font-family: 'IBM Plex Mono', monospace; color: var(--ink-muted); }
        .qty-tag { font-family: 'IBM Plex Mono', monospace; background: var(--tag-bg); color: var(--tag-text); padding: 2px 10px; border-radius: 999px; font-size: 13px; }
        .actions a, .actions button { color: var(--ink); text-decoration: none; margin-right: 10px; font-size: 14px; background: none; border: none; cursor: pointer; font-family: 'Work Sans', sans-serif; padding: 0; }
        .actions a:hover, .actions button:hover { text-decoration: underline; }
        .actions .delete-link { color: var(--stamp); }
        .empty-state { border: 1px dashed var(--line); padding: 40px 24px; }
        .empty-state p { margin: 0 0 16px; color: var(--ink-muted); }

        .modal-overlay { display: none; position: fixed; inset: 0; background: rgba(34,37,43,0.55); align-items: center; justify-content: center; z-index: 100; }
        .modal-overlay.open { display: flex; }
        .modal-card { background: var(--paper-card); border: 1px dashed var(--line); padding: 28px; max-width: 300px; width: 90%; }
        .modal-card h2 { font-family: 'Oswald', sans-serif; font-weight: 600; font-size: 19px; margin: 0 0 10px; }
        .modal-card p { font-size: 14px; color: var(--ink-muted); margin: 0 0 22px; }
        .modal-actions { display: flex; justify-content: flex-end; gap: 10px; }
        .modal-btn-cancel { font-family: 'Work Sans', sans-serif; font-size: 14px; background: transparent; border: 1px solid var(--line); color: var(--ink); padding: 8px 16px; cursor: pointer; }
        .modal-btn-cancel:hover { background: var(--paper); }
        .modal-btn-delete { font-family: 'Oswald', sans-serif; font-size: 14px; background: var(--stamp); border: 2px solid var(--stamp); color: #fff; padding: 8px 16px; cursor: pointer; }
        .modal-btn-delete:hover { background: var(--stamp-dark); border-color: var(--stamp-dark); }
    </style>
</head>
<body>
<div class="page">
    <div class="manifest-header">
        <h1>Item Manifest</h1>
        <span class="count-tag">${items.size()} in stock</span>
    </div>

    <a class="stamp-btn" href="${pageContext.request.contextPath}/items/add">+ Add item</a>

    <c:choose>
        <c:when test="${empty items}">
            <div class="empty-state">
                <p>No items recorded yet.</p>
                <a class="stamp-btn" href="${pageContext.request.contextPath}/items/add">Add your first item</a>
            </div>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${items}">
                    <tr>
                        <td class="id-cell">#${item.id}</td>
                        <td>${item.name}</td>
                        <td class="price-cell">$${item.price}</td>
                        <td><span class="qty-tag">x${item.quantity}</span></td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/items/${item.id}">view</a>
                            <a href="${pageContext.request.contextPath}/items/update/${item.id}">edit</a>
                            <button type="button" class="delete-link" onclick="openDeleteModal('${pageContext.request.contextPath}/items/delete/${item.id}')">delete</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<div class="modal-overlay" id="deleteModal">
    <div class="modal-card">
        <h2>Delete item?</h2>
        <p>This can't be undone.</p>
        <div class="modal-actions">
            <button type="button" class="modal-btn-cancel" onclick="closeDeleteModal()">Cancel</button>
            <button type="button" class="modal-btn-delete" onclick="confirmDelete()">Delete item</button>
        </div>
    </div>
</div>

<script>
    var pendingDeleteUrl = null;
    function openDeleteModal(url) {
        pendingDeleteUrl = url;
        document.getElementById('deleteModal').classList.add('open');
    }
    function closeDeleteModal() {
        pendingDeleteUrl = null;
        document.getElementById('deleteModal').classList.remove('open');
    }
    function confirmDelete() {
        if (pendingDeleteUrl) {
            window.location.href = pendingDeleteUrl;
        }
    }
</script>
</body>
</html>