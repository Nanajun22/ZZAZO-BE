package org.example.zzazo.global.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.global.common.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BaseErrorCode implements BaseCode {


    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON_500_1","서버 에러가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON_400_1","잘못된 요청입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST,"COMMON_400_2","입력값이 올바르지 않습니다."),
    TYPE_MISMATCH_ERROR(HttpStatus.BAD_REQUEST,"COMMON_400_3","허용되지 않은 값입니다."),

    // Auth
    INVALID_SCHOOL_EMAIL(HttpStatus.BAD_REQUEST,"AUTH_400_1","가천대학교 이메일(@gachon.ac.kr)만 사용할 수 있습니다."),
    VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST,"AUTH_400_2","인증번호가 일치하지 않습니다."),
    EMAIL_VERIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST,"AUTH_400_5","인증 요청 기록이 없습니다. 이메일 인증을 다시 요청해주세요."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST,"AUTH_400_6","인증번호가 만료되었습니다. 인증번호를 다시 요청해주세요."),
    EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN,"AUTH_403_1","이메일 인증이 완료되지 않았습니다."),
    EMAIL_ALREADY_REGISTERED(HttpStatus.CONFLICT,"AUTH_409_1","이미 존재하는 이메일입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED,"AUTH_401_1","이메일 또는 비밀번호가 일치하지 않습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
