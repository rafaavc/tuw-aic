FROM openjdk:11-bullseye

WORKDIR /opt

# so that it doesn't download it when gradlew is called
RUN wget https://services.gradle.org/distributions/gradle-7.4-bin.zip

# Install wait-for
RUN apt-get -q update && apt-get -qy install netcat
ADD https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for /wait-for
RUN chmod +x /wait-for

COPY . /app
COPY ../taxi_data /app

WORKDIR /app/producer

RUN ./gradlew jar --no-daemon

ENTRYPOINT ["./start.sh"]
