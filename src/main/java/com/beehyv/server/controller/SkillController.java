package com.beehyv.server.controller;

import com.beehyv.server.dto.SkillRatingDto;
import com.beehyv.server.entity.Skill;
import com.beehyv.server.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping("/api/v1/skills")
    public List<Skill> fetchAllSkills() {
        return skillService.fetchAllSkills();
    }

    @GetMapping("/api/v1/skills/{skill-id}")
    public Skill fetchSkillById(@PathVariable("skill-id") Long skillId) {
        return skillService.fetchSkillById(skillId);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/api/v1/skills/ratings/employee/{employee-id}/manager/{manager-id}")
    public void rateEmployeeSkills(@PathVariable("employee-id") Long employeeId, @PathVariable("manager-id") Long managerId, @RequestBody Map<String, Double> skillsRatings) {
        skillService.rateEmployeeSkills(employeeId, managerId, skillsRatings);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("api/v1/skills/ratings/employee/{employee-id}/manager/{manager-id}")
    public ResponseEntity<List<SkillRatingDto>> getSkillRatings(@PathVariable("employee-id") Long employeeId, @PathVariable("manager-id") Long managerId) {
        List<SkillRatingDto> ratings = skillService.getSkillRatingsByManagerAndEmployee(employeeId, managerId);
        return ResponseEntity.ok(ratings);
    }

}
