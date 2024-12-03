package com.beehyv.server.service;

import com.beehyv.server.entity.Designation;

import java.util.List;

public interface DesignationService {
    List<Designation> fetchAllDesignations();

    Designation fetchDesignationById(Long designationId);
}
