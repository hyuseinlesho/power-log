/*!
* Start Bootstrap - Modern Business v5.0.7 (https://startbootstrap.com/template-overviews/modern-business)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-modern-business/blob/master/LICENSE)
*/
function addExercise() {
    const exercisesDiv = document.getElementById('exercises');
    const exerciseCount = exercisesDiv.getElementsByClassName('exercise').length;
    const exerciseTemplate = document.getElementById('exerciseTemplate').innerHTML;
    const newExerciseHtml = exerciseTemplate.replace(/__INDEX__/g, exerciseCount);
    exercisesDiv.insertAdjacentHTML('beforeend', newExerciseHtml);
}