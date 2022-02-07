FROM openjdk:11
#VOLUME ["/image/review", "/image/item", "/image/detail", "/image/thumbnail"]
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/static/detailImage/ /image/detail/
COPY src/main/resources/static/itemImage/ /image/item/
COPY src/main/resources/static/reviewImage/ /image/review/
COPY src/main/resources/static/thumbnail/ /image/thumbnail/
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080