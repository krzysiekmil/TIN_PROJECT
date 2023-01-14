FROM gradle:7.6.0-jdk19-alpine as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

FROM amazoncorretto:19-alpine
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/tin-0.0.1-SNAPSHOT.war /app/tin-0.0.1-SNAPSHOT.war
EXPOSE 8080
ENTRYPOINT ["java","-war","/app/tin-0.0.1-SNAPSHOT.war"]
