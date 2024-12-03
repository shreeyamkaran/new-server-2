package com.beehyv.server.service;

import com.beehyv.server.dto.SkillRatingDto;
import com.beehyv.server.entity.Skill;

import java.util.List;
import java.util.Map;

public interface SkillService {
    List<Skill> fetchAllSkills();

    Skill fetchSkillById(Long skillId);

    void rateEmployeeSkills(Long employeeId, Long managerId, Map<String, Double> skillsRatings);

    List<SkillRatingDto> getSkillRatingsByManagerAndEmployee(Long employeeId, Long managerId);
}
