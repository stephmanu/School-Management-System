package com.steph.taskcrudapi.v1.service;

import com.steph.taskcrudapi.v1.entities.Task;
import com.steph.taskcrudapi.v1.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public ResponseEntity<?> addTask(Task task, String requestId) {
        log.info("[" + requestId + "] is about to process request to add task to the repository");

        try {
            taskRepository.save(task);

        } catch (Exception e) {
            throw new IllegalStateException("An error occurred while persisting task: "
                    + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public void delete(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
