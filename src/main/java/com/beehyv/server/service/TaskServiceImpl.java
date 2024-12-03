package com.beehyv.server.service;

import com.beehyv.server.dto.TaskDto;
import com.beehyv.server.entity.Employee;
import com.beehyv.server.entity.Task;
import com.beehyv.server.repository.EmployeeRepository;
import com.beehyv.server.repository.ProjectRepository;
import com.beehyv.server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Task> fetchAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task fetchTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    @Override
    public void updateTask(Long taskId, TaskDto task) {
        Task previousTask = taskRepository.findById(taskId).orElse(null);
        if(previousTask != null) {
            previousTask.setTitle(task.getTitle());
            previousTask.setDescription(task.getDescription());
            previousTask.setDate(task.getDate());
            previousTask.setDuration(task.getDuration());
            previousTask.setAppraisalStatus(task.getAppraisalStatus());
            projectRepository.updateProjectByTaskId(task.getProjectId(), taskId);
        }
    }

    @Override
    public void createTask(Long employeeId, TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDate(taskDto.getDate());
        task.setDuration(taskDto.getDuration());
        task.setAppraisalStatus(taskDto.getAppraisalStatus());
        task.setRatings(taskDto.getRatings());
        task.setNumberOfRatings(taskDto.getNumberOfRatings());
        Task createdTask = taskRepository.save(task);
        taskRepository.mapTaskIdWithEmployeeId(createdTask.getId(), employeeId);
        taskRepository.mapTaskIdWithProjectId(createdTask.getId(), taskDto.getProjectId());
    }

    @Override
    public void rateTask(Long taskId, Double rating) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if(task == null) {
            return;
        }
        task.setRatings(rating);
        task.setNumberOfRatings(1);
        taskRepository.save(task);
        Long employeeId = taskRepository.findEmployeeIdByTaskId(taskId);
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if(employee == null) {
            return;
        }
        employee.setRatings((employee.getRatings() * employee.getNumberOfRatings() + rating) / (employee.getNumberOfRatings() + 1));
        employee.setNumberOfRatings(employee.getNumberOfRatings() + 1);
        employeeRepository.save(employee);
    }

    @Override
    public void updateTaskRating(Long taskId, Double rating) {
        Task task = taskRepository.findById(taskId).orElse(null);
        Double oldRating = task.getRatings();
        if(task == null) {
            return;
        }
        task.setRatings(rating);
        taskRepository.save(task);
        Long employeeId = taskRepository.findEmployeeIdByTaskId(taskId);
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if(employee == null) {
            return;
        }
        employee.setRatings((employee.getRatings() * employee.getNumberOfRatings() - oldRating + rating) / employee.getNumberOfRatings());
        employeeRepository.save(employee);
    }

}
