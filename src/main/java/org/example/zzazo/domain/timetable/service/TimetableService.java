package org.example.zzazo.domain.timetable.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.domain.lecture.entity.Lecture;
import org.example.zzazo.domain.lecture.repository.LectureRepository;
import org.example.zzazo.domain.timetable.dto.TimetableCreateRequest;
import org.example.zzazo.domain.timetable.dto.TimetableCreateResponse;
import org.example.zzazo.domain.timetable.dto.TimetableListResponse;
import org.example.zzazo.domain.timetable.entity.Timetable;
import org.example.zzazo.domain.timetable.entity.TimetableLecture;
import org.example.zzazo.domain.timetable.repository.TimetableLectureRepository;
import org.example.zzazo.domain.timetable.repository.TimetableRepository;
import org.example.zzazo.domain.user.entity.User;
import org.example.zzazo.domain.user.exception.AuthErrorCode;
import org.example.zzazo.domain.user.security.CustomUserDetails;
import org.example.zzazo.global.error.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final LectureRepository lectureRepository;
    private final TimetableLectureRepository timetableLectureRepository;
    private final EntityManager entityManager;

    @Transactional
    public TimetableCreateResponse createTimetable(TimetableCreateRequest request) {
        User user = entityManager.getReference(User.class, getCurrentUserId());
        List<Lecture> lectures = findSelectedLectures(request.selectedLectureIds());

        Timetable timetable = new Timetable(
                user,
                request.candidateName(),
                request.departmentId(),
                request.preferredFreeDays() == null ? List.of() : request.preferredFreeDays(),
                request.totalCredits(),
                request.targetCredits(),
                request.grade(),
                request.semester()
        );

        Timetable savedTimetable = timetableRepository.save(timetable);
        List<TimetableLecture> timetableLectures = lectures.stream()
                .map(lecture -> TimetableLecture.of(savedTimetable, lecture))
                .toList();
        timetableLectureRepository.saveAll(timetableLectures);

        return new TimetableCreateResponse(savedTimetable.getTimetableId(), "시간표가 저장되었습니다.");
    }

    @Transactional(readOnly = true)
    public TimetableListResponse getTimetables() {
        List<Timetable> timetables = timetableRepository.findAllByUser_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(
                getCurrentUserId()
        );

        return TimetableListResponse.from(timetables);
    }

    private List<Lecture> findSelectedLectures(List<Long> selectedLectureIds) {
        if (selectedLectureIds == null || selectedLectureIds.isEmpty()) {
            return List.of();
        }

        Set<Long> lectureIds = new LinkedHashSet<>(selectedLectureIds);
        List<Lecture> lectures = lectureRepository.findAllById(lectureIds);
        if (lectures.size() != lectureIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "강의를 찾을 수 없습니다.");
        }

        return lectures;
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new CustomException(AuthErrorCode.TOKEN_USER_NOT_FOUND);
        }

        return userDetails.getUserId();
    }
}
