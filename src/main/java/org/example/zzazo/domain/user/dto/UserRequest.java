package org.example.zzazo.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// user 도메인 Request DTO 모음
public class UserRequest {

    // 이메일 인증번호 발송 요청 정보
    public record EmailVerificationSendRequest(
            @Schema(
                    description = "가천대학교 이메일 주소. 도메인은 반드시 @gachon.ac.kr 이어야 합니다.",
                    example = "student@gachon.ac.kr"
            )
            @NotBlank
            @Email
            @Pattern(
                    regexp = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@gachon\\.ac\\.kr$",
                    message = "가천대학교 이메일(@gachon.ac.kr)만 사용할 수 있습니다."
            )
            String email
    ) {
    }

    // 이메일 인증번호 확인 요청 정보
    public record EmailVerificationConfirmRequest(
            @Schema(example = "student@university.ac.kr")
            @NotBlank
            @Email
            String email,

            @Schema(example = "482910")
            @NotBlank
            @Size(min = 6, max = 6)
            String verificationCode
    ) {
    }

    // 회원가입 요청 정보
    public record SignUpRequest(
            @Schema(example = "3")
            @NotNull
            Long departmentId,

            @Schema(example = "20210001")
            @NotNull
            Long studentId,

            @Schema(
                    description = "인증 완료된 가천대학교 이메일(@gachon.ac.kr). 사용자가 재입력하는 값이 아니라 회원가입 화면에서 인증 완료된 이메일 값을 그대로 포함해 전송합니다.",
                    example = "student@gachon.ac.kr"
            )
            @NotBlank
            @Email
            @Pattern(
                    regexp = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@gachon\\.ac\\.kr$",
                    message = "가천대학교 이메일(@gachon.ac.kr)만 사용할 수 있습니다."
            )
            String email,

            @Schema(example = "password123!")
            @NotBlank
            @Size(min = 8, max = 20)
            String password,

            @Schema(example = "2")
            @NotNull
            @Min(1)
            @Max(4)
            Integer grade
    ) {
    }

    // 로그인 요청 정보
    public record LoginRequest(
            @Schema(example = "student@university.ac.kr")
            @NotBlank
            @Email
            String email,

            @Schema(example = "password123!")
            @NotBlank
            String password
    ) {
    }
}
