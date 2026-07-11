package org.example.zzazo.domain.lecture.service;


import lombok.RequiredArgsConstructor;
import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.example.zzazo.domain.curriculum.repository.CurriculumRepository;
import org.example.zzazo.domain.department.repository.DepartmentRepository;
import org.example.zzazo.domain.lecture.domain.LiberalCategory;
import org.example.zzazo.domain.lecture.dto.LectureResponse;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.domain.lecture.repository.LectureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;
    private final CurriculumRepository curriculumRepository;
    private final DepartmentRepository departmentRepository;
    //전공 과목목록 조회
    public LectureResponse.LectureList getMajorList(Long departmentId,int semester) {



        List<Curriculum> curriculumList = curriculumRepository
                .findMajorCurriculumsByDepartmentIdAndSemester(departmentId,semester);

        List<LectureResponse.Lecture> lectureList = curriculumList.stream()
                .map(c -> LectureResponse.Lecture.builder()
                        .lectureId(c.getLecture().getId())
                        .lectureName(c.getLecture().getName())
                        .lectureClassification(c.getLecture().getLectureClassification())
                        .lectureTime(c.getLecture().getLectureSchedules().stream()
                                .map(lectureSchedule -> LectureResponse.LectureTime.builder()
                                        .dayOfWeek(lectureSchedule.getDayOfWeek())
                                        .startTime(lectureSchedule.getStartTime())
                                        .endTime(lectureSchedule.getEndTime())
                                        .build()
                                )
                                .toList()
                        )
                        .build()

                )
                .toList();

        return new LectureResponse.LectureList(lectureList);
    }


    //교양 과목목록 조회
    public LectureResponse.LectureList getLiberalList(LiberalCategory liberalCategory,int semester) {
        List<Lecture> lectures = lectureRepository.findAllByLiberalCategoryAndSemester(liberalCategory,semester);


        List<LectureResponse.Lecture> lectureList = lectures.stream()
                .map(lecture -> LectureResponse.Lecture.builder()
                        .lectureId(lecture.getId())
                        .lectureName(lecture.getName())
                        .credit(lecture.getCredit())
                        .lectureClassification(lecture.getLectureClassification())
                        .lectureTime(lecture.getLectureSchedules().stream().
                                map(lectureSchedule -> LectureResponse.LectureTime.builder()
                                        .dayOfWeek(lectureSchedule.getDayOfWeek())
                                        .startTime(lectureSchedule.getStartTime())
                                        .endTime(lectureSchedule.getEndTime())
                                        .build()

                                )
                                .toList()
                        )
                        .build()

                )
                .toList();


        return new LectureResponse.LectureList(lectureList);
    }

}

