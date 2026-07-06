package org.example.zzazo.domain.recommend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.zzazo.domain.lecture.domain.LectureClassification;
import org.example.zzazo.global.common.Week;

import java.time.LocalTime;
import java.util.List;

public class RecommendResponse {

    @Schema(description = "시간표 추천 결과")
    public record RecommendResult(
            @Schema(description = "추천 시간표의 총 학점", example = "18")
            int totalCredits,
            @Schema(
                    description = "추천 결과 시간표 공강 요일",
                    example = "[\"FRI\"]"
            )
            List<Week> satisfiedFreeDays,
            @Schema(description = "추천 시간표의 강의 목록")
            List<Lecture> timetables
    ) {

    }

    public record Lecture(
            @Schema(description = "강의 ID",example = "1")
            Long lectureId,
            @Schema(description = "강의명",example = "경영학원론")
            String lectureName,
            @Schema(description = "학점",example = "3")
            int credit,
            @Schema(description = "담당교수", example = "홍길동")
            String professor,
            @Schema(description = "강의실", example = "가천관612")
            String classroom,
            @Schema(description = "강의 분류 [전공필수,전공선택,교양필수,교양선택]",example = "전공필수")
            LectureClassification lectureClassification,
            @Schema(description = "강의시간")
            List<LectureTime> lectureTime
            ) {
        public record LectureTime(
                @Schema(description = "강의 시작시간",example = "13:00")
                @JsonFormat(pattern = "HH:mm")
                LocalTime startTime,
                @Schema(description = "강의 종료시간",example = "14:00")
                @JsonFormat(pattern = "HH:mm")
                LocalTime endTime,
                @Schema(description = "강의 요일",example = "MON")
                Week dayOfWeek
        ) {

        }
    }


}
