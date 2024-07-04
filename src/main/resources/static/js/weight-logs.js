$(document).ready(function() {
    flatpickrInit();
    toastrOptions();

    $('#addWeightLogForm').submit(function(event) {
        event.preventDefault();
        const form = $(this);
        clearErrors();
        disableSubmitButton(true, form);
        const formData = getFormData(form);
        createWeightLog(formData, form);
    });

    $('#editWeightLogForm').submit(function(event) {
        event.preventDefault();
        let logId = $('#editWeightLogModal').data('id');
        let isDelete = event.originalEvent.submitter.id === 'deleteButton';
        const form = $(this);

        if (isDelete) {
            deleteWeightLog(logId, form);
        } else {
            saveChanges(logId, form);
        }
    });

    $('.weight-log-row').on('click', function () {
        openEditModal($(this));
    });
});

function flatpickrInit() {
    flatpickr("#addWeightDate", {
        dateFormat: "Y-m-d"
    });
    flatpickr("#addWeightTime", {
        enableTime: true,
        noCalendar: true,
        dateFormat: "H:i",
        time_24hr: true
    });

    flatpickr("#editWeightDate", {
        dateFormat: "Y-m-d"
    });
    flatpickr("#editWeightTime", {
        enableTime: true,
        noCalendar: true,
        dateFormat: "H:i",
        time_24hr: true
    });
}

function toastrOptions() {
    toastr.options = {
        positionClass: 'toast-bottom-right'
    };
}

function getFormData($form) {
    let unindexed_array = $form.serializeArray();
    let indexed_array = {};

    $.map(unindexed_array, function(n) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function createWeightLog(formData, form) {
    $.ajax({
        url: '/api/weight-logs/create',
        method: 'POST',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        success: function (response) {
            appendWeightLog(response, form);
            $('#addWeightLogModal').modal('hide');
            form[0].reset();
        },
        error: function (xhr, status, error) {
            disableSubmitButton(false, form);
            if (xhr.status === 400) {
                displayErrors(xhr.responseJSON, 'add');
            } else {
                toastr.error('Error creating weight log');
            }
        }
    });
}

function saveChanges(logId, form) {
    let weight = $('#editWeightValue').val();
    let date = $('#editWeightDate').val();
    let time = $('#editWeightTime').val();
    let comment = $('#editWeightComment').val();

    $.ajax({
        url: `/api/weight-logs/${logId}`,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            weight: weight,
            date: date,
            time: time,
            comment: comment
        }),
        success: function(response) {
            updateTableRow(logId, response);
            $('#editWeightLogModal').modal('hide');
            disableSubmitButton(false, form);
        },
        error: function(xhr, status, error) {
            disableSubmitButton(false, form);
            if (xhr.status === 400) {
                displayErrors(xhr.responseJSON, 'edit');
            } else {
                toastr.error('Error updating weight log');
            }
        }
    });
}

function deleteWeightLog(logId, form) {
    $.ajax({
        url: `/api/weight-logs/${logId}`,
        method: 'DELETE',
        success: function() {
            $(`tr[data-id="${logId}"]`).remove();
            $('#editWeightLogModal').modal('hide');
            disableSubmitButton(false, form);
        },
        error: function() {
            disableSubmitButton(false, form);
            toastr.error('Error deleting weight log');
        }
    });
}

function updateTableRow(logId, log) {
    let row = $(`tr[data-id="${logId}"]`);

    row.data('weight', log.weight);
    row.data('date', log.date);
    row.data('time', log.time);
    row.data('comment', log.comment);

    row.find('td').eq(0).text(log.weight);
    row.find('td').eq(1).text(log.date);
    row.find('td').eq(2).text(log.time);
    row.find('td').eq(3).text(log.comment || '');
}

function clearErrors() {
    $('.text-danger').text('');
}

function displayErrors(errors, prefix) {
    clearErrors();
    for (const [field, message] of Object.entries(errors)) {
        $(`#${prefix}${capitalizeFirstLetter(field)}Error`).text(message);
    }
}

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function appendWeightLog(log, form) {
    let newRow = $('<tr></tr>')
        .attr('data-id', log.id)
        .attr('data-weight', log.weight)
        .attr('data-date', log.date)
        .attr('data-time', log.time)
        .attr('data-comment', log.comment)
        .addClass('weight-log-row')
        .append('<td>' + log.weight + '</td>')
        .append('<td>' + log.date + '</td>')
        .append('<td>' + log.time + '</td>')
        .append('<td>' + (log.comment || '') + '</td>');
    $('#weightLogsTableBody').append(newRow);

    newRow.on('click', function () {
        openEditModal($(this));
    });

    disableSubmitButton(false, form);
}

function openEditModal(logRow) {
    const logId = logRow.data('id');
    const weight = logRow.data('weight');
    const date = logRow.data('date');
    const time = logRow.data('time');
    const comment = logRow.data('comment');

    const modal = $('#editWeightLogModal');

    modal.find('#editWeightValue').val(weight);
    modal.find('#editWeightDate').val(date);
    modal.find('#editWeightTime').val(time);
    modal.find('#editWeightComment').val(comment);

    modal.data('id', logId);
    modal.modal('show');
}

function disableSubmitButton(disable, form) {
    form.find('button[type="submit"]').prop('disabled', disable);
}
