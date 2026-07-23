package org.example.zzazo.domain.timetable.controller;

import jakarta.validation.Valid;
import org.example.zzazo.domain.timetable.controller.docs.TimetableControllerDocs;
import org.example.zzazo.domain.timetable.dto.TimetableCreateRequest;
import org.example.zzazo.domain.timetable.dto.TimetableCreateResponse;
import org.example.zzazo.domain.timetable.dto.TimetableDetailResponse;
import org.example.zzazo.domain.timetable.dto.TimetableListResponse;
import org.example.zzazo.domain.timetable.service.TimetableService;
import org.example.zzazo.global.code.BaseSuccessCode;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/timetables")
public class TimetableController implements TimetableControllerDocs {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<TimetableCreateResponse>> createTimetable(
            @Valid @RequestBody TimetableCreateRequest request
    ) {
        TimetableCreateResponse response = timetableService.createTimetable(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(BaseSuccessCode.GENERAL_CREATED, response));
    }

    @Override
    @GetMapping
    public ApiResponse<TimetableListResponse> getTimetables() {
        TimetableListResponse response = timetableService.getTimetables();
        return ApiResponse.success(BaseSuccessCode.GENERAL_OK, response);
    }

    @Override
    @GetMapping("/{timetableId}")
    public ApiResponse<TimetableDetailResponse> getTimetable(
            @PathVariable Long timetableId
    ) {
        return ApiResponse.success(BaseSuccessCode.GENERAL_OK, TimetableDetailResponse.example(timetableId));
    }

    @Override
    @DeleteMapping("/{timetableId}")
    public ResponseEntity<Void> deleteTimetable(
            @PathVariable Long timetableId
    ) {
        return ResponseEntity.noContent().build();
    }
}
