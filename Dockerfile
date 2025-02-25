FROM maven:3.9.9-amazoncorretto-21-alpine AS build
WORKDIR /app
COPY ./ .
RUN mvn clean package

FROM tomcat:11.0.4-jdk21-temurin-noble
ARG APP_NAME=myblog.war
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/${APP_NAME}
CMD ["catalina.sh", "run"]