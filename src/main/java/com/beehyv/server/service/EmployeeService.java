package com.beehyv.server.service;

import com.beehyv.server.dto.EmployeeDto;
import com.beehyv.server.dto.TaskDto;
import com.beehyv.server.entity.Employee;
import com.beehyv.server.entity.Project;

import java.util.List;

public interface EmployeeService {
    List<Employee> fetchAllEmployees();

    EmployeeDto fetchEmployeeById(Long employeeId);

    List<TaskDto> fetchEmployeesTasks(Long employeeId);

    List<Project> fetchEmployeesProjects(Long employeeId);

    List<Object> findEmployeesSkillsAndRatings(Long employeeId);

    List<EmployeeDto> findEmployeesUnderManager(Long managerId);
}
