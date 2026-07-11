package org.example.zzazo.domain.recommend.service;

import lombok.RequiredArgsConstructor;
import org.example.zzazo.domain.LectureSchedule.entity.LectureSchedule;
import org.example.zzazo.domain.curriculum.entity.Curriculum;
import org.example.zzazo.domain.curriculum.repository.CurriculumRepository;
import org.example.zzazo.domain.department.repository.DepartmentRepository;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.domain.lecture.repository.LectureRepository;
import org.example.zzazo.domain.recommend.dto.RecommendRequest;
import org.example.zzazo.domain.recommend.dto.RecommendResponse;
import org.example.zzazo.global.common.Week;
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

        departmentRepository.findById(request.departmentId()).orElseThrow(()-> new IllegalArgumentException("학과가 없어요"));

        //공강 요일 2개 넘을 시 예외처리
        if(request.preferredFreeDays() != null && request.preferredFreeDays().size()>2) {
            throw new IllegalArgumentException("공강 요일은 2개 넘을수없서용" + request.preferredFreeDays());
        }


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



        List<Lecture> selected = new ArrayList<>();
        int totalCredit = 0;


        // 1. 사용자가 지정한 필수과목부터 확정
        List<Long> requiredIds = request.selectedLectureIds() == null
                ? List.of() : request.selectedLectureIds();

        if(requiredIds.stream().distinct().toList().size() != requiredIds.size()) {
            throw new IllegalArgumentException("중복된 강의 입니다");
        }

        Map<Long, Lecture> lectureById = lectureRepository.findAllByIdInWithSchedules(requiredIds).stream()
                .collect(Collectors.toMap(Lecture::getId, l -> l));

        if(lectureById.values().stream().mapToInt(Lecture::getCredit).sum() >= 30) {
            throw new IllegalArgumentException("30 학점이 넘습니다.");
        }

        for (Long lectureId : requiredIds) {
            Lecture lecture = lectureById.getOrDefault(lectureId,null);
            if (lecture == null) {
                throw new IllegalArgumentException("존재하지 않는 과목입니다. lectureId=" + lectureId);
            }

            if(lecture.getLectureSchedules().stream().map(LectureSchedule::getDayOfWeek)
                    .anyMatch(request.preferredFreeDays()::contains)) {
                throw new IllegalArgumentException("공강요일인 과목은 선택할 수 없습니다.");
            }

            if(lecture.getSemester() != request.semester()) {
                throw new IllegalArgumentException("해당 학기에 개설된 강의가 아닙니다."+lectureId);
            }
            if (hasTimeConflict(lecture, selected)) {
                throw new IllegalArgumentException("지정한 과목끼리 시간이 겹칩니다. lectureId=" + lectureId);
            }
            selected.add(lecture);
            totalCredit += lecture.getCredit();
        }

        // 2. 나머지 후보 정렬: 필수 여부 -> 본인학년 이하 여부 -> 학년 오름차순 -> 학점 오름차순(많이 채우기 위한 tie-breaker)
        Set<Long> selectedIds = selected.stream()
                .map(Lecture::getId)
                .collect(Collectors.toSet());

        List<Curriculum> candidates = curriculumList.stream()
                .filter(c -> !selectedIds.contains(c.getLecture().getId()))
                .sorted(priorityComparator(request.grade()))
                .toList();

        // 3. 목표학점 도달 전까지 그리디하게 채우기
        for (Curriculum candidate : candidates) {
            if (totalCredit >= request.targetCredits()) {
                break;
            }
            Lecture lecture = candidate.getLecture();
            int nextCredit = totalCredit + lecture.getCredit();
            if (hasTimeConflict(lecture, selected)) {
                continue;
            }
            selected.add(candidate.getLecture());
            totalCredit = nextCredit;
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