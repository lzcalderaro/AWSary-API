FROM gradle:8-jdk11 AS build
ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Pass AWS credentials as environment variables during Gradle build

RUN gradle buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-docker-sample.jar
ENTRYPOINT ["java","-jar","/app/ktor-docker-sample.jar"]