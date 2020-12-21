FROM adoptopenjdk/openjdk11:alpine-jre

ARG JAR_FILE=target/spring-boot-web.jar

WORKDIR /opt/app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]