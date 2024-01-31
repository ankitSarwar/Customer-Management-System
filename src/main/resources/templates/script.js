document.getElementById('registrationForm').addEventListener('submit', function(event) {
    event.preventDefault();

    // Fetch form values
    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const street = document.getElementById('street').value;
    const address = document.getElementById('address').value;
    const city = document.getElementById('city').value;
    const state = document.getElementById('state').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const phone = document.getElementById('phone').value;
    const role = document.getElementById('role').value;

    // Create a user object with the form values
    const user = {
        firstName,
        lastName,
        street,
        address,
        city,
        state,
        email,
        password,
        phone,
        role
    };

    // Make an AJAX request or use Fetch API to send the user data to your backend
    // Example: You can use the fetch API to send a POST request to your backend endpoint

    fetch('/api/addNewUser', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
    })
    .then(response => response.json())
    .then(data => {
        // Handle the response from the server
        console.log(data);
        alert(data.message); // Display a success or error message
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred. Please try again.'); // Display an error message
    });
});
