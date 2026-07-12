package org.example.zzazo.domain.lecture.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.example.zzazo.domain.lecture.domain.LectureClassification;
import org.example.zzazo.domain.lecture.domain.LiberalCategory;
import org.example.zzazo.global.common.Week;
import java.time.LocalTime;
import java.util.List;

public class LectureResponse {
    //강의 목록조회 DTO
    @Builder
    @Schema(name = "LectureListResponse")
    public record LectureList(
            @Schema(description = "강의 목록")
            List<Lecture> lectures
    ) {
    }

    @Schema(description = "강의 목록 조회 응답")
    @Builder
    public record Lecture(
            @Schema(description = "강의 ID",example = "1")
            Long lectureId,
            @Schema(description = "강의명",example = "경영학원론")
            String lectureName,
            @Schema(description = "학점",example = "3")
            int credit,
            @Schema(description = "강의 분류 [전공필수,전공선택,교양필수,교양선택]",example = "전공필수")
            LectureClassification lectureClassification,
            @Schema(description = "강의시간")
            List<LectureTime> lectureTime
    ) {

    }

    @Builder
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

    @Builder
    @Schema(description = "교양 과목 세부구분 목록 조회 응답")
    public record LiberalCategoryList(List<Category> liberalCategoryList) {
        @Schema(description = "교양강의 세부 구분")
        public record Category(

                @Schema(description = "값", example = "COMMUNICATION")
                LiberalCategory liberalCategory,
                @Schema(description = "이름", example = "의사소통")
                String name) {

            public static Category from(LiberalCategory liberalCategory) {
                return new Category(liberalCategory, liberalCategory.getValue());
            }

        }
    }
}