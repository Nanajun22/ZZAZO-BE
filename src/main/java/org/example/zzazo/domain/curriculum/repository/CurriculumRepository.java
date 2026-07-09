package org.example.zzazo.domain.curriculum.repository;

import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurriculumRepository extends JpaRepository<Curriculum,Long> {



    //전공과목 목록 조회
    @Query("select distinct c from Curriculum c " +
            "join fetch c.lecture l " +
            "left join fetch l.lectureSchedules " +
            "where c.department.id = :departmentId " +
            "and c.lecture.semester = :semester " +
            "and c.lecture.lectureClassification in (org.example.zzazo.domain.lecture.domain.LectureClassification.MAJOR_REQUIREMENT, " +
            "org.example.zzazo.domain.lecture.domain.LectureClassification.MAJOR_ELECTIVE)")
    List<Curriculum> findAllByDepartmentIdWithLectureAndSchedules(
            @Param("departmentId") Long departmentId,
            @Param("semester") int semester
    );
}
