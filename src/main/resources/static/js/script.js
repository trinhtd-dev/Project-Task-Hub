document.addEventListener('DOMContentLoaded', function() {

    // Member selection modal
    const memberSelectionBtn = document.getElementById('memberSelectionBtn');
    const memberModalElement = document.getElementById('memberModal');
    const saveMembersBtn = document.getElementById('saveMembersBtn');

    if (memberSelectionBtn && memberModalElement && saveMembersBtn) {
        let memberModal;

        // Khởi tạo modal khi nút được nhấn
        memberSelectionBtn.addEventListener('click', function() {
            if (!memberModal) {
                memberModal = new bootstrap.Modal(memberModalElement);
            }
            memberModal.show();
        });

        saveMembersBtn.addEventListener('click', function() {
            const selectedMembers = document.querySelectorAll('#memberModal input[type="checkbox"]:checked');
            const memberList = document.getElementById('selectedMembers');
            if (memberList) {
                memberList.innerHTML = '';
                selectedMembers.forEach(function(member) {
                    const li = document.createElement('li');
                    li.innerHTML = `
                        <img src="${member.nextElementSibling.querySelector('img').src}" alt="${member.nextElementSibling.querySelector('.fw-bold').textContent}" class="rounded-circle" style="width: 30px; height: 30px; margin-right: 5px;">
                        <span>${member.nextElementSibling.querySelector('.fw-bold').textContent}</span>
                    `;
                    memberList.appendChild(li);
                });
            }
            memberModal.hide();
        });
    }

    // Task management
    const addTaskBtn = document.getElementById('addTaskBtn');
    const taskModalElement = document.getElementById('taskModal');
    let taskModal;

    if (addTaskBtn && taskModalElement) {
        addTaskBtn.addEventListener('click', function() {
            if (!taskModal) {
                taskModal = new bootstrap.Modal(taskModalElement);
            }
            document.getElementById('taskForm').reset();
            taskModal.show();
        });
    }

    const saveTaskBtn = document.getElementById('saveTaskBtn');
    const taskList = document.getElementById('taskList');
    let tasks = [];

    if(saveTaskBtn) {
        saveTaskBtn.addEventListener('click', function() {
            const taskName = document.getElementById('taskName').value;
            const taskDescription = document.getElementById('taskDescription').value;
            const taskDueDate = document.getElementById('taskDueDate').value;
            const taskAssignees = document.querySelectorAll('.task-assignee:checked');
            const assignees = Array.from(taskAssignees).map(checkbox => ({
                id: checkbox.value,
                name: checkbox.nextElementSibling.textContent,
                avatarUrl: checkbox.nextElementSibling.querySelector('img').src
            }));

            if (!taskName) {
                alert('Vui lòng nhập tên task');
                return;
            }

            const task = {
                name: taskName,
                description: taskDescription,
                dueDate: taskDueDate,
                assignees: assignees
            };
        
            const editIndex = saveTaskBtn.getAttribute('data-edit-index');
            if (editIndex !== null) {
                tasks[editIndex] = task;
                saveTaskBtn.removeAttribute('data-edit-index');
            } else {
                tasks.push(task);
            }

            renderTaskList();
            taskModal.hide();
        });
    }

    function renderTaskList() {
        taskList.innerHTML = '';
        tasks.forEach((task, index) => {
            const li = document.createElement('div');
            li.className = 'task-item';
            li.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <span class="task-name">${task.name}</span>
                    <div class="task-actions">
                        <button class="btn btn-sm btn-outline-primary edit-task" data-index="${index}" title="Sửa">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger delete-task" data-index="${index}" title="Xóa">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </div>
                </div>
                <div class="task-info">
                    <div class="task-date">
                        <i class="far fa-calendar-alt me-1"></i>
                        <small>${formatDate(task.dueDate)}</small>
                    </div>
                    <div class="task-assignees mt-2">
                        ${task.assignees.map(assignee => `
                            <img src="${assignee.avatarUrl}" alt="${assignee.name}" title="${assignee.name}" class="task-assignee-avatar">
                        `).join('')}
                    </div>
                </div>
            `;
            taskList.appendChild(li);
        });

        // Thêm event listeners cho các nút edit và delete
        document.querySelectorAll('.edit-task').forEach(btn => {
            btn.addEventListener('click', function() {
                const index = this.getAttribute('data-index');
                editTask(index);
            });
        });

        document.querySelectorAll('.delete-task').forEach(btn => {
            btn.addEventListener('click', function() {
                const index = this.getAttribute('data-index');
                deleteTask(index);
            });
        });
    }

    function editTask(index) {
        const task = tasks[index];
        document.getElementById('taskName').value = task.name;
        document.getElementById('taskDescription').value = task.description;
        document.getElementById('taskDueDate').value = task.dueDate;
        
        // Uncheck all checkboxes first
        document.querySelectorAll('.task-assignee').forEach(checkbox => {
            checkbox.checked = false;
        });
        
        // Check the checkboxes for assigned users
        task.assignees.forEach(assignee => {
            const checkbox = document.querySelector(`.task-assignee[value="${assignee.id}"]`);
            if (checkbox) {
                checkbox.checked = true;
            }
        });

        saveTaskBtn.setAttribute('data-edit-index', index);
        taskModal.show();
    }

    function deleteTask(index) {
        if (confirm('Bạn có chắc chắn muốn xóa task này?')) {
            tasks.splice(index, 1);
            renderTaskList();
        }
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('vi-VN', { 
            year: 'numeric', 
            month: '2-digit', 
            day: '2-digit', 
        });
    }

    // Form submission
    const form = document.getElementById('projectForm');
    if (form){
        form.addEventListener('submit', function(event) {
            event.preventDefault();

            if (!form.checkValidity()) {
                event.stopPropagation();
                form.classList.add('was-validated');
                return;
            }

            const formData = new FormData(form);
            
            // Ly memberIds từ các checkbox đã chọn trong modal
            const selectedMembers = document.querySelectorAll('#memberModal input[type="checkbox"]:checked');
            const memberIds = Array.from(selectedMembers).map(checkbox => parseInt(checkbox.value, 10));

            const projectData = {
                name: formData.get('name').trim(),
                description: formData.get('description') ? formData.get('description').trim() : null,
                status: formData.get('status'),
                startDate: formData.get('startDate') || null,
                dueDate: formData.get('dueDate') || null,
                priority: formData.get('priority'),
                tag: formData.get('tag'),
                memberIds: memberIds, // Sử dụng memberIds đã lấy được
                tasks: tasks.map(task => ({
                    name: task.name,
                    description: task.description,
                    dueDate: task.dueDate || null,
                    assigneeIds: task.assignees.map(assignee => assignee.id)
                }))
            };

            // Kiểm tra dữ liệu trước khi gửi
            if (!projectData.name || !projectData.status || !projectData.priority || !projectData.tag) {
                alert('Vui lòng điền đầy đủ thông tin bắt buộc.');
                return;
            }

            if (projectData.startDate && projectData.dueDate && new Date(projectData.startDate) > new Date(projectData.dueDate)) {
                alert('Ngày bắt đầu không thể sau ngày kết thúc.');
                return;
            }

            // Send form data to server
            fetch(form.action, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(projectData)
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw err; });
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                // Redirect or show success message
                alert('Dự án đã được tạo thành công!');
                window.location.href = '/projects'; // Redirect to projects list
            })
            .catch(error => {
                console.error('Error:', error);
                if (error.errors) {
                    // Display validation errors
                    const errorMessages = error.errors.map(err => err.defaultMessage).join('\n');
                    alert('Lỗi xác thực:\n' + errorMessages);
                } else {
                    alert('Có lỗi xảy ra khi tạo dự án. Vui lòng thử lại.');
                }
            });
        });
    }
    const userSearch = document.getElementById('userSearch');
    const userItems = document.querySelectorAll('.user-item');
    if(userSearch && userItems){
        userSearch.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            userItems.forEach(item => {
                const userName = item.querySelector('.fw-bold').textContent.toLowerCase();
                const userEmail = item.querySelector('.text-muted').textContent.toLowerCase();
                if (userName.includes(searchTerm) || userEmail.includes(searchTerm)) {
                    item.style.display = '';
                } else {
                    item.style.display = 'none';
                }
            });
        });
    }

    function confirmDelete(projectId) {
        if (confirm('Bạn có chắc chắn muốn xóa dự án này không?')) {
            window.location.href = '/projects/' + projectId + '/delete';
        }
    }

    // Thêm event listener cho tất cả các nút xóa
    document.querySelectorAll('[data-project-id]').forEach(function(element) {
        element.addEventListener('click', function(event) {
            event.preventDefault();
            confirmDelete(this.getAttribute('data-project-id'));
        });
    });
});
