package org.example.zzazo.timetable.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.zzazo.global.common.Week;

import java.util.List;

@Schema(description = "저장한 시간표 상세 응답")
public record TimetableDetailResponse(
        @Schema(description = "저장 시간표 ID", example = "1")
        Long timetableId,

        @Schema(description = "후보명", example = "공강 조건 중심 시간표")
        String candidateName,

        @Schema(description = "학과 ID", example = "1")
        Long departmentId,

        @Schema(description = "학기", example = "2")
        int semester,

        @Schema(description = "학년", example = "1")
        int grade,

        @ArraySchema(schema = @Schema(description = "희망 공강 요일", example = "FRI"))
        List<Week> preferredFreeDays,

        @Schema(description = "목표 학점", example = "20")
        int targetCredits,

        @ArraySchema(schema = @Schema(description = "사용자가 선택한 필수 포함 강의 ID", example = "13"))
        List<Long> selectedLectureIds,

        @Schema(description = "총 학점", example = "20")
        int totalCredits,

        @Schema(description = "수강기준 점검 결과")
        RequirementCheckResponse requirementCheck,

        @ArraySchema(schema = @Schema(implementation = TimetableCourseResponse.class))
        List<TimetableCourseResponse> courses
) {

    public static TimetableDetailResponse example(Long timetableId) {
        return new TimetableDetailResponse(
                timetableId,
                "공강 조건 중심 시간표",
                1L,
                2,
                1,
                List.of(Week.FRI, Week.WED),
                18,
                List.of(13L, 17L),
                20,
                RequirementCheckResponse.example(),
                List.of(TimetableCourseResponse.example())
        );
    }
}
