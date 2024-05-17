package com.steph.taskcrudapi.v1.controller;

import com.steph.taskcrudapi.v1.entities.Task;
import com.steph.taskcrudapi.v1.models.AddTaskModel;
import com.steph.taskcrudapi.v1.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.grammars.hql.HqlParser;
import org.hibernate.type.descriptor.java.ZonedDateTimeJavaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;

        Objects.requireNonNull(taskService, "Task service is required");
    }

    // add task
    @PostMapping(value = "api/v1/add", produces = "application/json")
    public ResponseEntity<?> addPost (HttpServletResponse response, HttpServletRequest request,
                                      @RequestBody AddTaskModel addTaskModel){

        String requestId = request.getSession().getId();
        log.info("[" + requestId + "] is about to process request to add task to the system.");

        Task task = new Task();

        if (addTaskModel.getPost() != null && !addTaskModel.getPost().isEmpty()){
            task.setPost(addTaskModel.getPost());
        }

        if (addTaskModel.getTitle() != null && !addTaskModel.getTitle().isEmpty()){
            task.setTitle(addTaskModel.getTitle());
        }

        ZonedDateTime currentDateTime = ZonedDateTime.now();

        task.setCreatedOn(currentDateTime);
        task.setUpdatedOn(currentDateTime);

        ResponseEntity<?> addTaskResponse = taskService.addTask(task, requestId);

        log.info("[" + requestId + "] request to add task resulted in: " + addTaskResponse);

        return ResponseEntity.status(addTaskResponse.getStatusCode())
                .body(addTaskResponse.getBody());
    }

    // get all tasks
    @GetMapping(value = "api/v1/tasks/getall", produces = "application/json")
    public ResponseEntity<?> getAllTasks (HttpServletResponse response,
                                          HttpServletRequest request){

        String requestId = request.getSession().getId();
        log.info("[" + requestId + "] is about to process request to get all tasks.");

        List<Task> tasks = taskService.findAll();

        if (tasks.isEmpty()){

            log.info("[" + requestId + "] request to get all tasks failed. No task available.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No task available.");
        }

        log.info("[" + requestId + "] request to get all task was successful");

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    // update task
    @PutMapping(value = "api/v1/tasks/{taskId}/update", produces = "application/json")
    public ResponseEntity<?> updateTask (HttpServletResponse response,
                                         HttpServletRequest request,
                                         @PathVariable Long taskId,
                                         @RequestBody AddTaskModel addTaskModel){

        String requestId = request.getSession().getId();
        log.info("[" + requestId + "] is about to process request to update task with ID: "
                + taskId);

        Optional<Task> optionalTask = taskService.findById(taskId);

        if(optionalTask.isEmpty()){
            log.info("[" + requestId + "] request to update task failed. Task does not exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task does not exist.");
        }

        Task task = optionalTask.get();

        if (addTaskModel.getPost() != null && !addTaskModel.getPost().isEmpty()){
            task.setPost(addTaskModel.getPost());
        }

        if (addTaskModel.getTitle() != null && !addTaskModel.getTitle().isEmpty()){
            task.setTitle(addTaskModel.getTitle());
        }

        ZonedDateTime currentDateTime = ZonedDateTime.now();

        task.setUpdatedOn(currentDateTime);

        ResponseEntity<?> addTaskResponse = taskService.addTask(task, requestId);

        log.info("[" + requestId + "] request to update task with ID: "
                + taskId + " resulted in: " + addTaskResponse);

        return ResponseEntity.status(addTaskResponse.getStatusCode())
                .body(addTaskResponse.getBody());
    }

    // delete task
    @DeleteMapping(value = "api/v1/tasks/{taskId}/delete", produces = "application/json")
    public ResponseEntity<?> deleteTask (HttpServletResponse response,
                                        HttpServletRequest request,
                                        @PathVariable Long taskId) {

        String requestId = request.getSession().getId();
        log.info("[" + requestId + "] is about to process request to delete task with ID: "
                + taskId);

        Optional<Task> optionalTask = taskService.findById(taskId);
        if (optionalTask.isEmpty()){
            log.info("[" + requestId + "] request to delete task failed. Task does not exist.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not available.");
        }

        taskService.delete(taskId);

        log.info("[" + requestId + "] request to delete task with ID: "
                + taskId + " was successful.");

        return ResponseEntity.status(HttpStatus.OK).body("Task Successfully deleted.");

    }

}
