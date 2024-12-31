# Sử dụng Maven với JDK 21 để build
FROM maven:3.8.5-openjdk-21 AS BUILD
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Sử dụng JDK 21 để chạy ứng dụng
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=BUILD /app/target/demo-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/demo.jar"]