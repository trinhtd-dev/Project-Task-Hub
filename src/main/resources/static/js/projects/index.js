document.addEventListener('DOMContentLoaded', function() {
   const deleteButtons = document.querySelectorAll('[delete-button]');
   deleteButtons.forEach(button => {
      button.addEventListener('click', async function(event) {
        const confirm = await confirmDeleteModal("dự án");
        if (confirm) {
            const projectId = this.getAttribute('delete-button');
            const response = await fetch(`/api/projects/${projectId}/delete`, {
                method: 'DELETE',
            });
            
            if (response.ok) {
                const divProject = document.querySelector(`[div-project="${projectId}"]`);
                divProject.remove(); // Remove the project card from the UI
            } else {
                // Handle error case
                console.error('Failed to delete project');
            }
        }
    });
   });
});

