# 빌드 스테이지: 최종 이미지 크기를 줄이기 위한 멀티 스테이지 빌드
FROM eclipse-temurin:23-jdk-jammy as builder

WORKDIR /build

# 빌드된 JAR 파일 복사
COPY build/libs/*.jar application.jar

# 레이어 최적화를 위해 JAR 파일 추출
RUN java -Djarmode=layertools -jar application.jar extract

# 실행 스테이지: 실제 운영 환경에서 사용할 이미지
FROM eclipse-temurin:23-jre-jammy-minimal

WORKDIR /app

# 보안을 위한 비-root 사용자 생성
RUN addgroup --system --gid 1001 appgroup && \
    adduser --system --uid 1001 --ingroup appgroup appuser

# 레이어별로 복사 (캐시 활용 최적화)
COPY --from=builder /build/dependencies/ ./
COPY --from=builder /build/spring-boot-loader/ ./
COPY --from=builder /build/snapshot-dependencies/ ./
COPY --from=builder /build/application/ ./

# 비-root 사용자로 전환
USER appuser

# 컨테이너 포트 노출
EXPOSE 8080

# 힙 메모리 설정 및 기타 JVM 옵션
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.JarLauncher"] 