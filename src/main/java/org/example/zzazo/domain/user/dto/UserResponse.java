package org.example.zzazo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

// user 도메인 Response DTO 모음
public class UserResponse {

    // 로그인 응답 정보
    @Builder
    public record LoginResponse(
            @Schema(example = "1")
            Long userId,

            @Schema(example = "student@university.ac.kr")
            String email
    ) {
    }

    // 회원가입 응답 정보
    @Builder
    public record SignUpResponse(
            @Schema(example = "1")
            Long userId,

            @Schema(example = "student@university.ac.kr")
            String email,

            @Schema(example = "2")
            int grade,

            @Schema(example = "3")
            Long departmentId,

            @Schema(example = "20210001")
            Long studentId
    ) {
    }
}
