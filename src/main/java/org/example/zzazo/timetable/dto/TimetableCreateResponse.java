package org.example.zzazo.timetable.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시간표 저장 응답")
public record TimetableCreateResponse(
        @Schema(description = "저장 시간표 ID", example = "1")
        Long timetableId,

        @Schema(description = "응답 메시지", example = "시간표가 저장되었습니다.")
        String message
) {
}
