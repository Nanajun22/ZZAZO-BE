package org.example.zzazo.timetable.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.zzazo.timetable.dto.TimetableCreateRequest;
import org.example.zzazo.timetable.dto.TimetableCreateResponse;
import org.example.zzazo.timetable.dto.TimetableDetailResponse;
import org.example.zzazo.timetable.dto.TimetableListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Timetable", description = "시간표 저장, 목록 조회, 상세 조회, 삭제 API")
@RestController
@RequestMapping("/api/v1/timetables")
public class TimetableController {

    @Operation(
            summary = "시간표 저장",
            description = "추천 후보 시간표를 로그인한 사용자의 저장 목록에 저장합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "사용자가 저장할 추천 시간표 정보",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TimetableCreateRequest.class),
                    examples = @ExampleObject(value = """
                            {
                              "candidateName": "공강 조건 중심 시간표",
                              "departmentId": 1,
                              "semester": 2,
                              "grade": 1,
                              "preferredFreeDays": ["FRI", "WED"],
                              "targetCredits": 18,
                              "selectedLectureIds": [13, 17],
                              "totalCredits": 20
                            }
                            """)
            )
    )
    @ApiResponse(
            responseCode = "201",
            description = "시간표 저장 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TimetableCreateResponse.class),
                    examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": {
                                "timetableId": 1,
                                "message": "시간표가 저장되었습니다."
                              }
                            }
                            """)
            )
    )
    @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content)
    @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    @PostMapping
    public ResponseEntity<TimetableCreateResponse> createTimetable(
            @Valid @RequestBody TimetableCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TimetableCreateResponse(1L, "시간표가 저장되었습니다."));
    }

    @Operation(
            summary = "저장한 시간표 목록 조회",
            description = "로그인한 사용자가 저장한 시간표 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "시간표 목록 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TimetableListResponse.class),
                    examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": {
                                "timetables": [
                                  {
                                    "timetableId": 1,
                                    "candidateName": "공강 조건 중심 시간표",
                                    "departmentId": 1,
                                    "totalCredits": 20,
                                    "preferredFreeDays": ["FRI", "WED"],
                                    "createdAt": "2026-07-20T12:00:00"
                                  }
                                ]
                              }
                            }
                            """)
            )
    )
    @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    @GetMapping
    public ResponseEntity<TimetableListResponse> getTimetables() {
        return ResponseEntity.ok(TimetableListResponse.example());
    }

    @Operation(
            summary = "저장한 시간표 상세 조회",
            description = "저장한 시간표의 입력 조건, 과목 목록, 수강기준 점검 결과를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "시간표 상세 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TimetableDetailResponse.class),
                    examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": {
                                "timetableId": 1,
                                "candidateName": "공강 조건 중심 시간표",
                                "departmentId": 1,
                                "semester": 2,
                                "grade": 1,
                                "preferredFreeDays": ["FRI", "WED"],
                                "targetCredits": 18,
                                "selectedLectureIds": [13, 17],
                                "totalCredits": 20,
                                "requirementCheck": {
                                  "targetCreditSatisfied": true,
                                  "requiredCourseIncluded": true,
                                  "foundationCourseIncluded": true,
                                  "generalEducationIncluded": true
                                },
                                "courses": [
                                  {
                                    "lectureId": 13,
                                    "lectureName": "경영학원론",
                                    "section": "001",
                                    "professor": "홍길동",
                                    "credit": 3,
                                    "lectureClassification": "전공필수",
                                    "dayOfWeek": "MON",
                                    "startTime": "09:00",
                                    "endTime": "10:15",
                                    "classroom": "가천관 000호"
                                  }
                                ]
                              }
                            }
                            """)
            )
    )
    @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    @ApiResponse(responseCode = "403", description = "다른 사용자의 시간표 접근", content = @Content)
    @ApiResponse(responseCode = "404", description = "시간표 없음", content = @Content)
    @GetMapping("/{timetableId}")
    public ResponseEntity<TimetableDetailResponse> getTimetable(
            @Parameter(description = "저장 시간표 ID", example = "1")
            @PathVariable Long timetableId
    ) {
        return ResponseEntity.ok(TimetableDetailResponse.example(timetableId));
    }

    @Operation(
            summary = "저장한 시간표 삭제",
            description = "로그인한 사용자가 저장한 시간표를 삭제합니다."
    )
    @ApiResponse(responseCode = "204", description = "시간표 삭제 성공", content = @Content)
    @ApiResponse(responseCode = "401", description = "인증 필요", content = @Content)
    @ApiResponse(responseCode = "403", description = "다른 사용자의 시간표 접근", content = @Content)
    @ApiResponse(responseCode = "404", description = "시간표 없음", content = @Content)
    @DeleteMapping("/{timetableId}")
    public ResponseEntity<Void> deleteTimetable(
            @Parameter(description = "저장 시간표 ID", example = "1")
            @PathVariable Long timetableId
    ) {
        return ResponseEntity.noContent().build();
    }
}
