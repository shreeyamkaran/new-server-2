package com.beehyv.server.service;

import com.beehyv.server.dto.EmployeeDto;
import com.beehyv.server.dto.TaskDto;
import com.beehyv.server.entity.Employee;
import com.beehyv.server.entity.Project;
import com.beehyv.server.entity.Task;
import com.beehyv.server.repository.EmployeeRepository;
import com.beehyv.server.repository.ProjectRepository;
import com.beehyv.server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Employee> fetchAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public EmployeeDto fetchEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        EmployeeDto employeeDto = new EmployeeDto();
        if(employee != null) {
            employeeDto.setId(employee.getId());
            employeeDto.setName(employee.getName());
            employeeDto.setUsername(employee.getUsername());
            employeeDto.setDesignation(employee.getDesignation());
            employeeDto.setDob(employee.getDob());
            employeeDto.setGender(employee.getGender());
            employeeDto.setDoj(employee.getDoj());
            employeeDto.setLocation(employee.getLocation());
            employeeDto.setRatings(employee.getRatings());

            return employeeDto;
        }
        else {
            return null;
        }
    }

    @Override
    public List<TaskDto> fetchEmployeesTasks(Long employeeId) {
        List<Long> taskIds = employeeRepository.findEmployeesTaskIds(employeeId);
        List<TaskDto> tasks = new ArrayList<>();
        for(Long taskId: taskIds) {
            Task task = taskRepository.findById(taskId).orElse(null);
            Long projectId = projectRepository.findProjectIdByTaskId(taskId);
            Project project = projectRepository.findById(projectId).orElse(null);
            TaskDto taskDto = new TaskDto();
            if(task != null) {
                taskDto.setId(task.getId());
                taskDto.setTitle(task.getTitle());
                taskDto.setDescription(task.getDescription());
                taskDto.setDate(task.getDate());
                taskDto.setDuration(task.getDuration());
                taskDto.setAppraisalStatus(task.getAppraisalStatus());
                taskDto.setRatings(task.getRatings());
                taskDto.setNumberOfRatings(task.getNumberOfRatings());
            }
            if(project != null) {
                taskDto.setProjectId(project.getId());
                taskDto.setProjectName(project.getName());
            }
            tasks.add(taskDto);
        }
        return tasks;
    }

    @Override
    public List<Project> fetchEmployeesProjects(Long employeeId) {
        List<Long> projectIds = employeeRepository.findEmployeesProjectIds(employeeId);
        List<Project> projects = new ArrayList<>();
        for(Long projectId: projectIds) {
            Project project = projectRepository.findById(projectId).orElse(null);
            projects.add(project);
        }
        return projects;
    }

    @Override
    public List<Object> findEmployeesSkillsAndRatings(Long employeeId) {
        return employeeRepository.findEmployeesSkillsAndRatings(employeeId);
    }

    @Override
    public List<EmployeeDto> findEmployeesUnderManager(Long managerId) {
        List<Long> employeesIdsUnderManager = employeeRepository.findEmployeesIdsUnderManager(managerId);
        List<EmployeeDto> employees = new ArrayList<>();
        for(Long employeeId: employeesIdsUnderManager) {
            EmployeeDto employee = fetchEmployeeById(employeeId);
            employees.add(employee);
        }
        return employees;
    }

}
