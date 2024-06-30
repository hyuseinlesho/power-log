document.addEventListener('DOMContentLoaded', function() {
    flatpickr("#date", {
        dateFormat: "Y-m-d"
    });
    flatpickr("#time", {
        enableTime: true,
        noCalendar: true,
        dateFormat: "H:i",
        time_24hr: true
    });
});

document.addEventListener('DOMContentLoaded', function() {
    flatpickr("#weightDate", {
        dateFormat: "Y-m-d"
    });
    flatpickr("#weightTime", {
        enableTime: true,
        noCalendar: true,
        dateFormat: "H:i",
        time_24hr: true
    });
});