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

function validateEmail(email) {
    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!email) {
        return 'Email is required';
    }
    if (!emailPattern.test(email)) {
        return 'Invalid email format';
    }
    if (email.length > 50) {
        return 'Email must be less than 50 characters';
    }
    return '';
}

function validatePassword(oldPassword, newPassword, confirmPassword) {
    if (!oldPassword) {
        return 'Old password is required';
    }
    if (!newPassword) {
        return 'New password is required';
    }
    if (newPassword.length < 8 || newPassword.length > 50) {
        return 'New password must be between 8 and 50 characters';
    }
    if (!confirmPassword) {
        return 'Password confirmation is required';
    }
    if (newPassword !== confirmPassword) {
        return 'New password and confirmation do not match';
    }
    return '';
}

function changeEmail() {
    const newEmail = $('#newEmail').val();
    const emailError = $('#emailError');

    const error = validateEmail(newEmail);
    if (error) {
        emailError.text(error);
        return;
    }

    emailError.text('');

    $.ajax({
        type: 'POST',
        url: '/users/profile/change-email',
        contentType: 'application/json',
        data: JSON.stringify({ email: newEmail }),
        success: function(response) {
            if (response.success) {
                $('#email').val(newEmail);
                $('#changeEmailModal').modal('hide');
            } else {
                alert(response.message);
            }
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
            alert('An error occurred while changing email. Please try again.');
        }
    });
}

function changePassword() {
    const oldPassword = $('#oldPassword').val();
    const newPassword = $('#newPassword').val();
    const confirmPassword = $('#confirmPassword').val();
    const passwordError = $('#passwordError');

    const error = validatePassword(oldPassword, newPassword, confirmPassword);
    if (error) {
        passwordError.text(error);
        return;
    }

    passwordError.text('');

    $.ajax({
        type: 'POST',
        url: '/users/profile/change-password',
        contentType: 'application/json',
        data: JSON.stringify({ oldPassword: oldPassword, newPassword: newPassword, confirmPassword: confirmPassword }),
        success: function(response) {
            if (response.success) {
                $('#changePasswordModal').modal('hide');
                alert('Password changed successfully.');
            } else {
                alert(response.message);
            }
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
            alert('An error occurred while changing password. Please try again.');
        }
    });
}

$(document).ready(function() {
    window.changeEmail = changeEmail;
    window.changePassword = changePassword;
});
