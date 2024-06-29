/*!
* Start Bootstrap - Modern Business v5.0.7 (https://startbootstrap.com/template-overviews/modern-business)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-modern-business/blob/master/LICENSE)
*/

document.addEventListener('DOMContentLoaded', function() {
    flatpickr("#date", {
        dateFormat: "Y-m-d"
    });
    flatpickr("#time", {
        enableTime: true,
        noCalendar: true,
        dateFormat: "H:i",
        time_24hr: true
    });
});

function changeUsername() {
    const newUsername = document.getElementById('newUsername').value;

    fetch('/users/profile/change-username', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        },
        body: JSON.stringify({ username: newUsername })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('username').value = newUsername;
                $('#changeUsernameModal').modal('hide');
            } else {
                alert(data.message);
            }
        })
        .catch(error => console.error('Error:', error));
}

function changeEmail() {
    const newEmail = document.getElementById('newEmail').value;

    fetch('/users/profile/change-email', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        },
        body: JSON.stringify({ email: newEmail })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('email').value = newEmail;
                $('#changeEmailModal').modal('hide');
            } else {
                alert(data.message);
            }
        })
        .catch(error => console.error('Error:', error));
}

function changePassword() {
    const newPassword = document.getElementById('newPassword').value;

    fetch('/users/profile/change-password', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        },
        body: JSON.stringify({ password: newPassword })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                $('#changePasswordModal').modal('hide');
            } else {
                alert(data.message);
            }
        })
        .catch(error => console.error('Error:', error));
}