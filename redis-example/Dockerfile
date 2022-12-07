FROM openjdk:11-bullseye

WORKDIR /opt

# so that it doesn't download it when gradlew is called
RUN wget https://services.gradle.org/distributions/gradle-7.4-bin.zip

COPY . /app

WORKDIR /app/redis-example

RUN ./gradlew jar --no-daemon

CMD ["./start.sh"]
