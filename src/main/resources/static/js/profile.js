$(document).ready(function () {
    toastrOptions();

    handleFormSubmission('#changeEmailForm', changeEmail);
    handleFormSubmission('#changePasswordForm', changePassword);
});

function toastrOptions() {
    toastr.options = {
        positionClass: 'toast-bottom-right'
    };
}

function handleFormSubmission(formSelector, submitCallback) {
    $(formSelector).submit(function (event) {
        event.preventDefault();
        const form = $(this);
        clearErrors();
        disableSubmitButton(true, form);
        const formData = getFormData(form);
        submitCallback(formData, form);
    });
}

function getFormData($form) {
    let unindexed_array = $form.serializeArray();
    let indexed_array = {};

    $.map(unindexed_array, function (n) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function changeEmail(formData, form) {
    $.ajax({
        url: '/users/profile/change-email',
        method: 'POST',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        success: function (response) {
            if (response.success) {
                $('#email').val(formData.newEmail);
                $('#changeEmailModal').modal('hide');
                form[0].reset();
                toastr.success('Email changed successfully.');
            } else {
                toastr.error(response.message);
            }
            disableSubmitButton(false, form);
        },
        error: function (xhr, status, error) {
            disableSubmitButton(false, form);
            if (xhr.status === 400) {
                displayErrors(xhr.responseJSON);
            } else {
                toastr.error('An error occurred while changing email.');
            }
        }
    });
}

function changePassword(formData, form) {
    $.ajax({
        url: '/users/profile/change-password',
        method: 'POST',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        success: function (response) {
            if (response.success) {
                $('#changePasswordModal').modal('hide');
                form[0].reset();
                toastr.success('Password changed successfully.');
            } else {
                toastr.error(response.message);
            }
            disableSubmitButton(false, form);
        },
        error: function (xhr, status, error) {
            disableSubmitButton(false, form);
            if (xhr.status === 400) {
                displayErrors(xhr.responseJSON);
            } else {
                toastr.error('An error occurred while changing password.');
            }
        }
    });
}

function clearErrors() {
    $('.text-danger').text('');
}

function displayErrors(errors) {
    clearErrors();
    for (const [field, message] of Object.entries(errors)) {
        $(`#${field}Error`).text(message);
    }
}

function disableSubmitButton(disable, form) {
    form.find('button[type="submit"]').prop('disabled', disable);
}
