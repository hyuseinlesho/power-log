$(document).ready(function() {
    flatpickr("#date", {
        dateFormat: "Y-m-d"
    });
    flatpickr("#time", {
        enableTime: true,
        noCalendar: true,
        dateFormat: "H:i",
        time_24hr: true
    });

    $('#addNewExerciseForm').submit(function(event) {
        event.preventDefault();
        clearErrors();
        disableSubmitButton(true);
        const formData = JSON.stringify(getFormData($(this)));
        addNewExercise(formData);
    });
});

function getFormData($form) {
    let unindexed_array = $form.serializeArray();
    let indexed_array = {};

    $.map(unindexed_array, function (n) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function addNewExercise(formData) {
    $.ajax({
        url: '/api/exercises/create',
        method: 'POST',
        data: formData,
        contentType: 'application/json',
        success: function (response) {
            $('#addNewExerciseModal').modal('hide');
            appendNewExercise(response);
            $('#addNewExerciseForm')[0].reset();
        },
        error: function (jqXHR) {
            disableSubmitButton(false);
            if (jqXHR.status === 400) {
                displayErrors(jqXHR.responseJSON);
            } else {
                alert('Error adding new exercise');
            }
        }
    });
}

function clearErrors() {
    $(`#nameError`).text('');
    $(`#typeError`).text('');
}

function displayErrors(errors) {
    clearErrors();
    for (const [field, message] of Object.entries(errors)) {
        $(`#${field}Error`).text(message);
    }
}

function appendNewExercise(exercise) {
    const newOption = $('<option></option>')
        .val(exercise.name)
        .text(exercise.name);

    $('.exercise-select').each(function() {
        $(this).append(newOption.clone());
    });

    disableSubmitButton(false);
}

function disableSubmitButton(disable) {
    $('#addNewExerciseForm button[type="submit"]').prop('disabled', disable);
}
