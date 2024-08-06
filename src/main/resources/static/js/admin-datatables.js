$(document).ready(function() {
    $('#datatablesSimpleUsers').DataTable({
        ajax: {
            url: '/api/admin/users',
            dataSrc: ''
        },
        columns: [
            { data: 'username' },
            { data: 'email' },
            {
                data: 'createdAt',
                render: function(data) {
                    return formatDate(data);
                }
            },
            {
                data: 'updatedAt',
                render: function(data) {
                    return formatDate(data);
                }
            }
        ]
    });

    $('#datatablesSimpleContacts').DataTable({
        ajax: {
            url: '/api/admin/contacts',
            dataSrc: ''
        },
        columns: [
            { data: 'name' },
            { data: 'email' },
            { data: 'message' },
            {
                data: 'createdAt',
                render: function(data) {
                    return formatDate(data);
                }
            }
        ]
    });

    function formatDate(dateString) {
        var date = new Date(dateString);

        var year = date.getFullYear();
        var month = String(date.getMonth() + 1).padStart(2, '0');
        var day = String(date.getDate()).padStart(2, '0');
        var hours = String(date.getHours()).padStart(2, '0');
        var minutes = String(date.getMinutes()).padStart(2, '0');
        var seconds = String(date.getSeconds()).padStart(2, '0');

        // Format to YYYY-MM-DD HH:MM:SS
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }
});
