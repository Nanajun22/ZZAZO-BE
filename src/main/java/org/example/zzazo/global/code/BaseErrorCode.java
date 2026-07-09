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
    TYPE_MISMATCH_ERROR(HttpStatus.BAD_REQUEST,"COMMON_400_3","허용되지 않은 값입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
