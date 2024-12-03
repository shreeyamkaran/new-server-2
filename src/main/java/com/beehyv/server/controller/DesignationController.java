package com.beehyv.server.controller;

import com.beehyv.server.entity.Designation;
import com.beehyv.server.service.DesignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DesignationController {

    @Autowired
    private DesignationService designationService;

    @GetMapping("/api/v1/designations")
    public List<Designation> fetchAllDesignations() {
        return designationService.fetchAllDesignations();
    }

    @GetMapping("/api/v1/designations/{designation-id}")
    public Designation fetchDesignationById(@PathVariable("designation-id") Long designationId) {
        return designationService.fetchDesignationById(designationId);
    }

}
