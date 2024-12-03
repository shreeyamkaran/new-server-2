package com.beehyv.server.repository;

import com.beehyv.server.dto.SkillRatingDto;
import com.beehyv.server.entity.Employee;
import com.beehyv.server.entity.Rating;
import com.beehyv.server.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByEmployeeAndSkillAndManager(Employee employee, Skill skill, Employee manager);

    @Query("SELECT new com.beehyv.server.dto.SkillRatingDto(r.skill.id, r.ratings, r.employee.id) " +
            "FROM Rating r " +
            "WHERE r.manager.id = :managerId AND r.employee.id = :employeeId")
    List<SkillRatingDto> findSkillRatingsByManagerAndEmployee(
            @Param("employeeId") Long employeeId,
            @Param("managerId") Long managerId);

}




