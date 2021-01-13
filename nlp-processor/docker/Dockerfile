FROM adoptopenjdk/openjdk11:alpine-slim
COPY app.jar app.jar
RUN apk add curl
ENTRYPOINT ["java","-jar","/app.jar"]