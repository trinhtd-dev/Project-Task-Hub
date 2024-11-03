document.addEventListener("DOMContentLoaded", function() {
    const projectId = document.querySelector('[data-project-id]').getAttribute('data-project-id');

    // Countdown timer
    function updateCountdown() {
        const dueDate = new Date(document.getElementById('projectDueDate').value).getTime();
        const now = new Date().getTime();
        const distance = dueDate - now;

        const days = Math.floor(distance / (1000 * 60 * 60 * 24));
        const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));

        document.getElementById("days").textContent = days;
        document.getElementById("hours").textContent = hours;
        document.getElementById("minutes").textContent = minutes;
    }

    if (document.getElementById('projectDueDate')) {
        updateCountdown();
        setInterval(updateCountdown, 1000 * 60); // Cập nhật mỗi phút
    }

    // Delete project confirmation
    window.confirmDelete = function(projectId) {
        if (confirm('Bạn có chắc chắn muốn xóa dự án này không?')) {
            window.location.href = '/projects/' + projectId + '/delete';
        }
    }


    // Add task to project
    document.getElementById('saveTaskBtn').addEventListener('click', function() {
        const taskForm = document.getElementById('addTaskForm');
        const assigneeIds = Array.from(taskForm.querySelectorAll('input[name="assigneeIds"]:checked'))
            .map(input => input.value);
        
        // Format date to YYYY-MM-DD
        const dueDateInput = taskForm.elements.dueDate.value;
        const formattedDate = dueDateInput ? dueDateInput : null;
        
        const taskData = {
            name: taskForm.elements.name.value,
            description: taskForm.elements.description.value,
            dueDate: formattedDate,  // This will be in YYYY-MM-DD format
            assigneeIds: assigneeIds
        }
        
        // Call API to add task
        fetch(`/api/projects/${projectId}/add-task`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(taskData)
        })
        .then(response => response.json())
        .then(data => {
            // Reload page or update UI
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi tạo task');
        });
    });

    // Edit task
    const editTaskBtns = document.querySelectorAll('.task-btn-edit');
    editTaskBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const taskId = btn.getAttribute('data-task-id');
            const modal = new bootstrap.Modal(document.getElementById('editTaskModal'));
            const editForm = document.getElementById('editTaskForm');

            fetch(`/api/tasks/${taskId}`)
                .then(response => response.json())
                .then(data => {
                    editForm.elements.name.value = data.name;
                    editForm.elements.description.value = data.description;
                    editForm.elements.dueDate.value = data.dueDate;
                    editForm.elements.status.value = data.status;

                    data.assignees.forEach(assignee => {
                        const checkbox = editForm.querySelector(`input[name="assigneeIds"][value="${assignee.id}"]`);
                        if (checkbox) {
                            checkbox.checked = true;
                        }
                    });
                    modal.show();
                });
            const updateTaskBtn = document.querySelector('#updateTaskBtn');
            updateTaskBtn.addEventListener('click', function() {
                const assigneeIds = Array.from(editForm.querySelectorAll('input[name="assigneeIds"]:checked'))
                    .map(input => input.value);
                const taskData = {
                    name: editForm.elements.name.value,
                    description: editForm.elements.description.value,
                    dueDate: editForm.elements.dueDate.value,
                    status: editForm.elements.status.value,
                    assigneeIds: assigneeIds
                }

                fetch(`/api/tasks/${taskId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(taskData)
                })
                .then(response => response.json())
                .then(data => {
                    window.location.reload();
                });
            });
        });
    });



    // Delete task
    const deleteTaskBtns = document.querySelectorAll('.task-btn-delete');
    deleteTaskBtns.forEach(btn => {
        btn.addEventListener('click', async function() {
            const taskId = btn.getAttribute('data-task-id');
            const confirmed = await confirmDeleteModal('công việc');
            
            if (confirmed) {
                try {
                    const response = await fetch(`/api/tasks/${taskId}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });
                    const divTask = document.querySelector(`[divTask="${taskId}"]`);
                    divTask.remove();
                } catch (error) {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi xóa công việc');
                }
            }
        });
    });

    // Delete announcement
    const deleteAnnouncementBtns = document.querySelectorAll('.announcement-btn-delete');
    deleteAnnouncementBtns.forEach(btn => {
        btn.addEventListener('click', async function() {
            const announcementId = btn.getAttribute('data-announcement-id');
            const confirmed = await confirmDeleteModal('thông báo');
            if(confirmed) {
                try {
                    const response = await fetch(`/api/announcements/${announcementId}`, {
                        method: 'DELETE',
                    });
                    const announcementDiv = document.querySelector(`[divAnnouncement="${announcementId}"]`);
                    announcementDiv.remove();
                } catch (error) {
                    console.error('Error:', error);
                    alert('Có lỗi xảy ra khi xóa thông báo');
                }
            }
        });
    });
});
