package org.example.zzazo.domain.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.example.zzazo.domain.user.dto.UserRequest;
import org.example.zzazo.domain.user.dto.UserResponse;
import org.example.zzazo.domain.user.entity.EmailVerification;
import org.example.zzazo.domain.user.entity.RefreshToken;
import org.example.zzazo.domain.user.entity.User;
import org.example.zzazo.domain.user.exception.AuthErrorCode;
import org.example.zzazo.domain.user.repository.EmailVerificationRepository;
import org.example.zzazo.domain.user.repository.RefreshTokenRepository;
import org.example.zzazo.domain.user.repository.UserRepository;
import org.example.zzazo.global.error.CustomException;
import org.example.zzazo.global.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

// 인증(회원가입) 관련 비즈니스 로직
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Pattern SCHOOL_EMAIL_PATTERN =
            Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@gachon\\.ac\\.kr$");

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.email-verification.expiration-minutes}")
    private long expirationMinutes;

    @Value("${app.email-verification.code-length}")
    private int codeLength;

    // 이메일 인증번호 발송
    @Transactional
    public void sendEmailVerification(String email) {
        validateSchoolEmail(email);

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(AuthErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        String verificationCode = generateVerificationCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);

        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseGet(() -> EmailVerification.builder()
                        .email(email)
                        .verificationCode(verificationCode)
                        .expiresAt(expiresAt)
                        .verified(false)
                        .build());
        emailVerification.renew(verificationCode, expiresAt);
        emailVerificationRepository.save(emailVerification);

        emailSenderService.sendVerificationEmail(email, verificationCode);
    }

    // 이메일 인증번호 확인
    @Transactional
    public void verifyEmailCode(String email, String verificationCode) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(AuthErrorCode.EMAIL_VERIFICATION_NOT_FOUND));

        if (!emailVerification.matchesCode(verificationCode)) {
            throw new CustomException(AuthErrorCode.VERIFICATION_CODE_MISMATCH);
        }

        if (emailVerification.isExpired()) {
            throw new CustomException(AuthErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        emailVerification.verify();
    }

    // 회원가입
    @Transactional
    public UserResponse.SignUpResponse signUp(UserRequest.SignUpRequest request) {
        validateSchoolEmail(request.email());

        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(AuthErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        EmailVerification emailVerification = emailVerificationRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(AuthErrorCode.EMAIL_NOT_VERIFIED));

        if (!emailVerification.isVerified()) {
            throw new CustomException(AuthErrorCode.EMAIL_NOT_VERIFIED);
        }

        User user = User.builder()
                .departmentId(request.departmentId())
                .studentId(request.studentId())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .grade(request.grade())
                .emailVerified(true)
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.SignUpResponse.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .grade(savedUser.getGrade())
                .departmentId(savedUser.getDepartmentId())
                .studentId(savedUser.getStudentId())
                .build();
    }

    // 로그인
    @Transactional
    public UserResponse.LoginResponse login(UserRequest.LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(AuthErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(AuthErrorCode.LOGIN_FAILED);
        }

        if (!user.isEmailVerified()) {
            throw new CustomException(AuthErrorCode.EMAIL_NOT_VERIFIED);
        }

        String accessToken = jwtProvider.createAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());
        LocalDateTime refreshTokenExpiredAt = LocalDateTime.now()
                .plusSeconds(jwtProvider.getRefreshTokenExpiration() / 1000);

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(user.getUserId())
                .orElseGet(() -> RefreshToken.builder()
                        .userId(user.getUserId())
                        .token(refreshToken)
                        .expiredAt(refreshTokenExpiredAt)
                        .build());
        refreshTokenEntity.renew(refreshToken, refreshTokenExpiredAt);
        refreshTokenRepository.save(refreshTokenEntity);

        return UserResponse.LoginResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 로그아웃
    @Transactional
    public void logout(String refreshToken) {
        try {
            jwtProvider.parseClaims(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_INVALID);
        }

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));

        refreshTokenRepository.delete(refreshTokenEntity);
    }

    // 토큰 재발급 (RefreshToken Rotation)
    @Transactional
    public UserResponse.TokenReissueResponse reissueToken(String refreshToken) {
        Claims claims;
        try {
            claims = jwtProvider.parseClaims(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(AuthErrorCode.REFRESH_TOKEN_INVALID);
        }

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));

        Long userId = Long.parseLong(claims.getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.TOKEN_USER_NOT_FOUND));

        String newAccessToken = jwtProvider.createAccessToken(user.getUserId(), user.getEmail());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getUserId());
        LocalDateTime newRefreshTokenExpiredAt = LocalDateTime.now()
                .plusSeconds(jwtProvider.getRefreshTokenExpiration() / 1000);

        refreshTokenEntity.renew(newRefreshToken, newRefreshTokenExpiredAt);
        refreshTokenRepository.save(refreshTokenEntity);

        return UserResponse.TokenReissueResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }

    private void validateSchoolEmail(String email) {
        if (!SCHOOL_EMAIL_PATTERN.matcher(email).matches()) {
            throw new CustomException(AuthErrorCode.INVALID_SCHOOL_EMAIL);
        }
    }

    private String generateVerificationCode() {
        int bound = (int) Math.pow(10, codeLength);
        return String.format("%0" + codeLength + "d", secureRandom.nextInt(bound));
    }
}
