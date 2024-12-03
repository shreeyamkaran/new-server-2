package com.beehyv.server.controller;

import com.beehyv.server.dto.TaskDto;
import com.beehyv.server.entity.Task;
import com.beehyv.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/api/v1/tasks")
    public List<Task> fetchAllTasks() {
        return taskService.fetchAllTasks();
    }

    @GetMapping("/api/v1/tasks/{task-id}")
    public Task fetchTaskById(@PathVariable("task-id") Long taskId) {
        return taskService.fetchTaskById(taskId);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/api/v1/tasks/{task-id}")
    public void updateTask(@PathVariable("task-id") Long taskId, @RequestBody TaskDto task) {
        taskService.updateTask(taskId, task);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/api/v1/tasks/{employee-id}")
    public void createTask(@PathVariable("employee-id") Long employeeId, @RequestBody TaskDto task) {
        taskService.createTask(employeeId, task);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/api/v1/tasks/rateTask/{task-id}")
    public void rateTask(@PathVariable("task-id") Long taskId, @RequestBody Map<String, Double> ratingMap) {
        taskService.rateTask(taskId, ratingMap.get("rating"));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/api/v1/tasks/updateTaskRating/{task-id}")
    public void updateTaskRating(@PathVariable("task-id") Long taskId, @RequestBody Map<String, Double> ratingMap) {
        taskService.updateTaskRating(taskId, ratingMap.get("rating"));
    }

}
