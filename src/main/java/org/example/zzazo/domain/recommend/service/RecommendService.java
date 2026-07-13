package org.example.zzazo.domain.recommend.service;

import lombok.RequiredArgsConstructor;

import org.example.zzazo.domain.lectureschedule.entity.LectureSchedule;
import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.example.zzazo.domain.curriculum.repository.CurriculumRepository;
import org.example.zzazo.domain.department.repository.DepartmentRepository;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.domain.lecture.repository.LectureRepository;
import org.example.zzazo.domain.recommend.domain.Priority;
import org.example.zzazo.domain.recommend.dto.RecommendRequest;
import org.example.zzazo.domain.recommend.dto.RecommendResponse;
import org.example.zzazo.domain.recommend.exception.RecommendErrorCode;
import org.example.zzazo.global.common.Week;
import org.example.zzazo.global.error.CustomException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final CurriculumRepository curriculumRepository;
    private final LectureRepository lectureRepository;
    private final DepartmentRepository departmentRepository;

    public RecommendResponse.RecommendResult recommendTimeTable(RecommendRequest.createRecommendRequest request) {

        departmentRepository.findById(request.departmentId())
                .orElseThrow(()-> new CustomException(RecommendErrorCode.DEPARTMENT_NOT_EXISTS)
        );

        //학과 커리큘럼 과목목록 가져오기
        List<Curriculum> curriculumList = request.preferredFreeDays() == null || request.preferredFreeDays().isEmpty()
                ? curriculumRepository
                        .findCurriculumsByDepartmentIdAndSemester(
                                request.departmentId(),
                                request.semester()
                        )
                : curriculumRepository
                        .findCurriculumsByDepartmentIdAndSemesterExcludingFreeDays(
                                request.departmentId(),
                                request.semester(),
                                request.preferredFreeDays()
                        );


        // 사용자가 지정한 필수과목부터 확정
        List<Lecture> selected = putRequired(request);
        int totalCredit = selected.stream().mapToInt(Lecture::getCredit).sum();



        // 나머지 후보 정렬: 필수 여부 -> 본인학년 이하 여부 -> 학년 오름차순 -> 학점 오름차순
        Set<Long> selectedIds = selected.stream()
                .map(Lecture::getId)
                .collect(Collectors.toSet());

        List<Curriculum> candidates = curriculumList.stream()
                .filter(c -> !selectedIds.contains(c.getLecture().getId()))
                .sorted(priorityComparator(request.grade()))
                .toList();

        if(request.priority() == Priority.FREE_PERIOD) {
            fillGreedyByFreeDays(candidates,selected,request.targetCredits(),request.grade());
            totalCredit = selected.stream().mapToInt(Lecture::getCredit).sum();
        }

        else {

            // 목표학점 도달 전까지 그리디하게 채우기
            for (Curriculum candidate : candidates) {
                if (totalCredit >= request.targetCredits()) {
                    break;
                }
                Lecture lecture = candidate.getLecture();
                int nextCredit = totalCredit + lecture.getCredit();

                if (hasTimeConflict(lecture, selected) || nextCredit >=30 || hasSameLecture(lecture,selected)) {
                    continue;
                }


                selected.add(candidate.getLecture());
                totalCredit = nextCredit;
            }

        }
        List<RecommendResponse.Lecture> result = selected.stream()
                .map(l -> new RecommendResponse.Lecture(
                        l.getId(),
                        l.getName(),
                        l.getCredit(),
                        l.getProfessor(),
                        l.getClassroom(),
                        l.getLectureClassification(),
                        l.getLectureSchedules().stream()
                                .map(lectureSchedule -> new RecommendResponse.Lecture.LectureTime(
                                        lectureSchedule.getStartTime(),
                                        lectureSchedule.getEndTime(),
                                        lectureSchedule.getDayOfWeek()
                                )).toList()
                        )
                )
                .toList();

        return new RecommendResponse.RecommendResult(totalCredit,getFreeDays(selected),result);
    }

    private List<Lecture> putRequired(RecommendRequest.createRecommendRequest request) {
        List<Lecture> selected = new ArrayList<>();


        // 사용자가 지정한 필수과목부터 확정
        List<Long> requiredIds = request.selectedLectureIds();

        // 중복강의 체크
        if(requiredIds.stream().distinct().toList().size() != requiredIds.size()) {
            throw new CustomException(RecommendErrorCode.SELECT_DUPLICATED);
        }

        Map<Long, Lecture> lectureById = lectureRepository.findAllByIdInWithSchedules(requiredIds).stream()
                .collect(Collectors.toMap(Lecture::getId, l -> l));

        // 30학점 이상 체크
        if(lectureById.values().stream().mapToInt(Lecture::getCredit).sum() >= 30) {
            throw new CustomException(RecommendErrorCode.EXCEED_THIRTY);
        }

        for (Long lectureId : requiredIds) {
            Lecture lecture = lectureById.getOrDefault(lectureId,null);

            //존재하지 않는 강의
            if (lecture == null) {
                throw new CustomException(RecommendErrorCode.LECTURE_NOT_EXISTS);
            }


            if(lecture.getLectureSchedules().stream().map(LectureSchedule::getDayOfWeek)
                    .anyMatch(request.preferredFreeDays()::contains)) {
                throw new CustomException(RecommendErrorCode.SELECT_FREE_DAYS);
            }

            //학기 불일치
            if(lecture.getSemester() != request.semester()) {
                throw new CustomException(RecommendErrorCode.SEMESTER_NOT_EQUALS);
            }

            if (hasTimeConflict(lecture, selected)) {
                throw new CustomException(RecommendErrorCode.SCHEDULE_OVERLAPPED);
            }
            selected.add(lecture);
        }

        return selected;

    }

    private void fillGreedyByFreeDays(List<Curriculum> candidates, List<Lecture> selected,
                                      int targetCredits, int userGrade) {
        List<Curriculum> remaining = new ArrayList<>(candidates);
        int totalCredit = selected.stream().mapToInt(Lecture::getCredit).sum();

        while (totalCredit < targetCredits && !remaining.isEmpty()) {
            Set<Week> usedDays = selected.stream()
                    .flatMap(l -> l.getLectureSchedules().stream())
                    .map(LectureSchedule::getDayOfWeek)
                    .collect(Collectors.toSet());

            Optional<Curriculum> next = remaining.stream()
                    .filter(c -> !hasTimeConflict(c.getLecture(), selected))
                    .min(dynamicFreeDayComparator(userGrade, usedDays));

            if (next.isEmpty()) {
                break; // 더 이상 넣을 수 있는 후보가 없음
            }

            Curriculum chosen = next.get();

            if(hasSameLecture(chosen.getLecture(),selected)) {
                remaining.remove(chosen);
                continue;
            }


            selected.add(chosen.getLecture());
            totalCredit += chosen.getLecture().getCredit();
            remaining.remove(chosen);
        }
    }


    private Comparator<Curriculum> dynamicFreeDayComparator(int userGrade, Set<Week> usedDays) {
        return Comparator
                .comparing((Curriculum c) -> c.getGrade() > userGrade)     // 1순위: 학년 이하 그룹
                .thenComparing(c -> !c.getIsRequired())                    // 2순위: 필수 먼저
                .thenComparingLong(c -> newDaysCount(c.getLecture(), usedDays)) // 3순위: 새로 늘어나는 요일 최소화
                .thenComparing(Curriculum::getGrade)
                .thenComparing(c -> c.getLecture().getCredit());
    }

    private long newDaysCount(Lecture lecture, Set<Week> usedDays) {
        return lecture.getLectureSchedules().stream()
                .map(LectureSchedule::getDayOfWeek)
                .distinct()
                .filter(day -> !usedDays.contains(day))
                .count();
    }



    private boolean hasSameLecture(Lecture candidate, List<Lecture> selected) {
        for (Lecture l : selected) {
            if(l.getLectureGroup().getId().equals(candidate.getLectureGroup().getId())) {
                return true;
            }
        }
        return false;
    }

    private List<Week> getFreeDays(List<Lecture> selected) {
        Set<Week> usedDays = selected.stream()
                .flatMap(c -> c.getLectureSchedules().stream())
                .map(LectureSchedule::getDayOfWeek)
                .collect(Collectors.toSet());

        return Arrays.stream(Week.values())
                .filter(day -> !usedDays.contains(day))
                .toList();
    }
    private Comparator<Curriculum> priorityComparator(int userGrade) {
        return Comparator
                .comparing((Curriculum c) -> c.getGrade() > userGrade)   // 1순위: 학년 그룹 (이하 먼저)
                .thenComparing(c -> !c.getIsRequired())                  // 2순위: 필수 먼저
                .thenComparing(Curriculum::getGrade)                     // 3순위: 학년 오름차순
                .thenComparing(c -> c.getLecture().getCredit());
    }

    private boolean hasTimeConflict(Lecture candidate, List<Lecture> selected) {
        for (Lecture l : selected) {
            for (LectureSchedule s1 : candidate.getLectureSchedules()) {
                for (LectureSchedule s2 : l.getLectureSchedules()) {
                    if (isOverlap(s1, s2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isOverlap(LectureSchedule s1, LectureSchedule s2) {
        if (!s1.getDayOfWeek().equals(s2.getDayOfWeek())) {
            return false;
        }
        return s1.getStartTime().isBefore(s2.getEndTime())
                && s2.getStartTime().isBefore(s1.getEndTime());
    }
}