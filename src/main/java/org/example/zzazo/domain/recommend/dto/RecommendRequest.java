package org.example.zzazo.domain.recommend.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.zzazo.domain.recommend.domain.Priority;
import org.example.zzazo.global.common.Week;

import java.util.List;

public class RecommendRequest {
    public record createRecommendRequest(

            @Schema(
                    description = "사용자의 학과 ID",
                    example = "1",
                    requiredMode = Schema.RequiredMode.REQUIRED
            )
            @NotNull(message = "학과 정보는 필수입니다.")
            Long departmentId,

            @Schema(
                    description = "사용자의 학년",
                    example = "2",
                    minimum = "1",
                    maximum = "4",
                    requiredMode = Schema.RequiredMode.REQUIRED
            )
            @Min(value = 1, message = "학년은 1~4학년만 가능합니다.")
            @Max(value = 4, message = "학년은 1~4학년만 가능합니다.")
            @NotNull(message = "학년 정보는 필수입니다.")
            Integer grade,

            @Schema(
                    description = "추천 받을 시간표의 학기 정보",
                    example = "2",
                    minimum = "1",
                    maximum = "2",
                    requiredMode = Schema.RequiredMode.REQUIRED
            )
            @Min(value = 1, message = "학기는 1 또는 2만 입력 가능합니다.")
            @Max(value = 2, message = "학기는 1 또는 2만 입력 가능합니다.")
            @NotNull(message = "학기 정보는 필수입니다.")
            Integer semester,

            @Schema(
                    description = "희망 공강 요일 (최소 0개 - 최대 2개)",
                    example = "[\"MON\", \"FRI\"]"
            )
            @Size(max = 2,message = "공강 요일은 최대 2개까지 선택할 수 있습니다.")
            List<Week> preferredFreeDays,

            @Schema(
                    description = "목표 학점 (null일시 백엔드에서 18학점으로 처리)",
                    example = "18",
                    minimum = "12",
                    maximum = "30",
                    requiredMode = Schema.RequiredMode.REQUIRED
            )
            @Min(value = 12, message = "목표 학점은 12학점 이상이어야 합니다.")
            @Max(value = 30, message = "목표 학점은 30학점을 초과할 수 없습니다.")
            Integer targetCredits,
            @Schema(
                    description = "사용자가 선택한 시간표에 포함할 강의 ID 목록",
                    example = "[13, 16, 17]"
            )
            List<Long> selectedLectureIds,
            @Schema(
                    description = "시간표 추천 기준 선택 FREE_PERIOD(공강 우선),LECTURE_CRITERIA(수강기준 우선)",
                    example = "FREE_PERIOD"
            )
            Priority priority
            ) {

            public createRecommendRequest {
                    selectedLectureIds = selectedLectureIds == null ? List.of() : selectedLectureIds;
                    preferredFreeDays = preferredFreeDays == null ? List.of() : preferredFreeDays;
            }

    }
}
