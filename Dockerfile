FROM adoptopenjdk/maven-openjdk11

WORKDIR /opt/app

COPY target/gameofthree.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]