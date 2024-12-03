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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonManagedReference
    private Employee sender;
    @ManyToOne
    @JsonManagedReference
    private Employee receiver;
    @ManyToOne
    @JsonManagedReference
    private Employee subject;
    private Boolean readStatus;
    private String title;
    private String description;
}
