FROM openjdk:11-bullseye

WORKDIR /opt

# so that it doesn't download it when gradlew is called
RUN wget https://services.gradle.org/distributions/gradle-7.6-bin.zip

COPY ./interface-server /app/interface-server
COPY ./common /app/common
COPY ./settings.gradle /app
COPY ./gradlew* /app/interface-server

WORKDIR /app/interface-server

RUN ./gradlew build --no-daemon

ENTRYPOINT ["java", "-jar", "./build/libs/interface-server-0.0.1-SNAPSHOT.jar"]
