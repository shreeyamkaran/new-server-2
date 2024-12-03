package com.beehyv.server.dto;

import com.beehyv.server.entity.Designation;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeDto {
    private Long id;
    private String name;
    private String username;
    private Designation designation;
    private Date dob;
    private String gender;
    private Date doj;
    private String Location;
    private Double ratings;
}