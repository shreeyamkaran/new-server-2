package com.beehyv.server.service;

import com.beehyv.server.entity.Project;
import com.beehyv.server.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> fetchAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project fetchProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    @Override
    public Project fetchProjectByManagerId(Long managerId) {
        return projectRepository.findByManagerId(managerId);
    }

}
