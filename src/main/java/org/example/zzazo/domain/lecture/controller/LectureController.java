package org.example.zzazo.domain.lecture.controller;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.domain.lecture.controller.docs.LectureControllerDocs;
import org.example.zzazo.domain.lecture.domain.LiberalCategory;
import org.example.zzazo.domain.lecture.dto.LectureResponse;
import org.example.zzazo.domain.lecture.service.LectureService;
import org.example.zzazo.global.code.BaseSuccessCode;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lectures")
@Validated
public class LectureController implements LectureControllerDocs {

    private final LectureService lectureService;

    //전공 과목목록 조회 API
    @Override
    @GetMapping("/major")
    public ApiResponse<LectureResponse.LectureList> getMajorList(
            @NotNull @Positive
            @RequestParam Long departmentId,

            @Min(1) @Max(2)
            @NotNull
            @RequestParam Integer semester
    ) {
        LectureResponse.LectureList response = lectureService.getMajorList(departmentId,semester);
        return ApiResponse.success(BaseSuccessCode.GENERAL_OK,response);
    }


    //교양 과목목록 조회 API
    @Override
    @GetMapping("/liberal")
    public ApiResponse<LectureResponse.LectureList> getLiberalList(
            @RequestParam LiberalCategory liberalCategory,

            @Min(1) @Max(2)
            @NotNull
            @RequestParam Integer semester
    ) {
        LectureResponse.LectureList response = lectureService.getLiberalList(liberalCategory,semester);
        return ApiResponse.success(BaseSuccessCode.GENERAL_OK,response);
    }

    //교양 과목 세부 구분목록 조회 API
    @Override
    @GetMapping("/liberal-categories")
    public ApiResponse<LectureResponse.LiberalCategoryList> getLiberalCategoryList() {
        return ApiResponse.success(BaseSuccessCode.GENERAL_OK);
    }
}
