let currentEmployeeId = null;

document.getElementById('loadBtn').addEventListener('click', function () {
    const id = document.getElementById('loadId').value;
    const resultMessage = document.getElementById('resultMessage');
    const form = document.getElementById('updateEmployeeForm');

    if (!id) {
        return;
    }

    fetch(`/api/employees/byIds?ids=${id}`)
        .then(response => response.json())
        .then(employees => {
            if (employees.length === 0) {
                resultMessage.textContent = `No employee found with ID: ${id}`;
                resultMessage.className = 'result-message error';
                form.style.display = 'none';
                return;
            }

            const employee = employees[0];
            currentEmployeeId = employee.id;
            document.getElementById('name').value = employee.name;
            document.getElementById('age').value = employee.age;
            document.getElementById('phoneNumber').value = employee.phoneNumber;

            form.style.display = 'flex';
            resultMessage.textContent = '';
        })
        .catch(error => {
            resultMessage.textContent = 'Something went wrong while loading the employee.';
            resultMessage.className = 'result-message error';
            console.error('Error loading employee:', error);
        });
});

document.getElementById('updateEmployeeForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const employee = {
        id: currentEmployeeId,
        name: document.getElementById('name').value,
        age: parseInt(document.getElementById('age').value),
        phoneNumber: document.getElementById('phoneNumber').value
    };

    const resultMessage = document.getElementById('resultMessage');

    fetch('/api/employees', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(employee)
    })
        .then(response => response.json().then(data => ({ status: response.status, body: data })))
        .then(({ status, body }) => {
            if (status === 200) {
                resultMessage.textContent = `Employee "${body.name}" updated successfully.`;
                resultMessage.className = 'result-message success';
            } else {
                resultMessage.textContent = body.message;
                resultMessage.className = 'result-message error';
            }
        })
        .catch(error => {
            resultMessage.textContent = 'Something went wrong. Please try again.';
            resultMessage.className = 'result-message error';
            console.error('Error updating employee:', error);
        });
});