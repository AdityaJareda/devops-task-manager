package com.devops.internship.controller;

import com.devops.internship.model.Task;
import com.devops.internship.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity createTask(@RequestBody Task task) {
        Task created = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@PathVariable Long id, 
                                          @RequestBody Task task) {
        Task updated = taskService.updateTask(id, task);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    public ResponseEntity health() {
        return ResponseEntity.ok("Task API is running!");
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<Task> tasks = taskService.getAllTasks();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", tasks.size());
        stats.put("completed", tasks.stream().filter(Task::isCompleted).count());
        stats.put("pending", tasks.stream().filter(t -> !t.isCompleted()).count());
        
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/complete-all")
    public ResponseEntity completeAllTasks() {
        taskService.completeAllTasks();
        return ResponseEntity.ok("All tasks marked as completed");
    }
}
