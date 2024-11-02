document.addEventListener('DOMContentLoaded', function() {
    // Hàm dùng chung cho tìm kiếm user
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

    // Hàm format date dùng chung
    window.formatDate = function(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('vi-VN', { 
            year: 'numeric', 
            month: '2-digit', 
            day: '2-digit', 
        });
    }
});