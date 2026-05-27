FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/pasteleria-backend-*.jar app.jar
COPY storage/ ./storage/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
