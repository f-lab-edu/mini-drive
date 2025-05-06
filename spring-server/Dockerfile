# ✨ Build Stage
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

# 나머지 복사 및 빌드
COPY . .

# 빌드 (루트의 settings.gradle 기준으로 멀티모듈 전체 빌드)
RUN gradle :module-api:bootJar -x test --no-daemon

# ✨ Run Stage
FROM openjdk:21-slim
WORKDIR /app
COPY --from=builder /app/module-api/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]