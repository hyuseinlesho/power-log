$(document).ready(function() {
    $('#addWeightForm').submit(function(event) {
        event.preventDefault();
        console.log("Form submitted");
        clearErrors();
        const formData = JSON.stringify(getFormData($(this)));
        createWeightLog(formData);
    });
});

function clearErrors() {
    $('#weightError').text('');
    $('#dateError').text('');
    $('#timeError').text('');
    $('#commentError').text('');
}

function getFormData($form) {
    let unindexed_array = $form.serializeArray();
    let indexed_array = {};

    $.map(unindexed_array, function (n) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function createWeightLog(formData) {
    $.ajax({
        url: '/api/weight-logs/create',
        method: 'POST',
        data: formData,
        contentType: 'application/json',
        success: function () {
            $('#addWeightModal').modal('hide');
            alert('Weight added successfully');
        },
        error: function (jqXHR) {
            if (jqXHR.status === 400) {
                let errors = jqXHR.responseJSON;
                displayErrors(errors);
            } else {
                alert('Error adding weight');
            }
        }
    });
}

function displayErrors(errors) {
    if (errors.weight) {
        $('#weightError').text(errors.weight);
    }
    if (errors.date) {
        $('#dateError').text(errors.date);
    }
    if (errors.time) {
        $('#timeError').text(errors.time);
    }
    if (errors.comment) {
        $('#commentError').text(errors.comment);
    }
}
