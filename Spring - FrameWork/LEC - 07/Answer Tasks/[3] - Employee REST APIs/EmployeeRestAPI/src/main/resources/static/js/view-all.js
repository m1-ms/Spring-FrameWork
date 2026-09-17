let currentPage = 0;
const pageSize = 10;

function loadEmployees(page) {
    fetch(`/api/employees/paged?page=${page}&size=${pageSize}`)
        .then(response => response.json())
        .then(data => {
            currentPage = data.currentPage;
            renderTable(data.content);
            updatePaginationControls(data.currentPage, data.totalPages);
        })
        .catch(error => {
            console.error('Error fetching employees:', error);
        });
}

function renderTable(employees) {
    const tableBody = document.getElementById('employeeTableBody');
    const emptyMessage = document.getElementById('emptyMessage');
    tableBody.innerHTML = '';

    if (employees.length === 0) {
        emptyMessage.style.display = 'block';
        return;
    }

    emptyMessage.style.display = 'none';

    employees.forEach(employee => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${employee.id}</td>
            <td>${employee.name}</td>
            <td>${employee.age}</td>
            <td>${employee.phoneNumber}</td>
        `;
        tableBody.appendChild(row);
    });
}

function updatePaginationControls(current, totalPages) {
    const displayTotal = totalPages === 0 ? 1 : totalPages;
    document.getElementById('pageInfo').textContent = `Page ${current + 1} of ${displayTotal}`;
    document.getElementById('prevBtn').disabled = current === 0;
    document.getElementById('nextBtn').disabled = current >= totalPages - 1;
}

document.addEventListener('DOMContentLoaded', () => {
    loadEmployees(currentPage);

    document.getElementById('prevBtn').addEventListener('click', () => {
        if (currentPage > 0) {
            loadEmployees(currentPage - 1);
        }
    });

    document.getElementById('nextBtn').addEventListener('click', () => {
        loadEmployees(currentPage + 1);
    });
});