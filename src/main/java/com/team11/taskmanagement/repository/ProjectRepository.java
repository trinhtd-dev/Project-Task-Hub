package com.team11.taskmanagement.repository;

import com.team11.taskmanagement.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
}

// Các phương thức mặc định được cung cấp bởi JpaRepository:

    // Lưu một đối tượng Project vào cơ sở dữ liệu
    // <S extends Project> S save(S entity)

    // Lưu một danh sách các đối tượng Project vào cơ sở dữ liệu
    // <S extends Project> List<S> saveAll(Iterable<S> entities)

    // Tìm một Project theo ID
    // Optional<Project> findById(Long id)

    // Kiểm tra xem một Project có tồn tại theo ID không
    // boolean existsById(Long id)

    // Lấy tất cả các Project
    // List<Project> findAll()

    // Lấy tất cả các Project theo danh sách ID
    // List<Project> findAllById(Iterable<Long> ids)

    // Đếm số lượng Project
    // long count()

    // Xóa một Project theo ID
    // void deleteById(Long id)

    // Xóa một đối tượng Project
    // void delete(Project entity)

    // Xóa nhiều Project theo danh sách đối tượng
    // void deleteAll(Iterable<? extends Project> entities)

    // Xóa tất cả Project
    // void deleteAll()

    // Lấy tất cả Project với phân trang và sắp xếp
    // Page<Project> findAll(Pageable pageable)

    // Lấy tất cả Project với sắp xếp
    // List<Project> findAll(Sort sort)