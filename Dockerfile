FROM adoptopenjdk/maven-openjdk11

ARG JAR_FILE=target/spring-boot-web.jar

WORKDIR /opt/app

COPY target/gameofthree.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]