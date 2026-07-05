package org.example.zzazo.global.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

// 서버 상태 확인 API의 Swagger 문서 정의
@Tag(name = "Health", description = "서버 상태 확인 API")
public interface HealthControllerDocs {

    @Operation(
            summary = "헬스 체크",
            description = """
                    서버가 정상적으로 실행 중인지 확인합니다.

                    별도의 요청 파라미터 없이 호출하며, 서버가 정상적으로 응답 가능한 상태이면 응답 바디 없이 200 OK만 반환합니다.
                    """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "서버 정상 실행 중 (응답 바디 없음)"
            )
    })
    ResponseEntity<Void> healthCheck();
}
