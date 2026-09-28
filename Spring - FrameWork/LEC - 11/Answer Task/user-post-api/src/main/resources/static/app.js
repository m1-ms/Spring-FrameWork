const BASE = '';

const userForm = document.getElementById('userForm');
const userFormTitle = document.getElementById('userFormTitle');
const userIdField = document.getElementById('userId');
const uname = document.getElementById('uname');
const uage = document.getElementById('uage');
const upass = document.getElementById('upass');
const userError = document.getElementById('userError');
const userSubmitBtn = document.getElementById('userSubmitBtn');
const userCancelBtn = document.getElementById('userCancelBtn');

const postForm = document.getElementById('postForm');
const postFormTitle = document.getElementById('postFormTitle');
const postIdField = document.getElementById('postId');
const ptext = document.getElementById('ptext');
const ppath = document.getElementById('ppath');
const puser = document.getElementById('puser');
const postError = document.getElementById('postError');
const postSubmitBtn = document.getElementById('postSubmitBtn');
const postCancelBtn = document.getElementById('postCancelBtn');

const usersList = document.getElementById('usersList');
const refreshBtn = document.getElementById('refreshBtn');

const confirmModal = document.getElementById('confirmModal');
const confirmMessage = document.getElementById('confirmMessage');
const confirmOkBtn = document.getElementById('confirmOkBtn');
const confirmCancelBtn = document.getElementById('confirmCancelBtn');

function showConfirm(message) {
  return new Promise((resolve) => {
    confirmMessage.textContent = message;
    confirmModal.classList.remove('hidden');

    function cleanup(result) {
      confirmModal.classList.add('hidden');
      confirmOkBtn.removeEventListener('click', onOk);
      confirmCancelBtn.removeEventListener('click', onCancel);
      resolve(result);
    }
    function onOk() { cleanup(true); }
    function onCancel() { cleanup(false); }

    confirmOkBtn.addEventListener('click', onOk);
    confirmCancelBtn.addEventListener('click', onCancel);
  });
}

async function loadUsers() {
  usersList.innerHTML = '<div class="empty">Loading...</div>';
  try {
    const res = await fetch(`${BASE}/users/usersWithPost`);
    if (!res.ok) throw new Error('failed to load users');
    const users = await res.json();
    renderUsers(users);
    fillUserSelect(users);
  } catch (err) {
    usersList.innerHTML = `<div class="error">Could not load users. Is the backend running on ${BASE || 'this origin'}?</div>`;
  }
}

function fillUserSelect(users) {
  puser.innerHTML = '';
  users.forEach(u => {
    const opt = document.createElement('option');
    opt.value = u.id;
    opt.textContent = `${u.name} (id ${u.id})`;
    puser.appendChild(opt);
  });
}

function renderUsers(users) {
  if (!users.length) {
    usersList.innerHTML = '<div class="empty">No users yet.</div>';
    return;
  }

  usersList.innerHTML = '';
  users.forEach(user => {
    const card = document.createElement('div');
    card.className = 'user-card';

    const head = document.createElement('div');
    head.className = 'user-card-head';
    head.innerHTML = `
      <div>${escapeHtml(user.name)} <span class="meta">· age ${user.age} · id ${user.id}</span></div>
    `;

    const actions = document.createElement('div');
    actions.className = 'user-card-actions';

    const editBtn = document.createElement('button');
    editBtn.textContent = 'Edit';
    editBtn.className = 'edit';
    editBtn.onclick = () => startEditUser(user);

    const delBtn = document.createElement('button');
    delBtn.textContent = 'Delete';
    delBtn.className = 'danger';
    delBtn.onclick = () => deleteUser(user.id);

    actions.appendChild(editBtn);
    actions.appendChild(delBtn);
    head.appendChild(actions);
    card.appendChild(head);

    const postsList = document.createElement('div');
    postsList.className = 'posts-list';

    if (!user.posts || !user.posts.length) {
      postsList.innerHTML = '<div class="empty">No posts yet</div>';
    } else {
      user.posts.forEach(post => {
        const row = document.createElement('div');
        row.className = 'post-row';
        row.innerHTML = `
          <span>"${escapeHtml(post.text)}"<span class="path">${escapeHtml(post.imagePath || '')}</span></span>
        `;
        const rowActions = document.createElement('span');
        const editP = document.createElement('button');
        editP.textContent = 'Edit';
        editP.className = 'edit';
        editP.onclick = () => startEditPost(post);
        const delP = document.createElement('button');
        delP.textContent = 'Delete';
        delP.className = 'danger';
        delP.onclick = () => deletePost(post.id);
        rowActions.appendChild(editP);
        rowActions.appendChild(delP);
        row.appendChild(rowActions);
        postsList.appendChild(row);
      });
    }

    card.appendChild(postsList);
    usersList.appendChild(card);
  });
}

function escapeHtml(str) {
  const div = document.createElement('div');
  div.textContent = str ?? '';
  return div.innerHTML;
}

function resetUserForm() {
  userForm.reset();
  userIdField.value = '';
  userFormTitle.textContent = 'Add User';
  userSubmitBtn.textContent = 'Create User';
  userCancelBtn.classList.add('hidden');
  userError.textContent = '';
}

function startEditUser(user) {
  userIdField.value = user.id;
  uname.value = user.name;
  uage.value = user.age;
  upass.value = '';
  userFormTitle.textContent = `Edit User #${user.id}`;
  userSubmitBtn.textContent = 'Update User';
  userCancelBtn.classList.remove('hidden');
  userError.textContent = '';
}

userCancelBtn.onclick = resetUserForm;

userForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  userError.textContent = '';

  const id = userIdField.value;
  const body = {
    name: uname.value,
    age: Number(uage.value),
    password: upass.value
  };

  try {
    const res = await fetch(`${BASE}/users${id ? '/' + id : ''}`, {
      method: id ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });

    if (!res.ok) {
      const errBody = await res.json().catch(() => ({}));
      userError.textContent = formatError(errBody);
      return;
    }

    resetUserForm();
    loadUsers();
  } catch (err) {
    userError.textContent = 'Request failed. Check the backend is running.';
  }
});

async function deleteUser(id) {
  const ok = await showConfirm(`Delete user #${id}? This also deletes their posts.`);
  if (!ok) return;
  await fetch(`${BASE}/users/${id}`, { method: 'DELETE' });
  loadUsers();
}

function resetPostForm() {
  postForm.reset();
  postIdField.value = '';
  postFormTitle.textContent = 'Add Post';
  postSubmitBtn.textContent = 'Create Post';
  postCancelBtn.classList.add('hidden');
  postError.textContent = '';
}

function startEditPost(post) {
  postIdField.value = post.id;
  ptext.value = post.text;
  ppath.value = post.imagePath || '';
  if (post.userId) puser.value = post.userId;
  postFormTitle.textContent = `Edit Post #${post.id}`;
  postSubmitBtn.textContent = 'Update Post';
  postCancelBtn.classList.remove('hidden');
  postError.textContent = '';
}

postCancelBtn.onclick = resetPostForm;

postForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  postError.textContent = '';

  const id = postIdField.value;
  const body = {
    text: ptext.value,
    imagePath: ppath.value,
    userId: Number(puser.value)
  };

  try {
    const res = await fetch(`${BASE}/posts${id ? '/' + id : ''}`, {
      method: id ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });

    if (!res.ok) {
      const errBody = await res.json().catch(() => ({}));
      postError.textContent = formatError(errBody);
      return;
    }

    resetPostForm();
    loadUsers();
  } catch (err) {
    postError.textContent = 'Request failed. Check the backend is running.';
  }
});

async function deletePost(id) {
  const ok = await showConfirm(`Delete post #${id}?`);
  if (!ok) return;
  await fetch(`${BASE}/posts/${id}`, { method: 'DELETE' });
  loadUsers();
}

function formatError(errBody) {
  if (errBody && errBody.error) return errBody.error;
  if (errBody && typeof errBody === 'object') {
    const msgs = Object.values(errBody).filter(v => typeof v === 'string');
    if (msgs.length) return msgs.join(' · ');
  }
  return 'Something went wrong.';
}

refreshBtn.onclick = loadUsers;

loadUsers();