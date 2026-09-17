let foundEmployee = null;

document.getElementById('findBtn').addEventListener('click', function () {
    const id = document.getElementById('deleteId').value;
    const resultMessage = document.getElementById('resultMessage');
    const detailsBox = document.getElementById('employeeDetails');
    const deleteBtn = document.getElementById('deleteBtn');

    resultMessage.textContent = '';
    detailsBox.style.display = 'none';
    deleteBtn.style.display = 'none';
    foundEmployee = null;

    if (!id) {
        return;
    }

    fetch(`/api/employees/byIds?ids=${id}`)
        .then(response => response.json())
        .then(employees => {
            if (employees.length === 0) {
                resultMessage.textContent = `No employee found with ID: ${id}`;
                resultMessage.className = 'result-message error';
                return;
            }

            foundEmployee = employees[0];
            document.getElementById('detailName').textContent = foundEmployee.name;
            document.getElementById('detailAge').textContent = foundEmployee.age;
            document.getElementById('detailPhone').textContent = foundEmployee.phoneNumber;

            detailsBox.style.display = 'block';
            deleteBtn.style.display = 'block';
        })
        .catch(error => {
            resultMessage.textContent = 'Something went wrong while searching.';
            resultMessage.className = 'result-message error';
            console.error('Error finding employee:', error);
        });
});

document.getElementById('deleteBtn').addEventListener('click', function () {
    if (!foundEmployee) {
        return;
    }

    document.getElementById('modalMessage').textContent =
        `Are you sure you want to delete "${foundEmployee.name}" (ID: ${foundEmployee.id})?`;
    document.getElementById('confirmModal').style.display = 'flex';
});

document.getElementById('modalCancelBtn').addEventListener('click', function () {
    document.getElementById('confirmModal').style.display = 'none';
});

document.getElementById('modalConfirmBtn').addEventListener('click', function () {
    const resultMessage = document.getElementById('resultMessage');
    const id = foundEmployee.id;

    fetch(`/api/employees/${id}`, {
        method: 'DELETE'
    })
        .then(response => {
            document.getElementById('confirmModal').style.display = 'none';

            if (response.status === 204) {
                resultMessage.textContent = `Employee "${foundEmployee.name}" (ID: ${id}) deleted successfully.`;
                resultMessage.className = 'result-message success';
                document.getElementById('deleteId').value = '';
                document.getElementById('employeeDetails').style.display = 'none';
                document.getElementById('deleteBtn').style.display = 'none';
                foundEmployee = null;
                return null;
            }
            return response.json();
        })
        .then(body => {
            if (body) {
                resultMessage.textContent = body.message;
                resultMessage.className = 'result-message error';
            }
        })
        .catch(error => {
            resultMessage.textContent = 'Something went wrong. Please try again.';
            resultMessage.className = 'result-message error';
            console.error('Error deleting employee:', error);
        });
});