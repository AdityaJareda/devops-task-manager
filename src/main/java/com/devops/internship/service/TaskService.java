package com.devops.internship.service;

import com.devops.internship.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {
    
    private final List<Task> tasks = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public TaskService() {
        // Add some sample tasks
        tasks.add(new Task(counter.incrementAndGet(), 
            "Learn Maven", "Understand Maven build process", false));
        tasks.add(new Task(counter.incrementAndGet(), 
            "Learn Docker", "Containerize the application", false));
        tasks.add(new Task(counter.incrementAndGet(), 
            "Setup CI/CD", "Automate build and deployment", false));
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Task getTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Task createTask(Task task) {
        task.setId(counter.incrementAndGet());
        tasks.add(task);
        return task;
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = getTaskById(id);
        if (task != null) {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
        }
        return task;
    }

    public boolean deleteTask(Long id) {
        return tasks.removeIf(task -> task.getId().equals(id));
    }

    public void completeAllTasks() {
        tasks.forEach(task -> task.setCompleted(true));
    }

    public int clearCompletedTasks() {
        int removedCount = 0;
        List<Task> completedTasks = tasks.stream()
                .filter(Task::isCompleted)
                .toList();
    
        removedCount = completedTasks.size();
        tasks.removeAll(completedTasks);
    
        return removedCount;
    }
}
