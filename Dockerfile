FROM eclipse-temurin:21
VOLUME /tmp
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "sleep 5 && java -jar /app.jar"]