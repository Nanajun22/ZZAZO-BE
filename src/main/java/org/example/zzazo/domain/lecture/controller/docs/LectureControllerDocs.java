package org.example.zzazo.domain.lecture.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.zzazo.domain.lecture.domain.LiberalCategory;
import org.example.zzazo.domain.lecture.dto.LectureResponse;
import org.example.zzazo.global.common.ApiResponse;

@Tag(name = "Lecture", description = "강의 API (강의목록 조회)")
public interface LectureControllerDocs {

    @Operation(
            summary = "전공 강의목록 조회",
            description = """
                    
                    사용자는 전공강의 목록을 조회합니다.
                  
                    [강의목록 조회 흐름] \n
                    1단계 - 사용자가 전공 강의를 선택합니다. \n
                    2단계 - 사용자가 학과를 선택합니다. \n
                    3단계 - GET /api/v1/lectures/major API를 호출합니다. \n
                    4단계 - 시스템이 사용자가 선택한 학과의 전공 강의목록를 반환합니다.
                    

                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "과목목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LectureResponse.LectureList.class),
                            examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": {
                                "lectures": [
                                  {
                                    "lectureId": 1,
                                    "lectureName": "경영학원론",
                                    "lectureClassification": "전공필수",
                                    "credit": 3,
                                    "lectureTime": [
                                      {
                                        "startTime": "10:30",
                                        "endTime": "11:45",
                                        "dayOfWeek": "MON"
                                      },
                                      {
                                        "startTime": "10:30",
                                        "endTime": "11:45",
                                        "dayOfWeek": "WED"
                                      }
                                    ]
                                  }
                                ]
                              }
                            }
                            """))
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = """
                            사용자가 잘못된 입력값을 입력한다. \n
                            구체적인 케이스는 다음과 같다. \n
                         
                            1.학기 정보 미입력 \n
                            2.학기 범위 오류 (ex. -1 or 3) \n
                            3.학과 정보 미입력 \n
                            """,
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "VALIDATION_400_1",
                              "message": "입력값이 올바르지 않습니다.",
                              "data": {
                                "semester": "학기 정보는 필수입니다.",
                                "departmentId": "학과 정보는 필수입니다."
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = """
                            사용자가 입력한 학과가 존재하지 않는 케이스입니다.
                            """,
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "LECTURE_404_1",
                              "message": "존재하지 않는 학과입니다.",
                              "data": null
                            }
                            """))
            )
    })
    ApiResponse<LectureResponse.LectureList> getMajorList(
            @Parameter(
                description = "사용자가 조회하고자 하는 전공 과목목록의 학과 id 입니다.",
                example = "1",
                required = true
            )

            @NotNull @Positive
            Long departmentId,
            @Parameter(
                    description = "사용자가 조회하고자 하는 과목 목록 학기 정보입니다. STEP 1.의 기본정보 확인에서 입력한 학기 정보를 사용합니다. \n" +
                            "가능한 값 : 1,2",
                    example = "2",
                    required = true
            )
            @NotNull
            @Min(1) @Max(2)
            Integer semester
    );



    @Operation(
            summary = "교양 강의목록 조회",
            description = """
                    
                    사용자는 교양 강의목록을 조회합니다.
                    
                    [강의목록 조회 흐름] \n
                    1단계 - 사용자가 교양을 선택합니다. \n
                    2단계 - 사용자가 세부 교양분류를 선택합니다. \n
                    3단계 - GET /api/v1/lectures/liberal API를 호출합니다. \n
                    4단계 - 시스템이 선택한 분류의 교양 강의목록를 반환합니다.

                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "과목목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LectureResponse.LectureList.class),
                            examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": {
                                "lectures": [
                                  {
                                    "lectureId": 101,
                                    "lectureName": "College English2",
                                    "lectureClassification": "교양필수",
                                    "credit": 2,
                                    "lectureTime": [
                                      {
                                        "startTime": "09:00",
                                        "endTime": "11:00",
                                        "dayOfWeek": "MON"
                                      }
                                    ]
                                  }
                                ]
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = """
                            [VALIDATION_400_1] \n
                            사용자가 잘못된 입력값을 입력한다. \n
                            구체적인 케이스는 다음과 같다. \n
                            \n
                            1.학기 정보 미입력 \n
                            2.학기 범위 오류 (ex. -1 or 3) \n
                            3.교양 분류 정보 미입력 \n
                            \n
                            [TYPE_MISMATCH_400_1] \n
                            정의되지 않은 교양 분류 정보 값을 입력한다. \n
                            1.지원하지 않는 타입의 교양 구분 정보 입력
                            """,
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "VALIDATION_400_1", value = """
                            {
                              "isSuccess": false,
                              "code": "VALIDATION_400_1",
                              "message": "입력값이 올바르지 않습니다.",
                              "data": {
                                "semester": "학기 정보는 필수입니다.",
                                "liberalCategory": "교양 분류 정보는 필수입니다."
                              }
                            }
                            """),
                            @ExampleObject(name = "TYPE_MISMATCH_400_1", value = """
                            {
                              "isSuccess": false,
                              "code": "TYPE_MISMATCH_400_1",
                              "message": "허용되지 않은 값입니다.",
                              "data": {
                                "field": "liberalCategory",
                                "invalidValue": "ABC"
                              }
                            }
                            """)}
                    )
            )
    })
    ApiResponse<LectureResponse.LectureList> getLiberalList(
            @Parameter(
                    description = """
                            사용자가 조회하고자 하는 교양 과목의 세부 영역입니다.
                            
                            **MVP에서 사용할 값**
                            - `COMMUNICATION` : 의사소통
                            - `AI_BASIC` : AI기초
                            - `GACHON_VISION` : 가천비전
                            """,
                    example = "COMMUNICATION",
                    required = true
            )

            LiberalCategory liberalCategory,
            @Parameter(
                    description = "사용자가 조회하고자 하는 과목 목록 학기 정보입니다. STEP 1.의 기본정보 확인에서 입력한 학기 정보를 사용합니다. \n" +
                            "가능한 값 : 1,2",
                    example = "2",
                    required = true
            )
            @NotNull
            @Min(1) @Max(2)
            Integer semester
    );


    @Operation(
            summary = "교양 세부 구분목록 조회",
            description = """
                    교양 과목의 세부 구분목록을 조회합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "교양 세부 구분목록 조회 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": {
                                "categories": [
                                  {
                                    "code": "COMMUNICATION",
                                    "name": "의사소통"
                                  },
                                  {
                                    "code": "AI_BASIC",
                                    "name": "AI기초"
                                  },
                                  {
                                    "code": "GACHON_VISION",
                                    "name": "가천비전"
                                  }
                                ]
                              }
                            }
                            """))
            )
    })
    ApiResponse<LectureResponse.LiberalCategoryList> getLiberalCategoryList();

}
