package org.example.zzazo.timetable.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "수강기준 점검 결과")
public record RequirementCheckResponse(
        @Schema(description = "목표 학점 충족 여부", example = "true")
        boolean targetCreditSatisfied,

        @Schema(description = "필수 과목 포함 여부", example = "true")
        boolean requiredCourseIncluded,

        @Schema(description = "기초/계열기초 과목 포함 여부", example = "true")
        boolean foundationCourseIncluded,

        @Schema(description = "교양 과목 포함 여부", example = "true")
        boolean generalEducationIncluded
) {

    public static RequirementCheckResponse example() {
        return new RequirementCheckResponse(true, true, true, true);
    }
}
