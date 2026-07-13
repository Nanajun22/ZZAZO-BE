package org.example.zzazo.domain.recommend.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.zzazo.domain.recommend.dto.RecommendRequest;
import org.example.zzazo.domain.recommend.dto.RecommendResponse;
import org.example.zzazo.global.common.ApiResponse;

@Tag(name = "Recommend", description = "시간표 추천 관련 API")
public interface RecommendControllerDocs {

    @Operation(
            summary = "조건에 맞는 시간표 추천",
            description = """
                    사용자는 조건에 맞는 시간표를 추천 받습니다. \n
                    
                    [추천 시간표 생성 흐름] \n
                    1단계 - 사용자가 기본 정보를 입력합니다(학과,학년,학기) \n
                    2단계 - 사용자가 공강 요일을 선택합니다(최대 2개/nullable) \n
                    3단계 - 사용자가 목표 학점을 선택합니다.(기본 18학점) \n
                    4단계 - 사용자가 과목 목록에서 듣고 싶은 과목을 선택합니다.(nullable) \n
                    5단계 - 사용자가 조건에 맞는 시간표를 추천 받습니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "사용자가 설정한 시간표의 조건",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "departmentId": 1,
                      "grade": 1,
                      "semester": 2,
                      "preferredFreeDays": [],
                      "targetCredits": 18,
                      "selectedLectureIds": [13,17],
                      "priority": "FREE_PERIOD"
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "조건에 맞는 시간표 추천 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecommendResponse.RecommendResult.class),
                            examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": {
                                "totalCredits": 20,
                                "preferredFreeDays": ["FRI"],
                                "timetables": [
                                  {
                                    "lectureId": 13,
                                    "lectureName": "경영학원론",
                                    "credit": 3,
                                    "professor": "홍길동",
                                    "classroom": "가천관612",
                                    "lectureClassification": "전공필수",
                                    "lectureTime": [
                                      {
                                        "dayOfWeek": "MON",
                                        "startTime": "09:00",
                                        "endTime": "10:15"
                                      },
                                      {
                                        "dayOfWeek": "WED",
                                        "startTime": "09:00",
                                        "endTime": "10:15"
                                      }
                                    ]
                                  },
                                  {
                                    "lectureId": 17,
                                    "lectureName": "경제학원론",
                                    "credit": 3,
                                    "professor": "홍길동",
                                    "classroom": "가천관612",
                                    "lectureClassification": "전공선택",
                                    "lectureTime": [
                                      {
                                        "dayOfWeek": "WED",
                                        "startTime": "13:00",
                                        "endTime": "16:00"
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
                            [RECOMMEND_400_1] \n
                            요청으로 들어온 학기 정보와 다른 학기 정보를 가지고 있는 강의 ID를 선택한 케이스입니다. \n
                            [RECOMMEND_400_2] \n
                            선택한 강의 목록에 중복된 강의 ID가 존재하는 케이스입니다.\n
                            [RECOMMEND_400_3] \n
                            선택한 강의 목록에 속한 강의들의 시간표가 겹치는 케이스입니다. \n
                            [RECOMMEND_400_4] \n
                            선택한 강의 목록에 속한 강의들의 학점이 30을 초과하는 케이스입니다. \n
                            [VALIDATION_400_1] \n
                            사용자가 입력한 값이 유효하지 않은 케이스입니다. \n
                            
                            
                            """,
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "RECOMMEND_400_1", value = """
                                            {
                                              "isSuccess": false,
                                              "code": "RECOMMEND_400_1",
                                              "message": "선택한 강의 정보가 올바르지 않습니다.",
                                              "data": null
                                            }
                                            """),
                                    @ExampleObject(name = "RECOMMEND_400_2", value = """
                                            {
                                              "isSuccess": false,
                                              "code": "RECOMMEND_400_2",
                                              "message": "동일한 과목을 중복 선택할 수 없습니다.",
                                              "data": null
                                            }
                                            """),
                                    @ExampleObject(name = "RECOMMEND_400_3", value = """
                                            {
                                              "isSuccess": false,
                                              "code": "RECOMMEND_400_3",
                                              "message": "선택할 수 없는 과목입니다.",
                                              "data": null
                                            }
                                            """),
                                    @ExampleObject(name = "RECOMMEND_400_4", value = """
                                            {
                                              "isSuccess": false,
                                              "code": "RECOMMEND_400_4",
                                              "message": "선택한 과목은 30학점을 초과할 수 없습니다.",
                                              "data": null
                                            }
                                            """),
                                    @ExampleObject(name = "VALIDATION_400_1", value = """
                                            {
                                              "isSuccess": false,
                                              "code": "VALIDATION_400_1",
                                              "message": "입력값이 올바르지 않습니다.",
                                              "data": {
                                                "preferredFreeDays": "공강 요일은 최대 2개까지 선택할 수 있습니다.",
                                                "targetCredits": "목표 학점은 30학점을 초과할 수 없습니다."
                                              }
                                            }
                                            """)
                            }
                    )
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = """
                            [RECOMMEND_404_1] \n
                            사용자가 입력한 학과가 존재하지 않는 케이스입니다. \n
                            [RECOMMEND_404_2] \n
                            사용자가 입력한 강의가 존재하지 않는 케이스입니다. \n
                            [RECOMMEND_404_3] \n
                            사용자가 선택한 조건에 맞는 시간표가 존재하지 않는 케이스입니다. \n
                            """,
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "RECOMMEND_404_1", value = """
                                            {
                                              "isSuccess": false,
                                              "code": "RECOMMEND_404_1",
                                              "message": "존재하지 않는 학과입니다.",
                                              "data": null
                                            }
                                            """),
                                    @ExampleObject(name = "RECOMMEND_404_2", value = """
                                            {
                                              "isSuccess": false,
                                              "code": "RECOMMEND_404_2",
                                              "message": "존재하지 않는 강의입니다.",
                                              "data": null
                                            }
                                            """),
                                    @ExampleObject(name = "RECOMMEND_404_3", value = """
                                            {
                                              "isSuccess": false,
                                              "code": "RECOMMEND_404_3",
                                              "message": "조건에 맞는 시간표가 존재하지 않습니다.",
                                              "data": null
                                            }
                                            """)

                            }

                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = """
                            [RECOMMEND_500_1] \n
                            시간표 추천 로직이 제한시간을 초과한 케이스입니다. \n
                            """,
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "RECOMMEND_500_1",
                              "message": "시간표 추천 시간이 초과되었습니다. 추천 조건을 추가하여 다시 시도해 주세요.",
                              "data": null
                            }
                            """))
            )

    })
    ApiResponse<RecommendResponse.RecommendResult> recommendTimetable(RecommendRequest.createRecommendRequest request);
}
