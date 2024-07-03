$(document).ready(function() {
    $('#addExerciseForm').submit(function(event) {
        event.preventDefault();
        clearErrors('add');
        disableSubmitButton(true);
        const formData = JSON.stringify(getFormData($(this)));
        createExercise(formData);
    });

    $('#editExerciseForm').submit(function(event) {
        event.preventDefault();
        let exerciseId = $('#editExerciseModal').data('id');
        let isDelete = event.originalEvent.submitter.id === 'deleteButton';

        if (isDelete) {
            deleteExercise(exerciseId);
        } else {
            saveChanges(exerciseId);
        }
    });

    $('.list-group-item').on('click', function () {
        openEditModal($(this));
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

function createExercise(formData) {
    $.ajax({
        url: '/api/exercises/create',
        method: 'POST',
        data: formData,
        contentType: 'application/json',
        success: function (response) {
            $('#addExerciseModal').modal('hide');
            appendExercise(response);
            $('#addExerciseForm')[0].reset();
        },
        error: function (jqXHR) {
            disableSubmitButton(false);
            if (jqXHR.status === 400) {
                displayErrors(jqXHR.responseJSON, 'add');
            } else {
                alert('Error adding exercise');
            }
        }
    });
}

function saveChanges(exerciseId) {
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
            $('#editExerciseModal').modal('hide');
            updateListItem(exerciseId, response);
        },
        error: function(jqXHR) {
            if (jqXHR.status === 400) {
                displayErrors(jqXHR.responseJSON, 'edit');
            } else {
                alert('Error updating exercise');
            }
        }
    });
}

function deleteExercise(exerciseId) {
    $.ajax({
        url: `/api/exercises/${exerciseId}`,
        method: 'DELETE',
        success: function() {
            $(`a[data-id="${exerciseId}"]`).remove();
            $('#editExerciseModal').modal('hide');
        },
        error: function() {
            alert('Error deleting exercise');
        }
    });
}

function updateListItem(exerciseId, exercise) {
    let item = $(`a[data-id="${exerciseId}"]`);
    item.data('name', exercise.name);

    item.find('span').text(exercise.name);
}

function clearErrors(prefix) {
    $(`#${prefix}NameError`).text('');
    $(`#${prefix}TypeError`).text('');
}

function displayErrors(errors, prefix) {
    clearErrors(prefix);
    for (const [field, message] of Object.entries(errors)) {
        $(`#${prefix}${capitalizeFirstLetter(field)}Error`).text(message);
    }
}

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function appendExercise(exercise) {
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

    disableSubmitButton(false);
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

function disableSubmitButton(disable) {
    $('#addExerciseForm button[type="submit"]').prop('disabled', disable);
}
