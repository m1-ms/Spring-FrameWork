document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', () => {
        document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));
        item.classList.add('active');

        const section = item.dataset.section;
        document.getElementById('section-title').textContent = item.textContent;

        document.querySelectorAll('.section-body').forEach(s => s.hidden = true);
        document.getElementById('section-' + section).hidden = false;

        if (section === 'dashboard') {
            loadDashboard();
        }

        if (section === 'register') {
            loadRegisterOptions();
        }

        if (section === 'students') {
            loadStudentsList();
            document.getElementById('studentDetailView').innerHTML = '<div class="empty-state">Select a student to see details.</div>';
        }

        if (section === 'courses') {
            loadCoursesList();
            document.getElementById('courseDetailView').innerHTML = '<div class="empty-state">Select a course to see details.</div>';
        }

        if (section === 'instructors') {
            loadInstructorsList();
            document.getElementById('instructorDetailView').innerHTML = '<div class="empty-state">Select an instructor to see details.</div>';
        }
    });
});

async function loadDashboard() {
    const [students, courses, instructors] = await Promise.all([
        fetch('/api/students').then(r => r.json()),
        fetch('/api/courses').then(r => r.json()),
        fetch('/api/instructors').then(r => r.json())
    ]);

    document.getElementById('dashboardStudentsCount').textContent = students.length;
    document.getElementById('dashboardCoursesCount').textContent = courses.length;
    document.getElementById('dashboardInstructorsCount').textContent = instructors.length;
}

async function loadRegisterOptions() {
    const studentSelect = document.getElementById('registerStudentSelect');
    const courseSelect = document.getElementById('registerCourseSelect');

    const [students, courses] = await Promise.all([
        fetch('/api/students').then(r => r.json()),
        fetch('/api/courses').then(r => r.json())
    ]);

    studentSelect.innerHTML = '<option value="">Choose a student</option>' +
        students.map(s => `<option value="${s.id}">${s.name}</option>`).join('');

    courseSelect.innerHTML = '<option value="">Choose a course</option>' +
        courses.map(c => `<option value="${c.id}">${c.title}</option>`).join('');

    document.getElementById('registerMessage').textContent = '';
}

document.getElementById('registerBtn').addEventListener('click', async () => {
    const studentId = document.getElementById('registerStudentSelect').value;
    const courseId = document.getElementById('registerCourseSelect').value;
    const message = document.getElementById('registerMessage');

    if (!studentId || !courseId) {
        message.textContent = 'Choose both a student and a course.';
        message.className = 'form-message error';
        return;
    }

    const response = await fetch(`/api/students/${studentId}/register/${courseId}`, {
        method: 'POST'
    });

    if (response.ok) {
        message.textContent = 'Student registered successfully.';
        message.className = 'form-message success';
    } else {
        const errorText = await response.text();
        message.textContent = errorText || 'Something went wrong. Try again.';
        message.className = 'form-message error';
    }
});

document.getElementById('addStudentBtn').addEventListener('click', async () => {
    const name = document.getElementById('studentNameInput').value.trim();
    const email = document.getElementById('studentEmailInput').value.trim();
    const phoneNumber = document.getElementById('studentPhoneInput').value.trim();
    const age = document.getElementById('studentAgeInput').value.trim();
    const message = document.getElementById('addStudentMessage');

    if (!name || !email) {
        message.textContent = 'Enter both name and email.';
        message.className = 'form-message error';
        return;
    }

    const response = await fetch('/api/students', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            name,
            email,
            phoneNumber: phoneNumber || null,
            age: age ? Number(age) : null
        })
    });

    if (response.ok) {
        message.textContent = 'Student added successfully.';
        message.className = 'form-message success';
        document.getElementById('studentNameInput').value = '';
        document.getElementById('studentEmailInput').value = '';
        document.getElementById('studentPhoneInput').value = '';
        document.getElementById('studentAgeInput').value = '';
    } else {
        const errorText = await response.text();
        message.textContent = errorText || 'Something went wrong. Try again.';
        message.className = 'form-message error';
    }
});

document.getElementById('addCourseBtn').addEventListener('click', async () => {
    const title = document.getElementById('courseTitleInput').value.trim();
    const description = document.getElementById('courseDescriptionInput').value.trim();
    const message = document.getElementById('addCourseMessage');

    if (!title) {
        message.textContent = 'Enter a course title.';
        message.className = 'form-message error';
        return;
    }

    const response = await fetch('/api/courses', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, description })
    });

    if (response.ok) {
        message.textContent = 'Course added successfully.';
        message.className = 'form-message success';
        document.getElementById('courseTitleInput').value = '';
        document.getElementById('courseDescriptionInput').value = '';
    } else {
        const errorText = await response.text();
        message.textContent = errorText || 'Something went wrong. Try again.';
        message.className = 'form-message error';
    }
});

document.getElementById('addInstructorBtn').addEventListener('click', async () => {
    const name = document.getElementById('instructorNameInput').value.trim();
    const email = document.getElementById('instructorEmailInput').value.trim();
    const phoneNumber = document.getElementById('instructorPhoneInput').value.trim();
    const age = document.getElementById('instructorAgeInput').value.trim();
    const message = document.getElementById('addInstructorMessage');

    if (!name || !email) {
        message.textContent = 'Enter both name and email.';
        message.className = 'form-message error';
        return;
    }

    const response = await fetch('/api/instructors', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            name,
            email,
            phoneNumber: phoneNumber || null,
            age: age ? Number(age) : null
        })
    });

    if (response.ok) {
        message.textContent = 'Instructor added successfully.';
        message.className = 'form-message success';
        document.getElementById('instructorNameInput').value = '';
        document.getElementById('instructorEmailInput').value = '';
        document.getElementById('instructorPhoneInput').value = '';
        document.getElementById('instructorAgeInput').value = '';
    } else {
        const errorText = await response.text();
        message.textContent = errorText || 'Something went wrong. Try again.';
        message.className = 'form-message error';
    }
});

async function loadStudentsList() {
    const students = await fetch('/api/students').then(r => r.json());
    const tbody = document.getElementById('studentsTableBody');

    tbody.innerHTML = students.map(s => {
        const isActive = Boolean(s.active);
        return `
            <tr data-student-id="${s.id}">
                <td><span class="row-link">${s.name}</span></td>
                <td>${s.email}</td>
                <td><span class="status-badge ${isActive ? 'status-active' : 'status-inactive'}">${isActive ? 'Active' : 'Inactive'}</span></td>
                <td class="chevron">&#8250;</td>
            </tr>
        `;
    }).join('');

    tbody.querySelectorAll('.row-link, .chevron').forEach(el => {
        el.addEventListener('click', () => {
            const row = el.closest('tr');
            tbody.querySelectorAll('tr').forEach(r => r.classList.remove('selected'));
            row.classList.add('selected');
            showStudentDetail(row.dataset.studentId);
        });
    });
}

async function showStudentDetail(studentId) {
    const student = await fetch(`/api/students/${studentId}`).then(r => r.json());
    renderStudentDetail(student);
}

function renderStudentDetail(student) {
    const detailView = document.getElementById('studentDetailView');
    const isActive = Boolean(student.active);

    const coursesHtml = student.courses.length
        ? student.courses.map(c => `
            <div class="course-item">
                <div class="course-title">${c.title}</div>
                <div class="course-instructor">${c.instructor ? 'Instructor: ' + c.instructor.name : 'No instructor assigned'}</div>
            </div>
        `).join('')
        : '<p style="color: var(--text-secondary); font-size: 14px;">Not enrolled in any course.</p>';

    detailView.innerHTML = `
        <div class="detail-header">
            <div>
                <h3>${student.name}</h3>
                <p class="email">${student.email}${student.phoneNumber ? ' · ' + student.phoneNumber : ''}${student.age != null ? ' · ' + student.age + ' yrs' : ''}</p>
            </div>
            <span class="status-badge ${isActive ? 'status-active' : 'status-inactive'}">${isActive ? 'Active' : 'Inactive'}</span>
        </div>
        <div class="detail-actions">
            <button class="btn-secondary" id="editStudentBtn">Edit</button>
            <button class="btn-secondary" id="toggleActiveBtn">${isActive ? 'Set Inactive' : 'Set Active'}</button>
            <button class="btn-danger" id="deleteStudentBtn">Delete</button>
        </div>
        <h4>Enrolled Courses</h4>
        ${coursesHtml}
    `;

    document.getElementById('editStudentBtn').addEventListener('click', () => {
        renderStudentEditForm(student);
    });

    document.getElementById('toggleActiveBtn').addEventListener('click', async () => {
        await fetch(`/api/students/${student.id}/toggle-active`, { method: 'PUT' });
        showStudentDetail(student.id);
        loadStudentsList();
    });

    document.getElementById('deleteStudentBtn').addEventListener('click', () => {
        showConfirmDialog(`Delete ${student.name}? This cannot be undone.`, async () => {
            await fetch(`/api/students/${student.id}`, { method: 'DELETE' });
            detailView.innerHTML = '<div class="empty-state">Select a student to see details.</div>';
            loadStudentsList();
        });
    });
}

function renderStudentEditForm(student) {
    const detailView = document.getElementById('studentDetailView');

    detailView.innerHTML = `
        <span class="back-link" id="cancelEditLink">Cancel</span>
        <h3>Edit Student</h3>
        <div class="form-group" style="margin-top: 16px;">
            <label for="editNameInput">Name</label>
            <input type="text" id="editNameInput" value="${student.name}">
        </div>
        <div class="form-group">
            <label for="editEmailInput">Email</label>
            <input type="email" id="editEmailInput" value="${student.email}">
        </div>
        <button class="btn-primary" id="saveEditBtn">Save Changes</button>
        <p id="editMessage" class="form-message"></p>
    `;

    document.getElementById('cancelEditLink').addEventListener('click', () => {
        showStudentDetail(student.id);
    });

    document.getElementById('saveEditBtn').addEventListener('click', async () => {
        const name = document.getElementById('editNameInput').value.trim();
        const email = document.getElementById('editEmailInput').value.trim();
        const message = document.getElementById('editMessage');

        if (!name || !email) {
            message.textContent = 'Enter both name and email.';
            message.className = 'form-message error';
            return;
        }

        const response = await fetch(`/api/students/${student.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email })
        });

        if (response.ok) {
            showStudentDetail(student.id);
            loadStudentsList();
        } else {
            const errorText = await response.text();
            message.textContent = errorText || 'Something went wrong. Try again.';
            message.className = 'form-message error';
        }
    });
}

async function loadCoursesList() {
    const courses = await fetch('/api/courses').then(r => r.json());
    const tbody = document.getElementById('coursesTableBody');

    tbody.innerHTML = courses.map(c => {
        const isActive = Boolean(c.active);
        return `
            <tr data-course-id="${c.id}">
                <td><span class="row-link">${c.title}</span></td>
                <td><span class="status-badge ${isActive ? 'status-active' : 'status-inactive'}">${isActive ? 'Active' : 'Inactive'}</span></td>
                <td class="chevron">&#8250;</td>
            </tr>
        `;
    }).join('');

    tbody.querySelectorAll('.row-link, .chevron').forEach(el => {
        el.addEventListener('click', () => {
            const row = el.closest('tr');
            tbody.querySelectorAll('tr').forEach(r => r.classList.remove('selected'));
            row.classList.add('selected');
            showCourseDetail(row.dataset.courseId);
        });
    });
}

async function showCourseDetail(courseId) {
    const course = await fetch(`/api/courses/${courseId}`).then(r => r.json());
    renderCourseDetail(course);
}

function renderCourseDetail(course) {
    const detailView = document.getElementById('courseDetailView');
    const isActive = Boolean(course.active);

    const instructorHtml = course.instructor
        ? `<div class="course-item">
                <div class="course-title">${course.instructor.name}</div>
                <div class="course-instructor">${course.instructor.email}</div>
           </div>`
        : '<p style="color: var(--text-secondary); font-size: 14px;">No instructor assigned.</p>';

    const studentsHtml = course.students.length
        ? course.students.map(s => `
            <div class="course-item">
                <div class="course-title">${s.name}</div>
                <div class="course-instructor">${s.email}</div>
            </div>
        `).join('')
        : '<p style="color: var(--text-secondary); font-size: 14px;">No students enrolled.</p>';

    detailView.innerHTML = `
        <div class="detail-header">
            <div>
                <h3>${course.title}</h3>
                <p class="subtitle">${course.description || ''}</p>
            </div>
            <span class="status-badge ${isActive ? 'status-active' : 'status-inactive'}">${isActive ? 'Active' : 'Inactive'}</span>
        </div>

        <div class="detail-actions">
            <button class="btn-secondary" id="editCourseBtn">Edit</button>
            <button class="btn-secondary" id="toggleCourseActiveBtn">${isActive ? 'Set Inactive' : 'Set Active'}</button>
            <button class="btn-danger" id="deleteCourseBtn">Delete</button>
        </div>

        <h4>Instructor</h4>
        ${instructorHtml}

        <div class="form-group" style="margin-top: 16px;">
            <label for="assignInstructorSelect">Assign Instructor</label>
            <select id="assignInstructorSelect">
                <option value="">Choose an instructor</option>
            </select>
            <button id="assignInstructorBtn" class="btn-secondary" style="margin-top: 8px; width: 100%;">Assign</button>
            <p id="assignInstructorMessage" class="form-message"></p>
        </div>

        <h4 style="margin-top:20px;">Enrolled Students</h4>
        ${studentsHtml}
    `;

    loadAssignInstructorOptions(course);

    document.getElementById('assignInstructorBtn').addEventListener('click', async () => {
        const instructorId = document.getElementById('assignInstructorSelect').value;
        const message = document.getElementById('assignInstructorMessage');

        if (!instructorId) {
            message.textContent = 'Choose an instructor.';
            message.className = 'form-message error';
            return;
        }

        const response = await fetch(`/api/courses/${course.id}/assign-instructor/${instructorId}`, {
            method: 'PUT'
        });

        if (response.ok) {
            showCourseDetail(course.id);
            loadCoursesList();
        } else {
            const errorText = await response.text();
            message.textContent = errorText || 'Something went wrong. Try again.';
            message.className = 'form-message error';
        }
    });

    document.getElementById('editCourseBtn').addEventListener('click', () => {
        renderCourseEditForm(course);
    });

    document.getElementById('toggleCourseActiveBtn').addEventListener('click', async () => {
        await fetch(`/api/courses/${course.id}/toggle-active`, { method: 'PUT' });
        showCourseDetail(course.id);
        loadCoursesList();
    });

    document.getElementById('deleteCourseBtn').addEventListener('click', () => {
        showConfirmDialog(`Delete ${course.title}? This cannot be undone.`, async () => {
            await fetch(`/api/courses/${course.id}`, { method: 'DELETE' });
            detailView.innerHTML = '<div class="empty-state">Select a course to see details.</div>';
            loadCoursesList();
        });
    });
}

function renderCourseEditForm(course) {
    const detailView = document.getElementById('courseDetailView');

    detailView.innerHTML = `
        <span class="back-link" id="cancelCourseEditLink">Cancel</span>
        <h3>Edit Course</h3>
        <div class="form-group" style="margin-top: 16px;">
            <label for="editCourseTitleInput">Title</label>
            <input type="text" id="editCourseTitleInput" value="${course.title}">
        </div>
        <div class="form-group">
            <label for="editCourseDescriptionInput">Description</label>
            <input type="text" id="editCourseDescriptionInput" value="${course.description || ''}">
        </div>
        <button class="btn-primary" id="saveCourseEditBtn">Save Changes</button>
        <p id="editCourseMessage" class="form-message"></p>
    `;

    document.getElementById('cancelCourseEditLink').addEventListener('click', () => {
        showCourseDetail(course.id);
    });

    document.getElementById('saveCourseEditBtn').addEventListener('click', async () => {
        const title = document.getElementById('editCourseTitleInput').value.trim();
        const description = document.getElementById('editCourseDescriptionInput').value.trim();
        const message = document.getElementById('editCourseMessage');

        if (!title) {
            message.textContent = 'Enter a course title.';
            message.className = 'form-message error';
            return;
        }

        const response = await fetch(`/api/courses/${course.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, description })
        });

        if (response.ok) {
            showCourseDetail(course.id);
            loadCoursesList();
        } else {
            const errorText = await response.text();
            message.textContent = errorText || 'Something went wrong. Try again.';
            message.className = 'form-message error';
        }
    });
}

async function loadAssignInstructorOptions(course) {
    const allInstructors = await fetch('/api/instructors').then(r => r.json());
    const select = document.getElementById('assignInstructorSelect');

    select.innerHTML = '<option value="">Choose an instructor</option>' +
        allInstructors.map(i => `<option value="${i.id}" ${course.instructor && course.instructor.id === i.id ? 'selected' : ''}>${i.name}</option>`).join('');
}

async function loadInstructorsList() {
    const instructors = await fetch('/api/instructors').then(r => r.json());
    const tbody = document.getElementById('instructorsTableBody');

    tbody.innerHTML = instructors.map(i => `
        <tr data-instructor-id="${i.id}">
            <td><span class="row-link">${i.name}</span></td>
            <td>${i.email}</td>
            <td class="chevron">&#8250;</td>
        </tr>
    `).join('');

    tbody.querySelectorAll('.row-link, .chevron').forEach(el => {
        el.addEventListener('click', () => {
            const row = el.closest('tr');
            tbody.querySelectorAll('tr').forEach(r => r.classList.remove('selected'));
            row.classList.add('selected');
            showInstructorDetail(row.dataset.instructorId);
        });
    });
}

async function showInstructorDetail(instructorId) {
    const instructor = await fetch(`/api/instructors/${instructorId}`).then(r => r.json());
    renderInstructorDetail(instructor);
}

function renderInstructorDetail(instructor) {
    const detailView = document.getElementById('instructorDetailView');

    const coursesHtml = instructor.courses.length
        ? instructor.courses.map(c => {
            const studentsHtml = c.students.length
                ? c.students.map(s => `<div class="student-line">${s.name}</div>`).join('')
                : `<div class="student-line">No students enrolled</div>`;

            return `
                <div class="course-item">
                    <div class="course-title">${c.title}</div>
                    <div class="course-instructor">${c.students.length} student(s) enrolled</div>
                    <div class="student-list">${studentsHtml}</div>
                </div>
            `;
        }).join('')
        : '<p style="color: var(--text-secondary); font-size: 14px;">Not teaching any course.</p>';

    detailView.innerHTML = `
        <h3>${instructor.name}</h3>
        <p class="email">${instructor.email}${instructor.phoneNumber ? ' · ' + instructor.phoneNumber : ''}${instructor.age != null ? ' · ' + instructor.age + ' yrs' : ''}</p>

        <div class="detail-actions">
            <button class="btn-secondary" id="editInstructorBtn">Edit</button>
            <button class="btn-danger" id="deleteInstructorBtn">Delete</button>
        </div>

        <h4>Courses Taught</h4>
        ${coursesHtml}
    `;

    document.getElementById('editInstructorBtn').addEventListener('click', () => {
        renderInstructorEditForm(instructor);
    });

    document.getElementById('deleteInstructorBtn').addEventListener('click', () => {
        showConfirmDialog(`Delete ${instructor.name}? This cannot be undone.`, async () => {
            await fetch(`/api/instructors/${instructor.id}`, { method: 'DELETE' });
            detailView.innerHTML = '<div class="empty-state">Select an instructor to see details.</div>';
            loadInstructorsList();
        });
    });
}

function renderInstructorEditForm(instructor) {
    const detailView = document.getElementById('instructorDetailView');

    detailView.innerHTML = `
        <span class="back-link" id="cancelInstructorEditLink">Cancel</span>
        <h3>Edit Instructor</h3>
        <div class="form-group" style="margin-top: 16px;">
            <label for="editInstructorNameInput">Name</label>
            <input type="text" id="editInstructorNameInput" value="${instructor.name}">
        </div>
        <div class="form-group">
            <label for="editInstructorEmailInput">Email</label>
            <input type="email" id="editInstructorEmailInput" value="${instructor.email}">
        </div>
        <button class="btn-primary" id="saveInstructorEditBtn">Save Changes</button>
        <p id="editInstructorMessage" class="form-message"></p>
    `;

    document.getElementById('cancelInstructorEditLink').addEventListener('click', () => {
        showInstructorDetail(instructor.id);
    });

    document.getElementById('saveInstructorEditBtn').addEventListener('click', async () => {
        const name = document.getElementById('editInstructorNameInput').value.trim();
        const email = document.getElementById('editInstructorEmailInput').value.trim();
        const message = document.getElementById('editInstructorMessage');

        if (!name || !email) {
            message.textContent = 'Enter both name and email.';
            message.className = 'form-message error';
            return;
        }

        const response = await fetch(`/api/instructors/${instructor.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email })
        });

        if (response.ok) {
            showInstructorDetail(instructor.id);
            loadInstructorsList();
        } else {
            const errorText = await response.text();
            message.textContent = errorText || 'Something went wrong. Try again.';
            message.className = 'form-message error';
        }
    });
}

function showConfirmDialog(message, onConfirm) {
    const overlay = document.getElementById('confirmModalOverlay');
    const msgEl = document.getElementById('confirmModalMessage');
    const confirmBtn = document.getElementById('confirmModalConfirm');
    const cancelBtn = document.getElementById('confirmModalCancel');

    msgEl.textContent = message;
    overlay.hidden = false;

    const cleanup = () => {
        overlay.hidden = true;
        confirmBtn.removeEventListener('click', onConfirmClick);
        cancelBtn.removeEventListener('click', onCancelClick);
    };

    const onConfirmClick = () => {
        cleanup();
        onConfirm();
    };

    const onCancelClick = () => {
        cleanup();
    };

    confirmBtn.addEventListener('click', onConfirmClick);
    cancelBtn.addEventListener('click', onCancelClick);
}

loadDashboard();