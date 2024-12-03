package com.beehyv.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Date date;
    private Integer duration;
    private String appraisalStatus;
    @Column(columnDefinition = "float(53) default 0.0")
    private Double ratings;
    @Column(columnDefinition = "int default 0")
    private Integer numberOfRatings;
}
