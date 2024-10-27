package com.team11.taskmanagement.repository;

import com.team11.taskmanagement.model.Task;
import com.team11.taskmanagement.model.Project;
import com.team11.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectAndAssignee(Project project, User assignee);
}
