FROM openjdk:11
VOLUME ["/image/review", "/image/item", "/image/detail", "/image/thumbnail"]
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080