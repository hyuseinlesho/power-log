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

$(document).ready(function() {
    window.changeEmail = changeEmail;
});

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
    window.changePassword = changePassword;
});

function validateExercise(name, type) {
    if (!name) {
        return 'Name is required';
    }
    if (!type) {
        return 'Name is required';
    }
    return '';
}

function addNewExercise() {
    const exerciseName = $('#exerciseName').val().trim();
    const exerciseType = $('#exerciseType').val();
    const exerciseError = $('#exerciseError');

    const error = validateExercise(exerciseName, exerciseType);
    if (error) {
        exerciseError.text(error);
        return;
    }

    exerciseError.text('');

    $.ajax({
        type: 'POST',
        url: '/exercises/add-new',
        contentType: 'application/json',
        data: JSON.stringify({ name: exerciseName, type: exerciseType }),
        success: function(response) {
            if (response.success) {
                const newOption = $('<option></option>')
                    .val(exerciseName)
                    .text(exerciseName);

                $('.exercise-select').each(function() {
                    $(this).append(newOption.clone());
                });

                $('#addNewExerciseModal').modal('hide');
                $('#exerciseName').val('');
                $('#exerciseType').val('');
            } else {
                exerciseError.text(response.message);
            }
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
            exerciseError.text('An error occurred while adding new exercise. Please try again.');
        }
    });
}

$(document).ready(function() {
    window.addNewExercise = addNewExercise;
});