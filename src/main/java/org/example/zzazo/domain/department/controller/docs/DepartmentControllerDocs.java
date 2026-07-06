package org.example.zzazo.domain.department.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.zzazo.domain.department.dto.DepartmentResponse;
import org.example.zzazo.global.common.ApiResponse;

@Tag(name = "Department", description = "학과 관련 API")
public interface DepartmentControllerDocs {

    @Operation(
            summary = "학과 목록 조회",
            description = """
                    전체 학과 ID와 이름 목록을 조회합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "학과목록 조회 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                               "isSuccess": true,
                               "code": "COMMON_200_1",
                               "message": "요청 응답 성공",
                               "data": {
                                 "departments": [
                                   {
                                     "departmentId": 1,
                                     "departmentName": "경영학과"
                                   },
                                   {
                                     "departmentId": 2,
                                     "departmentName": "컴퓨터공학과"
                                   },
                                   {
                                     "departmentId": 3,
                                     "departmentName": "전자공학과"
                                   }
                                 ]
                               }
                             }
                            """))
            )
    })
    ApiResponse<DepartmentResponse.DepartmentList> getDepartmentList();
}
