package org.example.zzazo.domain.recommend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.domain.recommend.controller.docs.RecommendControllerDocs;
import org.example.zzazo.domain.recommend.dto.RecommendRequest;
import org.example.zzazo.domain.recommend.dto.RecommendResponse;
import org.example.zzazo.domain.recommend.service.RecommendService;
import org.example.zzazo.global.code.BaseSuccessCode;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/timetables")
@RestController
@RequiredArgsConstructor
public class RecommendController implements RecommendControllerDocs {

    private final RecommendService recommendService;

    @PostMapping("/recommend")
    public ApiResponse<RecommendResponse.RecommendResult> recommendTimetable(
            @RequestBody @Valid RecommendRequest.createRecommendRequest request) {
        RecommendResponse.RecommendResult response = recommendService.recommendTimeTable(request);
        return ApiResponse.success(BaseSuccessCode.GENERAL_OK,response);
    }
}
