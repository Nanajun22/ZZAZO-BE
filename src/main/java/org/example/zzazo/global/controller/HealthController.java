package org.example.zzazo.global.controller;

import org.example.zzazo.global.code.BaseSuccessCode;
import org.example.zzazo.global.common.ApiResponse;
import org.example.zzazo.global.common.HealthResponseDto;
import org.example.zzazo.global.controller.docs.HealthControllerDocs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
// 서버 상태 확인 API Controller
public class HealthController implements HealthControllerDocs {

    // 서버 상태 확인
    @Override
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<HealthResponseDto>> healthCheck() {
        HealthResponseDto data = HealthResponseDto.builder()
                .status("UP")
                .message("Server is running")
                .build();
        return ResponseEntity.ok(ApiResponse.success(BaseSuccessCode.GENERAL_OK, data));
    }
}
