package org.example.zzazo.domain.timetable.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.zzazo.domain.timetable.entity.Timetable;
import org.example.zzazo.global.common.Week;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "저장한 시간표 목록 응답")
public record TimetableListResponse(
        @ArraySchema(schema = @Schema(implementation = TimetableSummaryResponse.class))
        List<TimetableSummaryResponse> timetables
) {

    public static TimetableListResponse from(List<Timetable> timetables) {
        return new TimetableListResponse(timetables.stream()
                .map(TimetableSummaryResponse::from)
                .toList());
    }

    public static TimetableListResponse example() {
        return new TimetableListResponse(List.of(TimetableSummaryResponse.example()));
    }

    @Schema(description = "저장한 시간표 요약")
    public record TimetableSummaryResponse(
            @Schema(description = "저장 시간표 ID", example = "1")
            Long timetableId,

            @Schema(description = "후보명", example = "공강 조건 중심 시간표")
            String candidateName,

            @Schema(description = "학과 ID", example = "1")
            Long departmentId,

            @Schema(description = "총 학점", example = "20")
            int totalCredits,

            @ArraySchema(schema = @Schema(description = "희망 공강 요일", example = "FRI"))
            List<Week> preferredFreeDays,

            @Schema(description = "저장 일시", example = "2026-07-20T12:00:00")
            LocalDateTime createdAt
    ) {

        public static TimetableSummaryResponse from(Timetable timetable) {
            return new TimetableSummaryResponse(
                    timetable.getTimetableId(),
                    timetable.getCandidateName(),
                    timetable.getDepartmentId(),
                    timetable.getTotalCredits(),
                    timetable.getPreferredFreeDays(),
                    timetable.getCreatedAt()
            );
        }

        public static TimetableSummaryResponse example() {
            return new TimetableSummaryResponse(
                    1L,
                    "공강 조건 중심 시간표",
                    1L,
                    20,
                    List.of(Week.FRI, Week.WED),
                    LocalDateTime.of(2026, 7, 20, 12, 0)
            );
        }
    }
}
