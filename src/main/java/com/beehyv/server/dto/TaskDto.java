package com.beehyv.server.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Date date;
    private Integer duration;
    private String appraisalStatus;
    private Long projectId;
    private String projectName;
    private Double ratings;
    private Integer numberOfRatings;
}
