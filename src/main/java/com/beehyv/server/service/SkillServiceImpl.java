package com.beehyv.server.service;

import com.beehyv.server.dto.SkillRatingDto;
import com.beehyv.server.entity.Employee;
import com.beehyv.server.entity.Rating;
import com.beehyv.server.entity.Skill;
import com.beehyv.server.repository.EmployeeRepository;
import com.beehyv.server.repository.RatingRepository;
import com.beehyv.server.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public List<Skill> fetchAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Skill fetchSkillById(Long skillId) {
        return skillRepository.findById(skillId).orElse(null);
    }

    @Override
    public void rateEmployeeSkills(Long employeeId, Long managerId, Map<String, Double> skillsRatings) {
        // Validate employee and manager existence
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + employeeId));
        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("Manager not found with id: " + managerId));

        int newRatingsCount = 0; // Count new ratings for updating the number_of_ratings field
        double totalSkillRating = 0.0; // Sum of skill ratings for calculating the average

        // Loop through the skill ratings map
        for (Map.Entry<String, Double> entry : skillsRatings.entrySet()) {
            Long skillId = Long.parseLong(entry.getKey());
            Double ratingValue = entry.getValue();

            // Find the skill
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + skillId));

            // Check if the rating entry already exists
            Optional<Rating> existingRating = ratingRepository.findByEmployeeAndSkillAndManager(employee, skill, manager);

            if (existingRating.isPresent()) {
                // Update the rating if it already exists
                Rating rating = existingRating.get();
                totalSkillRating += ratingValue - rating.getRatings(); // Adjust total rating by the difference
                rating.setRatings(ratingValue);
                ratingRepository.save(rating);
            } else {
                // Create a new rating entry if it doesn't exist
                Rating newRating = new Rating();
                newRating.setEmployee(employee);
                newRating.setSkill(skill);
                newRating.setManager(manager);
                newRating.setRatings(ratingValue);
                ratingRepository.save(newRating);

                // Increment the count for new ratings
                newRatingsCount++;
                totalSkillRating += ratingValue;
            }
        }

        // Update the employee's overall ratings and number_of_ratings
        double updatedTotalRatings = (employee.getRatings() * employee.getNumberOfRatings()) + totalSkillRating;
        int updatedNumberOfRatings = employee.getNumberOfRatings() + newRatingsCount;
        double updatedAverageRating = updatedNumberOfRatings > 0 ? updatedTotalRatings / updatedNumberOfRatings : 0.0;

        employee.setRatings(updatedAverageRating);
        employee.setNumberOfRatings(updatedNumberOfRatings);
        employeeRepository.save(employee);
    }

    @Override
    public List<SkillRatingDto> getSkillRatingsByManagerAndEmployee(Long employeeId, Long managerId) {
        return ratingRepository.findSkillRatingsByManagerAndEmployee(employeeId, managerId);
    }

}
