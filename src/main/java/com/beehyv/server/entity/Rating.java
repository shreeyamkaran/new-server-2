package com.beehyv.server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JsonManagedReference
    private Employee employee;
    @ManyToOne
    @JsonManagedReference
    private Skill skill;
    @Column(columnDefinition = "float(53) default 0.0")
    private Double ratings;
    @ManyToOne
    @JsonManagedReference
    private Employee manager;
}
