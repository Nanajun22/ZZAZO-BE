package org.example.zzazo.domain.user.controller;

import jakarta.validation.Valid;
import org.example.zzazo.domain.user.controller.docs.AuthControllerDocs;
import org.example.zzazo.domain.user.dto.request.EmailVerificationConfirmRequestDto;
import org.example.zzazo.domain.user.dto.request.EmailVerificationSendRequestDto;
import org.example.zzazo.domain.user.dto.request.LoginRequestDto;
import org.example.zzazo.domain.user.dto.request.SignUpRequestDto;
import org.example.zzazo.domain.user.dto.response.LoginResponseDto;
import org.example.zzazo.domain.user.dto.response.SignUpResponseDto;
import org.example.zzazo.global.code.BaseSuccessCode;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
// 인증 관련 API Controller
public class AuthController implements AuthControllerDocs {

    // 이메일 인증번호 발송
    @Override
    @PostMapping("/email/send")
    public ResponseEntity<ApiResponse<Void>> sendEmailVerification(
            @Valid @RequestBody EmailVerificationSendRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(BaseSuccessCode.GENERAL_OK));
    }

    // 이메일 인증번호 확인
    @Override
    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmailCode(
            @Valid @RequestBody EmailVerificationConfirmRequestDto request) {
        return ResponseEntity.ok(ApiResponse.success(BaseSuccessCode.GENERAL_OK));
    }

    // 회원가입
    @Override
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponseDto>> signUp(
            @Valid @RequestBody SignUpRequestDto request) {
        SignUpResponseDto response = SignUpResponseDto.builder()
                .userId(1L)
                .email(request.getEmail())
                .grade(request.getGrade())
                .departmentId(request.getDepartmentId())
                .studentId(request.getStudentId())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(BaseSuccessCode.GENERAL_CREATED, response));
    }

    // 로그인
    @Override
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto request) {
        LoginResponseDto response = LoginResponseDto.builder()
                .userId(1L)
                .email(request.getEmail())
                .build();
        return ResponseEntity.ok(ApiResponse.success(BaseSuccessCode.GENERAL_OK, response));
    }

    // 로그아웃
    @Override
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.success(BaseSuccessCode.GENERAL_OK));
    }
}
