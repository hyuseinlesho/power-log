$(document).ready(function() {
    flatpickr("#weightDate", {
        dateFormat: "Y-m-d"
    });
    flatpickr("#weightTime", {
        enableTime: true,
        noCalendar: true,
        dateFormat: "H:i",
        time_24hr: true
    });

    $('#addWeightForm').submit(function(event) {
        event.preventDefault();
        clearErrors();
        disableSubmitButton(true);
        const formData = JSON.stringify(getFormData($(this)));
        createWeightLog(formData);
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

function createWeightLog(formData) {
    $.ajax({
        url: '/api/weight-logs/create',
        method: 'POST',
        data: formData,
        contentType: 'application/json',
        success: function (response) {
            $('#addWeightModal').modal('hide');
            appendWeightLog(response);
        },
        error: function (jqXHR) {
            disableSubmitButton(false);
            if (jqXHR.status === 400) {
                displayErrors(jqXHR.responseJSON);
            } else {
                alert('Error adding weight');
            }
        }
    });
}

function clearErrors() {
    $('#weightError').text('');
    $('#dateError').text('');
    $('#timeError').text('');
    $('#commentError').text('');
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

function appendWeightLog(log) {
    let tbody = $('#weightLogsTableBody');
    tbody.append('<tr><td>' + log.weight + '</td>'
        + '<td>' + log.date + '</td>'
        + '<td>' + log.time + '</td>'
        + '<td>' + (log.comment || '') + '</td></tr>');
    disableSubmitButton(false);
}

function disableSubmitButton(disable) {
    $('#addWeightForm button[type="submit"]').prop('disabled', disable);
}
