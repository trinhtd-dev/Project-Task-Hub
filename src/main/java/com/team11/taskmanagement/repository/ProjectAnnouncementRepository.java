package com.team11.taskmanagement.repository;

import com.team11.taskmanagement.model.ProjectAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectAnnouncementRepository extends JpaRepository<ProjectAnnouncement, Long> {
    // Bạn có thể thêm các phương thức truy vấn tùy chỉnh ở đây nếu cần
}