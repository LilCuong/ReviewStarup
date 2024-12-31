# Sử dụng image JDK 21 để build
FROM eclipse-temurin:21-jdk AS BUILD
WORKDIR /app

# Cài đặt Maven thủ công
RUN apt-get update && apt-get install -y maven

COPY . .
RUN mvn clean package -DskipTests

# Tạo image runtime
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=BUILD /app/target/ReviewMaps-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]