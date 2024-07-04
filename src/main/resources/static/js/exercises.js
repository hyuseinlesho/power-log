$(document).ready(function() {
    toastrOptions();

    $('#addExerciseForm').submit(function(event) {
        event.preventDefault();
        const form = $(this);
        clearErrors();
        disableSubmitButton(true, form);
        const formData = getFormData(form);
        createExercise(formData, form);
    });

    $('#editExerciseForm').submit(function(event) {
        event.preventDefault();
        let exerciseId = $('#editExerciseModal').data('id');
        let isDelete = event.originalEvent.submitter.id === 'deleteButton';
        const form = $(this);

        if (isDelete) {
            deleteExercise(exerciseId, form);
        } else {
            saveChanges(exerciseId, form);
        }

        disableSubmitButton(true, form);
    });

    $('.list-group-item').on('click', function () {
        openEditModal($(this));
    });
});

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

function createExercise(formData, form) {
    $.ajax({
        url: '/api/exercises/create',
        method: 'POST',
        data: JSON.stringify(formData),
        contentType: 'application/json',
        success: function (response) {
            appendExercise(response, form);
            $('#addExerciseModal').modal('hide');
            form[0].reset();
        },
        error: function (xhr, status, error) {
            disableSubmitButton(false, form);
            if (xhr.status === 400) {
                displayErrors(xhr.responseJSON, 'add');
            } else {
                toastr.error('Exercise with the same name and type already exists.');
            }
        }
    });
}

function saveChanges(exerciseId, form) {
    let name = $('#editExerciseName').val();
    let type = $('#editExerciseType').val();

    $.ajax({
        url: `/api/exercises/${exerciseId}`,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            name: name,
            type: type
        }),
        success: function(response) {
            updateListItem(exerciseId, response);
            $('#editExerciseModal').modal('hide');
            disableSubmitButton(false, form);
        },
        error: function(xhr, status, error) {
            disableSubmitButton(false, form);
            if (xhr.status === 400) {
                displayErrors(xhr.responseJSON, 'edit');
            } else {
                toastr.error('Exercise with the same name and type already exists.');
            }
        }
    });
}

function deleteExercise(exerciseId, form) {
    $.ajax({
        url: `/api/exercises/${exerciseId}`,
        method: 'DELETE',
        success: function() {
            $(`a[data-id="${exerciseId}"]`).remove();
            $('#editExerciseModal').modal('hide');
            disableSubmitButton(false, form);
        },
        error: function() {
            disableSubmitButton(false, form);
            toastr.error('Error deleting exercise');
        }
    });
}

function updateListItem(exerciseId, exercise) {
    let item = $(`a[data-id="${exerciseId}"]`);

    item.data('name', exercise.name);
    item.find('span').text(exercise.name);
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

function appendExercise(exercise, form) {
    let newItem = $('<a></a>')
        .attr('data-id', exercise.id)
        .attr('data-name', exercise.name)
        .attr('data-type', exercise.type)
        .addClass('list-group-item list-group-item-action')
        .append('<div>'
            + '<strong>Name: </strong>'
            + '<span>' + exercise.name + '</span>'
            + '</div>');

    if (exercise.type === 'Strength') {
        $('#strengthExercisesListBody').append(newItem);
    } else if (exercise.type === 'Cardio') {
        $('#cardioExercisesListBody').append(newItem);
    }

    newItem.on('click', function () {
        openEditModal($(this));
    });

    disableSubmitButton(false, form);
}

function openEditModal(exerciseItem) {
    const exerciseId = exerciseItem.data('id');
    const name = exerciseItem.data('name');
    const type = exerciseItem.data('type');

    const modal = $('#editExerciseModal');

    modal.find('#editExerciseName').val(name);
    modal.find('#editExerciseType').val(type);

    modal.data('id', exerciseId);
    modal.modal('show');
}

function disableSubmitButton(disable, form) {
    form.find('button[type="submit"]').prop('disabled', disable);
}
