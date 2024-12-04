package com.beehyv.server.repository;

import com.beehyv.server.entity.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT project_id FROM project_tasks " +
            "WHERE tasks_id = :taskId",
            nativeQuery = true)
    Long findProjectIdByTaskId(Long taskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE project_tasks " +
            "SET project_id = :projectId " +
            "WHERE tasks_id = :taskId",
            nativeQuery = true)
    int updateProjectByTaskId(Long projectId, Long taskId);

    @Query(value = "SELECT p.manager_id FROM project p " +
            "INNER JOIN project_tasks pt " +
            "ON p.id = pt.project_id " +
            "WHERE pt.tasks_id = :taskId",
            nativeQuery = true)
    Long findManagerIdByTaskId(Long taskId);

    Project findByManagerId(Long managerId);

}
