document.addEventListener('DOMContentLoaded', function() {
    // Form and modal elements
    const form = document.getElementById('createUserForm');
    const modal = document.getElementById('createUserModal');
    const modalInstance = new bootstrap.Modal(modal);
    
    // Search and filter elements
    const searchInput = document.getElementById('userSearch');
    const roleFilter = document.getElementById('roleFilter');
    const statusFilter = document.getElementById('statusFilter');

    // Password toggle functionality
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');
    
    // Toggle password visibility
    togglePassword.addEventListener('click', function() {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);
        this.querySelector('i').classList.toggle('fa-eye');
        this.querySelector('i').classList.toggle('fa-eye-slash');
    });

    // Form validation and submission
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        if (!this.checkValidity()) {
            e.stopPropagation();
            this.classList.add('was-validated');
            return;
        }

        try {
            const formData = new FormData(this);
            
            const response = await fetch('/users/create', {
                method: 'POST',
                body: formData,
                headers: {
                    // 'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]').value
                }
            });

            if (!response.ok) {
                const result = await response.json();
                throw new Error(result.message || 'Có lỗi xảy ra khi tạo tài khoản');
            }

            // Show success message
            toast.show('success', 'Tạo tài khoản thành công!');
            
            // Close modal and reset form
            modalInstance.hide();
            this.reset();
            this.classList.remove('was-validated');
            
            // Reload user list
            window.location.reload();

        } catch (error) {
            toast.show('error', error.message);
        }
    });

    return;
    // Reset form when modal is closed
    modal.addEventListener('hidden.bs.modal', function() {
        form.reset();
        form.classList.remove('was-validated');
    });

    // Search functionality
    searchInput.addEventListener('input', filterUsers);
    roleFilter.addEventListener('change', filterUsers);
    statusFilter.addEventListener('change', filterUsers);

    function filterUsers() {
        const searchTerm = searchInput.value.toLowerCase();
        const roleValue = roleFilter.value;
        const statusValue = statusFilter.value;
        
        const userCards = document.querySelectorAll('.user-card');
        
        userCards.forEach(card => {
            const fullName = card.querySelector('.card-title').textContent.toLowerCase();
            const email = card.querySelector('.text-muted').textContent.toLowerCase();
            const role = card.querySelector('.badge.bg-primary').textContent;
            const status = card.querySelector('.badge:not(.bg-primary)').textContent;
            
            const matchesSearch = fullName.includes(searchTerm) || email.includes(searchTerm);
            const matchesRole = !roleValue || role === roleValue;
            const matchesStatus = !statusValue || status === statusValue;
            
            card.closest('.col').style.display = 
                matchesSearch && matchesRole && matchesStatus ? 'block' : 'none';
        });
    }

    // Delete user functionality
    document.querySelectorAll('[delete-user]').forEach(button => {
        button.addEventListener('click', async function(e) {
            e.preventDefault();
            const userId = this.getAttribute('delete-user');
            
            if (confirm('Bạn có chắc chắn muốn xóa tài khoản này?')) {
                try {
                    const response = await fetch(`/users/${userId}`, {
                        method: 'DELETE',
                        headers: {
                            'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]').value
                        }
                    });

                    if (!response.ok) {
                        const error = await response.json();
                        throw new Error(error.message || 'Có lỗi xảy ra khi xóa tài khoản');
                    }

                    toast.success('Xóa tài khoản thành công!');
                    
                    // Remove the user card from DOM
                    const userCard = document.querySelector(`[div-user="${userId}"]`);
                    if (userCard) {
                        userCard.remove();
                    }

                } catch (error) {
                    toast.error(error.message);
                }
            }
        });
    });

    // Reset password functionality
    document.querySelectorAll('[reset-password]').forEach(button => {
        button.addEventListener('click', async function(e) {
            e.preventDefault();
            const userId = this.getAttribute('reset-password');
            
            if (confirm('Bạn có chắc chắn muốn đặt lại mật khẩu cho tài khoản này?')) {
                try {
                    const response = await fetch(`/users/${userId}/reset-password`, {
                        method: 'POST',
                        headers: {
                            'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]').value
                        }
                    });

                    if (!response.ok) {
                        const error = await response.json();
                        throw new Error(error.message || 'Có lỗi xảy ra khi đặt lại mật khẩu');
                    }

                    const result = await response.json();
                    toast.success('Đặt lại mật khẩu thành công!');
                    
                    // Show new password in alert
                    alert(`Mật khẩu mới: ${result.newPassword}`);

                } catch (error) {
                    toast.error(error.message);
                }
            }
        });
    });
});