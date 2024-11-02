package com.team11.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.team11.taskmanagement.model.Project;
import com.team11.taskmanagement.model.Task;
import com.team11.taskmanagement.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectAndAssigneesContaining(Project project, User user);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate < CURRENT_DATE AND t.status != 'COMPLETED'")
    List<Task> findOverdueTasks();
}
