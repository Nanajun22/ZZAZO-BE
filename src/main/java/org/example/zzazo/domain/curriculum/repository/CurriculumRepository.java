package org.example.zzazo.domain.curriculum.repository;

import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.example.zzazo.global.common.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CurriculumRepository extends JpaRepository<Curriculum,Long> {


    @Query("select distinct c from Curriculum c " +
            "join fetch c.lecture l " +
            "left join fetch l.lectureSchedules " +
            "where c.department.id = :departmentId " +
            "and c.lecture.semester = :semester")
    List<Curriculum> findCurriculumsByDepartmentIdAndSemester(
            @Param("departmentId") Long departmentId,
            @Param("semester") int semester
    );

    @Query("select distinct c from Curriculum c " +
            "join fetch c.lecture l " +
            "left join fetch l.lectureSchedules " +
            "where c.department.id = :departmentId " +
            "and c.lecture.semester = :semester " +
            "and not exists (" +
            "    select 1 from LectureSchedule ls " +
            "    where ls.lecture = l " +
            "    and ls.dayOfWeek in :freeDays" +
            ")")
    List<Curriculum> findCurriculumsByDepartmentIdAndSemesterExcludingFreeDays(
            @Param("departmentId") Long departmentId,
            @Param("semester") int semester,
            @Param("freeDays") List<Week> freeDays
    );


    //전공과목 목록 조회
    @Query("select distinct c from Curriculum c " +
            "join fetch c.lecture l " +
            "left join fetch l.lectureSchedules " +
            "where c.department.id = :departmentId " +
            "and c.lecture.semester = :semester " +
            "and c.lecture.lectureClassification in (org.example.zzazo.domain.lecture.domain.LectureClassification.MAJOR_REQUIREMENT, " +
            "org.example.zzazo.domain.lecture.domain.LectureClassification.MAJOR_ELECTIVE)")
    List<Curriculum> findMajorCurriculumsByDepartmentIdAndSemester(
            @Param("departmentId") Long departmentId,
            @Param("semester") int semester
    );
}
