document.getElementById('addEmployeeForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const employee = {
        name: document.getElementById('name').value,
        age: parseInt(document.getElementById('age').value),
        phoneNumber: document.getElementById('phoneNumber').value
    };

    const resultMessage = document.getElementById('resultMessage');

    fetch('/api/employees', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(employee)
    })
        .then(response => response.json().then(data => ({ status: response.status, body: data })))
        .then(({ status, body }) => {
            if (status === 200) {
                resultMessage.textContent = `Employee "${body.name}" saved successfully with ID: ${body.id}`;
                resultMessage.className = 'result-message success';
                document.getElementById('addEmployeeForm').reset();
            } else {
                resultMessage.textContent = body.message;
                resultMessage.className = 'result-message error';
            }
        })
        .catch(error => {
            resultMessage.textContent = 'Something went wrong. Please try again.';
            resultMessage.className = 'result-message error';
            console.error('Error saving employee:', error);
        });
});