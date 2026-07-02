package org.example.zzazo.global.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.zzazo.global.common.ApiResponse;
import org.example.zzazo.global.common.HealthResponseDto;
import org.springframework.http.ResponseEntity;

// 서버 상태 확인 API의 Swagger 문서 정의
@Tag(name = "Health", description = "서버 상태 확인 API")
public interface HealthControllerDocs {

    @Operation(
            summary = "헬스 체크",
            description = """
                    서버가 정상적으로 실행 중인지 확인합니다.

                    별도의 요청 파라미터 없이 호출하며, 서버가 응답 가능한 상태이면 status에 "UP"을 반환합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "서버 정상 실행 중",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "status": 200,
                              "message": "success",
                              "data": {
                                "status": "UP",
                                "message": "Server is running"
                              }
                            }
                            """))
            )
    })
    ResponseEntity<ApiResponse<HealthResponseDto>> healthCheck();
}
