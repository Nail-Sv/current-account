FROM maven:3.8.1-openjdk-17-slim AS builder

WORKDIR /app
COPY pom.xml ./
COPY src ./src

RUN mvn clean install -Dmaven.test.skip=true

FROM amazoncorretto:21.0.3-alpine
LABEL org.opencontainers.image.source=https://github.com/Nail-Sv/current-account
LABEL org.opencontainers.image.description="Current Account"
LABEL org.opencontainers.image.licenses=MIT


RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/target/*.jar /app/current-account.jar

RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-XX:+UseContainerSupport","-jar","/app/current-account.jar"]