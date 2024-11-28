document.addEventListener('DOMContentLoaded', function() {
    // CSRF token
    const token = document.querySelector("meta[name='_csrf']")?.content;
    const header = document.querySelector("meta[name='_csrf_header']")?.content;


   const deleteButtons = document.querySelectorAll('[delete-button]');
   deleteButtons.forEach(button => {
      button.addEventListener('click', async function(event) {
        const confirm = await confirmDeleteModal("dự án");
        if (confirm) {
            const projectId = this.getAttribute('delete-button');
            const response = await fetch(`/projects/${projectId}/delete`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    [header]: token
                },
            });
            
            if (response.ok) {
                const divProject = document.querySelector(`[div-project="${projectId}"]`);
                toast.show("success", "Dự án đã được chuyển đến thùng rác");
                divProject.remove(); // Remove the project card from the UI
            } else {
                // Handle error case
                console.error('Failed to delete project');
            }
        }
    });
   });
});

