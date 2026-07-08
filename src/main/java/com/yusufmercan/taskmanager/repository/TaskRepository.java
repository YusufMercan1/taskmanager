package com.yusufmercan.taskmanager.repository;

import com.yusufmercan.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
