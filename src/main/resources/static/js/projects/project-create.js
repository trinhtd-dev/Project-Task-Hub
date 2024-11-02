document.addEventListener('DOMContentLoaded', function() {
    // Member selection modal
    const memberSelectionBtn = document.getElementById('memberSelectionBtn');
    const memberModalElement = document.getElementById('memberModal');
    const saveMembersBtn = document.getElementById('saveMembersBtn');

    if (memberSelectionBtn && memberModalElement && saveMembersBtn) {
        let memberModal;

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
    let tasks = [];

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

        // Add event listeners for edit and delete buttons
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
        
        document.querySelectorAll('.task-assignee').forEach(checkbox => {
            checkbox.checked = false;
        });
        
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

    // Form submission
    const form = document.getElementById('projectForm');
    if (form) {
        form.addEventListener('submit', function(event) {
            event.preventDefault();

            if (!form.checkValidity()) {
                event.stopPropagation();
                form.classList.add('was-validated');
                return;
            }

            // Add member data to form
            const selectedMembers = document.querySelectorAll('#memberModal input[type="checkbox"]:checked');
            selectedMembers.forEach(function(member) {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'memberIds';
                input.value = member.value;
                form.appendChild(input);
            });

            // Add task data to form
            tasks.forEach(function(task, index) {
                const taskNameInput = document.createElement('input');
                taskNameInput.type = 'hidden';
                taskNameInput.name = `tasks[${index}].name`;
                taskNameInput.value = task.name;
                form.appendChild(taskNameInput);

                const taskDescInput = document.createElement('input');
                taskDescInput.type = 'hidden';
                taskDescInput.name = `tasks[${index}].description`;
                taskDescInput.value = task.description;
                form.appendChild(taskDescInput);

                const taskDueDateInput = document.createElement('input');
                taskDueDateInput.type = 'hidden';
                taskDueDateInput.name = `tasks[${index}].dueDate`;
                taskDueDateInput.value = task.dueDate;
                form.appendChild(taskDueDateInput);

                task.assignees.forEach(function(assignee, assigneeIndex) {
                    const assigneeInput = document.createElement('input');
                    assigneeInput.type = 'hidden';
                    assigneeInput.name = `tasks[${index}].assigneeIds[${assigneeIndex}]`;
                    assigneeInput.value = assignee.id;
                    form.appendChild(assigneeInput);
                });
            });

            form.submit();
        });
    }
});

