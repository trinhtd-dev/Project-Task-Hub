document.addEventListener("DOMContentLoaded", function() {
    
    // CSRF token
    const token = document.querySelector("meta[name='_csrf']")?.content;
    const header = document.querySelector("meta[name='_csrf_header']")?.content;

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
            startDate: taskForm.elements.startDate.value,
            dueDate: formattedDate,  // This will be in YYYY-MM-DD format
            assigneeIds: assigneeIds
        }
        
        // Call API to add task
        fetch(`/api/projects/${projectId}/add-task`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [header]: token
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

            fetch(`/api/tasks/${taskId}`, {
                headers: {
                    [header]: token
                }
            })
                .then(response => response.json())
                .then(data => {
                    editForm.elements.name.value = data.name;
                    editForm.elements.description.value = data.description;
                    editForm.elements.startDate.value = data.startDate;
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
                    startDate: editForm.elements.startDate.value,
                    dueDate: editForm.elements.dueDate.value,
                    status: editForm.elements.status.value,
                    assigneeIds: assigneeIds
                }

                fetch(`/api/tasks/${taskId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                        [header]: token
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

    // Delete project
    const deleteProjectBtn = document.querySelector('[delete-project-button]');
    deleteProjectBtn.addEventListener('click', async function() {
        const confirmed = await confirmDeleteModal('dự án');
        if (confirmed) {
            try {
                const response = await fetch(`/projects/${projectId}/delete`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                        [header]: token
                    }
                });
                window.location.href = '/projects';
                toast.show('success', 'Dự án đã được chuyển đến thùng rác');
            } catch (error) {
                console.error('Error:', error);
                toast.show('error', 'Có lỗi xảy ra khi xóa dự án');
            }
        }
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
                            'Content-Type': 'application/json',
                            [header]: token
                        }
                    });
                    const divTask = document.querySelector(`[divTask="${taskId}"]`);
                    divTask.remove();
                    toast.show('success', 'Công việc đã được xóa thành công');
                } catch (error) {
                    console.error('Error:', error);
                    toast.show('error', 'Có lỗi xảy ra khi xóa công việc');
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
                        headers: {
                            [header]: token
                        }
                    });
                    const announcementDiv = document.querySelector(`[divAnnouncement="${announcementId}"]`);
                    announcementDiv.remove();
                    toast.show('success', 'Thông báo đã được xóa thành công');
                } catch (error) {
                    console.error('Error:', error);
                    toast.show('error', 'Có lỗi xảy ra khi xóa thông báo');
                }
            }
        });
    });


    // Show attachments
    const documentProjectBtn = document.querySelector('[document-project-button]');
    const projectDocumentsModal = document.getElementById('projectDocumentsModal');
    
    documentProjectBtn.addEventListener('click', async () => {
        const modal = new bootstrap.Modal(projectDocumentsModal);
        modal.show();
        
        // Xử lý khi modal được đóng
        projectDocumentsModal.addEventListener('hidden.bs.modal', function () {
            document.body.classList.remove('modal-open');
            const backdrop = document.querySelector('.modal-backdrop');
            if (backdrop) {
                backdrop.remove();
            }
        });

        const response = await fetch(`/api/attachments/project/${projectId}`, {
            headers: {
                [header]: token
            }
        });

        const attachments = await response.json();
        const documentsList = document.querySelector('.documents-list');
        
        // Clear existing list
        documentsList.innerHTML = '';
        
        // Add attachments to list
        attachments.forEach(attachment => {
            const fileSize = (attachment.fileSize / 1024).toFixed(2); // Convert to KB
            const uploadDate = new Date(attachment.uploadedAt).toLocaleString();
            
            const docHtml = `
                <div class="document-item p-3 border-bottom" data-attachment-id="${attachment.id}">
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="d-flex align-items-center">
                            <i class="fas fa-file fa-lg text-info me-3"></i>
                            <div>
                                <h6 class="mb-0">${attachment.originalFileName}</h6>
                                <small class="text-muted">
                                    ${fileSize} KB • Uploaded by ${attachment.uploadedBy.fullName} • ${uploadDate}
                                </small>
                            </div>
                        </div>
                        <div class="btn-group">
                            <a href="${attachment.filePath}" 
                               class="btn btn-sm btn-outline-primary" 
                               target="_blank"
                               download="${attachment.originalFileName}">
                                <i class="fas fa-download"></i>
                            </a>
                            <button class="btn btn-sm btn-outline-danger delete-attachment" 
                                    data-attachment-id="${attachment.id}">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    </div>
                </div>
            `;
            documentsList.insertAdjacentHTML('beforeend', docHtml);
        });

        // Delete attachment
        const deleteAttachmentBtns = document.querySelectorAll('.delete-attachment');
        deleteAttachmentBtns.forEach(btn => {
            btn.addEventListener('click', async () => {
                const attachmentId = btn.getAttribute('data-attachment-id');
                const confirmed = await confirmDeleteModal('tập tin');
                if (confirmed) {
                    try {
                        const response = await fetch(`/api/attachments/${attachmentId}`, {
                            method: 'DELETE',
                            headers: {
                                [header]: token
                            }
                        });
                        const attachmentDiv = document.querySelector(`[data-attachment-id="${attachmentId}"]`);
                        attachmentDiv.remove();
                        toast.show('success', 'Tập tin đã được xóa thành công');
                    } catch (error) {
                        console.error('Error:', error);
                        toast.show('error', 'Có lỗi xảy ra khi xóa tập tin');
                    }
                }
            });
        });
    });


    // Upload file 
    const uploadForm = document.getElementById('uploadForm');
    uploadForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const fileInput = document.getElementById('fileUpload');
        const file = fileInput.files[0];
        
        if (!file) {
            toast.show('warning', 'Vui lòng chọn tập tin');
            return;
        }
        
        const formData = new FormData();
        formData.append('file', file);
        
        // Get project ID and CSRF tokens
        const projectId = document.querySelector('[data-project-id]').getAttribute('data-project-id');
        const token = document.querySelector("meta[name='_csrf']")?.content;
        const header = document.querySelector("meta[name='_csrf_header']")?.content;
        
        try {
            const response = await fetch(`/api/attachments/upload/${projectId}`, {
                method: 'POST',
                body: formData,
                headers: {
                    [header]: token
                }
            });

            if (!response.ok) {
                throw new Error(`Tải lên thất bại: ${response.status}`);
            }
            
            const attachment = await response.json();
            
            // Add new file to the documents list
            const documentsList = document.querySelector('.documents-list');
            const newDoc = `
                <div class="document-item p-3 border-bottom" data-attachment-id="${attachment.id}">
                    <div class="d-flex justify-content-between align-items-center">
                        <div class="d-flex align-items-center">
                            <i class="fas fa-file fa-lg text-info me-3"></i>
                            <div>
                                <h6 class="mb-0">${attachment.originalFileName}</h6>
                                <small class="text-muted">${new Date().toLocaleString()}</small>
                            </div>
                        </div>
                        <div class="btn-group">
                            <a href="${attachment.filePath}" 
                               class="btn btn-sm btn-outline-primary" 
                               target="_blank"
                               download="${attachment.originalFileName}">
                                <i class="fas fa-download"></i>
                            </a>
                            <button class="btn btn-sm btn-outline-danger delete-attachment" 
                                    data-attachment-id="${attachment.id}">
                                <i class="fas fa-trash"></i>
                            </button>
                        </div>
                    </div>
                </div>
            `;
            documentsList.insertAdjacentHTML('afterbegin', newDoc);
            
            // Reset form
            fileInput.value = '';
            toast.show('success', 'Tải lên tập tin thành công');
            
        } catch (error) {
            console.error('Lỗi:', error);
            toast.show('error', 'Có lỗi xảy ra khi tải lên tập tin');
        }
    });
});
