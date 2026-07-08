package com.yusufmercan.taskmanager.repository;

import com.yusufmercan.taskmanager.entity.Task;
import com.yusufmercan.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);

}
