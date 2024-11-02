document.addEventListener("DOMContentLoaded", function() {
    const projectId = document.querySelector('[data-project-id]').getAttribute('data-project-id');
    console.log(projectId);

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

});
