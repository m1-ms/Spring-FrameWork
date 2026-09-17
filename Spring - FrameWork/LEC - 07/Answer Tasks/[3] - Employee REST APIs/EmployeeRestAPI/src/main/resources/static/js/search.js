const endpoints = {
    derived: '/api/employees/search/derived',
    native: '/api/employees/search/native',
    jpql: '/api/employees/search/jpql'
};

document.getElementById('searchBtn').addEventListener('click', function () {
    const name = document.getElementById('searchName').value.trim();
    const searchType = document.querySelector('input[name="searchType"]:checked').value;
    const tableBody = document.getElementById('searchTableBody');
    const emptyMessage = document.getElementById('emptyMessage');

    tableBody.innerHTML = '';
    emptyMessage.style.display = 'none';

    if (!name) {
        return;
    }

    // الـ Native والـ JPQL محتاجين % في الآخر يدوي، الـ Derived لأ
    const queryValue = (searchType === 'derived') ? name : `${name}%`;
    const url = `${endpoints[searchType]}?name=${encodeURIComponent(queryValue)}`;

    fetch(url)
        .then(response => response.json())
        .then(employees => {
            if (employees.length === 0) {
                emptyMessage.style.display = 'block';
                return;
            }

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
        })
        .catch(error => {
            console.error('Error searching employees:', error);
        });
});