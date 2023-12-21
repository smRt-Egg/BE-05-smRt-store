# 우리 어플리케이션 jar 파일을 여러 레이어로 추출하는 과정
FROM eclipse-temurin:17-jre as builder
WORKDIR application
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

# 실제로 우리 어플리케이션이 실행 될 환경
FROM eclipse-temurin:17-jre
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
# 애플리케이션 JAR 파일이 이미 압축이 해제 되었으므로, JarLauncher를 사용하여 애플리케이션을 시작
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "org.springframework.boot.loader.launch.JarLauncher"]
