$(document).ready(function () {
    $('#addNewExerciseForm').submit(function (event) {
        event.preventDefault();
        const form = $(this);
        clearErrors();
        disableSubmitButton(true, form);
        const formData = getFormData(form);
        addNewExercise(formData, form);
    });
});

function clearErrors() {
    $('.text-danger').text('');
}

function disableSubmitButton(disable, form) {
    form.find('button[type="submit"]').prop('disabled', disable);
}

function getFormData($form) {
    let unindexed_array = $form.serializeArray();
    let indexed_array = {};

    $.map(unindexed_array, function (n) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function addNewExercise(formData, form) {
    $.ajax({
        url: '/api/exercises/create',
        method: 'POST',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        success: function (response) {
            appendNewExercise(response, form);
            $('#addNewExerciseModal').modal('hide');
            form[0].reset();
        },
        error: function (xhr, status, error) {
            disableSubmitButton(false, form);
            if (xhr.status === 400) {
                displayErrors(xhr.responseJSON);
            } else {
                toastr.error('Error adding new exercise');
            }
        }
    });
}

function appendNewExercise(exercise, form) {
    const newOption = $('<option></option>')
        .val(exercise.name)
        .text(exercise.name);

    $('.exercise-select').each(function () {
        $(this).append(newOption.clone());
    });

    disableSubmitButton(false, form);
}

function displayErrors(errors) {
    clearErrors();
    for (const [field, message] of Object.entries(errors)) {
        $(`#${field}Error`).text(message);
    }
}