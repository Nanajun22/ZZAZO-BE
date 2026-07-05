package org.example.zzazo.timetable.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시간표에 포함된 과목 정보")
public record TimetableCourseResponse(
        @Schema(description = "강의 ID", example = "13")
        Long lectureId,

        @Schema(description = "과목명", example = "경영학원론")
        String lectureName,

        @Schema(description = "분반", example = "001")
        String section,

        @Schema(description = "교수명", example = "홍길동")
        String professor,

        @Schema(description = "학점", example = "3")
        int credit,

        @Schema(description = "이수구분", example = "전공필수")
        String lectureClassification,

        @Schema(description = "요일", example = "MON")
        String dayOfWeek,

        @Schema(description = "시작 시간", example = "09:00")
        String startTime,

        @Schema(description = "종료 시간", example = "10:15")
        String endTime,

        @Schema(description = "강의실", example = "가천관 000호")
        String classroom
) {

    public static TimetableCourseResponse example() {
        return new TimetableCourseResponse(
                13L,
                "경영학원론",
                "001",
                "홍길동",
                3,
                "전공필수",
                "MON",
                "09:00",
                "10:15",
                "가천관 000호"
        );
    }
}
