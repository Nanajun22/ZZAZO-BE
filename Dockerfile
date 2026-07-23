FROM gradle:8.14.3-jdk21 AS builder

WORKDIR /app

# gradlew 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew

# 라이브러리 의존성 캐싱
RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew dependencies --no-daemon

COPY src src

# 빌드
RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]