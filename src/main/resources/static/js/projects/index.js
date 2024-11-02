document.addEventListener('DOMContentLoaded', function() {
    // Delete project confirmation
    window.confirmDelete = function(projectId) {
        if (confirm('Bạn có chắc chắn muốn xóa dự án này không?')) {
            window.location.href = '/projects/' + projectId + '/delete';
        }
    }

    // Add event listeners for all delete buttons
    document.querySelectorAll('[data-project-id]').forEach(function(element) {
        element.addEventListener('click', function(event) {
            event.preventDefault();
            confirmDelete(this.getAttribute('data-project-id'));
        });
    });
});

