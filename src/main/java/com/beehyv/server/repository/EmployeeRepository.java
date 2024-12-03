package com.beehyv.server.repository;

import com.beehyv.server.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "SELECT t.* FROM task t " +
            "JOIN employee_tasks et ON t.id = et.tasks_id " +
            "WHERE et.employee_id = :employeeId",
            nativeQuery = true)
    List<Long> findEmployeesTaskIds(Long employeeId);

    @Query(value = "SELECT p.* FROM project p " +
            "JOIN employee_projects ep ON p.id = ep.projects_id " +
            "WHERE ep.employee_id = :employeeId",
            nativeQuery = true)
    List<Long> findEmployeesProjectIds(Long employeeId);

    @Query(value = "SELECT s.id AS skill_id, " +
            "s.name AS skill_name, " +
            "COALESCE(AVG(r.ratings), 0.0) AS avg_rating, " +
            "s.category AS skill_category " +
            "FROM employee e " +
            "JOIN designation d ON e.designation_id = d.id " +
            "JOIN designation_skills ds ON d.id = ds.designation_id " +
            "JOIN skill s ON ds.skills_id = s.id " +
            "LEFT JOIN rating r ON e.id = r.employee_id AND s.id = r.skill_id " +
            "WHERE e.id = :employeeId " +
            "GROUP BY s.id, s.name, s.category",
            nativeQuery = true)
    List<Object> findEmployeesSkillsAndRatings(Long employeeId);

    @Query(value = "SELECT e.id AS employee_id " +
            "FROM employee e " +
            "JOIN employee_projects ep ON e.id = ep.employee_id " +
            "JOIN project p ON ep.projects_id = p.id " +
            "WHERE p.manager_id = :managerId",
            nativeQuery = true)
    List<Long> findEmployeesIdsUnderManager(Long managerId);

    Optional<Employee> findByUsername(String username);
}
