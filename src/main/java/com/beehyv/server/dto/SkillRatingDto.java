package com.beehyv.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillRatingDto {
    private Long skillId;
    private Double ratings;
    private Long employeeId;
}
