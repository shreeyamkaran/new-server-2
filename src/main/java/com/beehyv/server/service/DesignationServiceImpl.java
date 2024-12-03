package com.beehyv.server.service;

import com.beehyv.server.entity.Designation;
import com.beehyv.server.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignationServiceImpl implements DesignationService {

    @Autowired
    private DesignationRepository designationRepository;

    @Override
    public List<Designation> fetchAllDesignations() {
        return designationRepository.findAll();
    }

    @Override
    public Designation fetchDesignationById(Long designationId) {
        return designationRepository.findById(designationId).orElse(null);
    }

}
