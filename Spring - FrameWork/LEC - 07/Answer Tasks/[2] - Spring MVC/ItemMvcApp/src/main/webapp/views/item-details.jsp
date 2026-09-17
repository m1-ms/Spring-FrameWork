<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Item details</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Oswald:wght@500;600&family=IBM+Plex+Mono:wght@400;500&family=Work+Sans:wght@400;500&display=swap" rel="stylesheet">
    <style>
        :root { --paper: #E9E6DD; --paper-card: #F3F1EA; --ink: #22252B; --ink-muted: #6B6A63; --line: #B7AF9C; --stamp: #A8342A; --stamp-dark: #7A2620; }
        * { box-sizing: border-box; }
        body { background: var(--paper); color: var(--ink); font-family: 'Work Sans', sans-serif; margin: 0; padding: 64px 24px; }
        .page { max-width: 480px; margin: 0 auto; }
        h1 { font-family: 'Oswald', sans-serif; font-weight: 600; font-size: 24px; margin: 0 0 28px; }
        .tag-card { background: var(--paper-card); border: 1px dashed var(--line); padding: 32px; max-width: 320px; transform: rotate(-1deg); }
        dl { margin: 0; }
        dt { font-size: 12px; color: var(--ink-muted); margin-top: 18px; }
        dt:first-child { margin-top: 0; }
        dd { font-family: 'IBM Plex Mono', monospace; font-size: 20px; margin: 2px 0 0; }
        .tag-actions { display: flex; align-items: center; gap: 20px; margin-top: 24px; }
        .stamp-btn { display: inline-block; font-family: 'Oswald', sans-serif; font-size: 15px; color: #fff; background: var(--stamp); border: 2px solid var(--stamp); outline: 1px solid var(--stamp); outline-offset: 3px; padding: 10px 20px; text-decoration: none; transform: rotate(-1.5deg); }
        .stamp-btn:hover { background: var(--stamp-dark); border-color: var(--stamp-dark); }
        .delete-text { font-size: 14px; color: var(--stamp); text-decoration: none; background: none; border: none; cursor: pointer; font-family: 'Work Sans', sans-serif; padding: 0; }
        .delete-text:hover { text-decoration: underline; }
        .back-link { display: inline-block; margin-top: 28px; font-size: 14px; color: var(--ink-muted); text-decoration: none; }
        .back-link:hover { text-decoration: underline; }

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
    <h1>Item details</h1>
    <div class="tag-card">
        <dl>
            <dt>ID</dt>
            <dd>#${item.id}</dd>
            <dt>Name</dt>
            <dd>${item.name}</dd>
            <dt>Price</dt>
            <dd>$${item.price}</dd>
            <dt>Quantity</dt>
            <dd>x${item.quantity}</dd>
        </dl>
    </div>

    <div class="tag-actions">
        <a class="stamp-btn" href="${pageContext.request.contextPath}/items/update/${item.id}">Edit item</a>
        <button type="button" class="delete-text" onclick="openDeleteModal('${pageContext.request.contextPath}/items/delete/${item.id}')">Delete item</button>
    </div>

    <a class="back-link" href="${pageContext.request.contextPath}/items">Back to list</a>
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