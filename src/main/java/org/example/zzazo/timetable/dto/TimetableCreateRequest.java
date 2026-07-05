package org.example.zzazo.timetable.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.zzazo.global.common.Week;

import java.util.List;

@Schema(description = "시간표 저장 요청")
public record TimetableCreateRequest(
        @Schema(description = "후보명", example = "공강 조건 중심 시간표")
        @NotBlank
        String candidateName,

        @Schema(description = "학과 ID", example = "1")
        @NotNull
        Long departmentId,

        @Schema(description = "학기", example = "2", allowableValues = {"1", "2"})
        @NotNull
        @Min(1)
        @Max(2)
        Integer semester,

        @Schema(description = "학년", example = "1")
        @NotNull
        @Min(1)
        @Max(4)
        Integer grade,

        @ArraySchema(schema = @Schema(description = "희망 공강 요일", example = "FRI"))
        @Size(max = 2)
        List<Week> preferredFreeDays,

        @Schema(description = "목표 학점", example = "18")
        @NotNull
        Integer targetCredits,

        @ArraySchema(schema = @Schema(description = "사용자가 선택한 필수 포함 강의 ID", example = "13"))
        List<Long> selectedLectureIds,

        @Schema(description = "총 학점", example = "20")
        @NotNull
        Integer totalCredits,

        @Schema(description = "수강기준 점검 결과")
        @Valid
        @NotNull
        RequirementCheckResponse requirementCheck,

        @ArraySchema(schema = @Schema(implementation = TimetableCourseResponse.class))
        @NotEmpty
        List<@Valid TimetableCourseResponse> courses
) {
}
