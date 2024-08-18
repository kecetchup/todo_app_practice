FROM openjdk:11-jre-slim

WORKDIR /app
COPY target/myapp.jar /usr/local/lib/myapp.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/myapp.jar"]