package com.beehyv.server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    @ManyToOne
    @JsonManagedReference
    private Designation designation;
    private Date dob;
    private String gender;
    private Date doj;
    private String location;
    @ManyToMany
    @JsonManagedReference
    private List<Task> tasks;
    @ManyToMany
    @JsonManagedReference
    private List<Project> projects;
    @Column(columnDefinition = "float(53) default 0.0")
    private Double ratings;
    @Column(columnDefinition = "int default 0")
    private Integer numberOfRatings;
}
