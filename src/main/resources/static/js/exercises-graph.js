(function($) {
    let exerciseChart = null;

    $(document).ready(function() {
        flatpickrInit();
        toastrOptions();
        setupEventHandlers();
    });

    function flatpickrInit() {
        flatpickr("#startDate", {
            dateFormat: "Y-m-d"
        });
        flatpickr("#endDate", {
            dateFormat: "Y-m-d"
        });
    }

    function toastrOptions() {
        toastr.options = {
            positionClass: 'toast-bottom-right'
        };
    }

    function setupEventHandlers() {
        $('#filterButton').on('click', function() {
            const exerciseName = $('#exerciseSelect').val();
            const startDate = $('#startDate').val();
            const endDate = $('#endDate').val();
            if (exerciseName && startDate && endDate) {
                fetchExerciseLogs(exerciseName, startDate, endDate);
            } else {
                toastr.warning('Please select an exercise and date range.');
            }
        });
    }

    function fetchExerciseLogs(exerciseName, startDate, endDate) {
        $.ajax({
            url: '/api/exercise-logs',
            method: 'GET',
            data: {
                exerciseName: exerciseName,
                startDate: startDate,
                endDate: endDate
            },
            success: function(response) {
                if (response.length > 0) {
                    const labels = response.map(log => `${log.date} ${log.time}`);
                    const data = response.map(log => log.weight);
                    renderChart(labels, data);
                } else {
                    toastr.info('No data available for the selected exercise and date range.');
                    if (exerciseChart) {
                        exerciseChart.destroy();
                    }
                    $('#exerciseChart').hide();
                }
            },
            error: function(xhr, status, error) {
                toastr.error('Error fetching exercise logs');
            }
        });
    }

    function renderChart(labels, data) {
        const ctx = document.getElementById('exerciseChart').getContext('2d');

        if (exerciseChart) {
            exerciseChart.destroy();
        }

        exerciseChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Weight',
                    data: data,
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                    fill: false
                }]
            },
            options: {
                scales: {
                    x: {
                        type: 'time',
                        time: {
                            unit: 'day'
                        },
                        title: {
                            display: true,
                            text: 'Date and Time'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Weight (kg)'
                        }
                    }
                }
            }
        });
    }
})(jQuery);
