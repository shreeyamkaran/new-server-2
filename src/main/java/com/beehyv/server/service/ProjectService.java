package com.beehyv.server.service;

import com.beehyv.server.entity.Project;

import java.util.List;

public interface ProjectService {
    List<Project> fetchAllProjects();

    Project fetchProjectById(Long projectId);

    Project fetchProjectByManagerId(Long managerId);
}
