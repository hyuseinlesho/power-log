$(document).ready(function() {
    $('#datatablesSimple').DataTable({
        ajax: {
            url: '/api/admin/users',
            dataSrc: ''
        },
        columns: [
            { data: 'username' },
            { data: 'email' },
            { data: 'createdAt' },
            { data: 'updatedAt' }
        ]
    });
});