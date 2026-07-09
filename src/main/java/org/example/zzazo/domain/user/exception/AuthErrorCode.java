package org.example.zzazo.domain.user.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.global.common.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AuthErrorCode implements BaseCode {

    //AUTH 에러 코드
    INVALID_SCHOOL_EMAIL(HttpStatus.BAD_REQUEST,"AUTH_400_1","가천대학교 이메일(@gachon.ac.kr)만 사용할 수 있습니다."),
    VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST,"AUTH_400_2","인증번호가 일치하지 않습니다."),
    EMAIL_VERIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST,"AUTH_400_5","인증 요청 기록이 없습니다. 이메일 인증을 다시 요청해주세요."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST,"AUTH_400_6","인증번호가 만료되었습니다. 인증번호를 다시 요청해주세요."),
    EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN,"AUTH_403_1","이메일 인증이 완료되지 않았습니다."),
    EMAIL_ALREADY_REGISTERED(HttpStatus.CONFLICT,"AUTH_409_1","이미 존재하는 이메일입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED,"AUTH_401_1","이메일 또는 비밀번호가 일치하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"AUTH_401_2","리프레시 토큰이 만료되었습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,"AUTH_401_3","유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED,"AUTH_401_4","이미 로그아웃되었거나 존재하지 않는 리프레시 토큰입니다."),
    TOKEN_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED,"AUTH_401_5","토큰에 해당하는 사용자를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
