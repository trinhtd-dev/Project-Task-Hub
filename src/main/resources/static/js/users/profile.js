document.addEventListener('DOMContentLoaded', function() {
    // Get CSRF token
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
    const userId = document.getElementById('data-user-id').getAttribute('data-user-id');
    // Form references
    const editProfileForm = document.getElementById('editProfileForm');
    const changePasswordForm = document.getElementById('changePasswordForm');
    const avatarForm = document.getElementById('avatarForm');

    // Edit Profile Form Handler
    if (editProfileForm) {
        editProfileForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            
            if (!this.checkValidity()) {
                e.stopPropagation();
                this.classList.add('was-validated');
                return;
            }

            const formData = new FormData(this);
            const data = Object.fromEntries(formData.entries());
            try {
                const response = await fetch(`/api/users/${userId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                        [header]: token
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    toast.show('success', 'Cập nhật thông tin thành công');
                    bootstrap.Modal.getInstance(document.getElementById('editProfileModal')).hide();

                    // Update user data
                    const user = await response.json();
                    document.getElementById('fullName-title').textContent = user.fullName;
                    document.getElementById('email-title').textContent = user.email;
                    document.getElementById('phoneNumber').textContent = user.phoneNumber;
                } else {
                    const error = await response.json();
                    toast.show('error', error.message || 'Có lỗi xảy ra khi cập nhật thông tin');
                }
            } catch (error) {
                toast.show('error', 'Có lỗi xảy ra khi cập nhật thông tin');
            }
        });
    }
});
