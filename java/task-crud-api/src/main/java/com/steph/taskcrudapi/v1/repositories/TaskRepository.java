package com.steph.taskcrudapi.v1.repositories;

import com.steph.taskcrudapi.v1.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}