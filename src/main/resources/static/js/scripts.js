/*!
* Start Bootstrap - Modern Business v5.0.7 (https://startbootstrap.com/template-overviews/modern-business)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-modern-business/blob/master/LICENSE)
*/

document.addEventListener('DOMContentLoaded', function() {
    flatpickr("#dateTime", {
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        time_24hr: true
    });
});