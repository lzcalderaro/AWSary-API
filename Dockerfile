FROM gradle:8-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 80:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-docker-sample.jar
ARG GET_AWS_ACCESS_KEY_ID
ARG GET_AWS_SECRET_ACCESS_KEY

ENV AWS_ACCESS_KEY_ID $GET_AWS_ACCESS_KEY_ID
ENV AWS_SECRET_ACCESS_KEY $GET_AWS_SECRET_ACCESS_KEY
ENTRYPOINT ["java","-jar","/app/ktor-docker-sample.jar"]