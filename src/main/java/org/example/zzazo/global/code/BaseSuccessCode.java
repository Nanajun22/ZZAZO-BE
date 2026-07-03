package org.example.zzazo.global.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.global.common.BaseCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BaseSuccessCode implements BaseCode {

    GENERAL_OK(HttpStatus.OK,"COMMON_200_1","요청 응답 성공"),
    GENERAL_CREATED(HttpStatus.CREATED,"COMMON_201_1","요청 리소스 생성 성공");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
