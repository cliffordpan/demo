FROM amazoncorretto:17-al2023-headless
LABEL authors="Cliff Pan"
ENV JAVA_FILE=./build/libs/example-boot.jar
COPY ${JAVA_FILE} backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]