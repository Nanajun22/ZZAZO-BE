package org.example.zzazo.domain.user.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.zzazo.domain.user.dto.UserRequest;
import org.example.zzazo.domain.user.dto.UserResponse;
import org.example.zzazo.global.common.ApiResponse;
import org.springframework.http.ResponseEntity;

// Auth 관련 API의 Swagger 문서 정의
@Tag(name = "Auth", description = "인증 API (이메일 인증, 회원가입, 로그인, 로그아웃)")
public interface AuthControllerDocs {

    @Operation(
            summary = "이메일 인증번호 발송 (회원가입 1단계)",
            description = """
                    회원가입 화면에서 사용자가 입력한 가천대학교 이메일로 6자리 인증번호를 발송합니다.

                    이메일 도메인은 반드시 @gachon.ac.kr 이어야 합니다.
                    학교 이메일 형식이 아니면 인증번호를 발송하지 않습니다.

                    [회원가입 전체 흐름]
                    1단계 - 가천대학교 이메일 입력 후 인증번호 발송 (현재 API)
                    2단계 - 이메일 인증번호 확인 (/api/auth/email/verify)
                    3단계 - 나머지 정보 입력 후 최종 회원가입 (/api/auth/signup)

                    이 API만으로는 회원가입이 완료되지 않습니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "인증번호를 발송할 가천대학교 이메일 정보",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "email": "student@gachon.ac.kr"
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "인증번호 발송 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (학교 이메일이 아니거나 이메일 형식 오류)",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_400_1",
                              "message": "가천대학교 이메일(@gachon.ac.kr)만 사용할 수 있습니다.",
                              "data": null
                            }
                            """))
            )
    })
    ResponseEntity<ApiResponse<Void>> sendEmailVerification(UserRequest.EmailVerificationSendRequest request);

    @Operation(
            summary = "이메일 인증번호 확인 (회원가입 2단계)",
            description = """
                    회원가입 화면에서 입력한 이메일과 인증번호를 검증합니다.

                    인증 성공 시 해당 이메일은 회원가입 가능한 인증 완료 상태가 됩니다.
                    이후 회원가입 API(/api/auth/signup) 호출 시 동일한 이메일을 사용해야 합니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "인증을 확인할 이메일과 발송받은 6자리 인증번호",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "email": "student@gachon.ac.kr",
                      "verificationCode": "482910"
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "인증 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "인증 실패 또는 잘못된 요청 (예: 인증번호 불일치)",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_400_2",
                              "message": "인증번호가 일치하지 않습니다.",
                              "data": null
                            }
                            """))
            )
    })
    ResponseEntity<ApiResponse<Void>> verifyEmailCode(UserRequest.EmailVerificationConfirmRequest request);

    @Operation(
            summary = "회원가입 (회원가입 3단계)",
            description = """
                    이메일 인증 완료 후 최종 회원가입을 진행합니다.

                    요청의 email 필드는 사용자가 직접 다시 입력하는 값이 아닙니다.
                    회원가입 화면에서 이미 인증 완료된 가천대학교 이메일(@gachon.ac.kr) 값을 그대로 포함해 전송합니다.

                    백엔드는 해당 이메일이 인증 완료 상태인지 확인한 뒤 가입을 처리합니다.
                    이메일 인증이 완료되지 않았거나 학교 이메일(@gachon.ac.kr)이 아닌 경우 가입이 거부됩니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "회원가입에 필요한 사용자 정보. email은 인증 완료된 값이어야 합니다.",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "email": "student@gachon.ac.kr",
                      "password": "password123!",
                      "grade": 2,
                      "departmentId": 3,
                      "studentId": 20210001
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_201_1",
                              "message": "요청 리소스 생성 성공",
                              "data": {
                                "userId": 1,
                                "email": "student@university.ac.kr",
                                "grade": 2,
                                "departmentId": 3,
                                "studentId": 20210001
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (입력값 검증 실패)",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_400_3",
                              "message": "입력값이 올바르지 않습니다.",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "이메일 인증 미완료",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_403_1",
                              "message": "이메일 인증이 완료되지 않았습니다.",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 존재하는 이메일",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_409_1",
                              "message": "이미 존재하는 이메일입니다.",
                              "data": null
                            }
                            """))
            )
    })
    ResponseEntity<ApiResponse<UserResponse.SignUpResponse>> signUp(UserRequest.SignUpRequest request);

    @Operation(
            summary = "로그인",
            description = """
                    이메일과 비밀번호로 로그인합니다.

                    로그인에 성공하면 사용자 식별 정보(userId, email)를 반환합니다.
                    """
    )
    @RequestBody(
            required = true,
            description = "로그인에 사용할 이메일과 비밀번호",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "email": "student@university.ac.kr",
                      "password": "password123!"
                    }
                    """))
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": {
                                "userId": 1,
                                "email": "student@university.ac.kr"
                              }
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (입력값 검증 실패)",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_400_4",
                              "message": "입력값이 올바르지 않습니다.",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "이메일 또는 비밀번호 불일치",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_401_1",
                              "message": "이메일 또는 비밀번호가 일치하지 않습니다.",
                              "data": null
                            }
                            """))
            )
    })
    ResponseEntity<ApiResponse<UserResponse.LoginResponse>> login(UserRequest.LoginRequest request);

    @Operation(
            summary = "로그아웃",
            description = "로그인된 사용자를 로그아웃합니다. 별도의 요청 바디는 필요하지 않습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": true,
                              "code": "COMMON_200_1",
                              "message": "요청 응답 성공",
                              "data": null
                            }
                            """))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "isSuccess": false,
                              "code": "AUTH_401_2",
                              "message": "인증되지 않은 사용자입니다.",
                              "data": null
                            }
                            """))
            )
    })
    ResponseEntity<ApiResponse<Void>> logout();
}
