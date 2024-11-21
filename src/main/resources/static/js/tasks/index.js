document.addEventListener('DOMContentLoaded', function() {
    // Handle status updates
    const dropdownItems = document.querySelectorAll('.status-dropdown .dropdown-item');
    
    dropdownItems.forEach(item => {
        item.addEventListener('click', function(e) {
            e.preventDefault();
            const status = this.dataset.status;
            const taskId = this.closest('.status-dropdown')
                              .querySelector('.dropdown-toggle')
                              .dataset.taskId;
            
            updateTaskStatus(taskId, status);
        });
    });
    
    function updateTaskStatus(taskId, status) {
        console.log(taskId, status);
        fetch(`/api/tasks/${taskId}/status`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: status })
        })
        .then(response => response.json())
        .then(data => {
            // Update UI without page reload
            const statusBadge = document.querySelector(`[data-task-id="${taskId}"]`);
            const statusText = statusBadge.querySelector('span');
            
            // Remove existing status classes
            statusBadge.classList.remove('status-todo', 'status-progress', 'status-review', 'status-done');
            
            // Add new status class and update text
            const statusMap = {
                'TODO': { class: 'status-todo', text: 'Chưa bắt đầu' },
                'IN_PROGRESS': { class: 'status-progress', text: 'Đang thực hiện' },
                'REVIEW': { class: 'status-review', text: 'Đang xem xét' },
                'DONE': { class: 'status-done', text: 'Hoàn thành' }
            };
            
            statusBadge.classList.add(statusMap[status].class);
            statusText.textContent = statusMap[status].text;
            
            // Update updatedAt
            const updatedTime = document.querySelector(`[divTask="${taskId}"]`).querySelector('.update-time');
            updatedTime.textContent = formatDate(data.updatedAt);
            
            // Show success toast or notification
            toast.show('success', 'Cập nhật trạng thái thành công');
        })
        .catch(error => {
            console.error('Error:', error);
            toast.show('error', 'Có lỗi xảy ra khi cập nhật trạng thái');
        });
    }
});
