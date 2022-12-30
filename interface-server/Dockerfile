FROM openjdk:11-bullseye

# Install wait-for
RUN apt-get -q update && apt-get -qy install netcat
ADD https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for /wait-for
RUN chmod +x /wait-for

WORKDIR /opt

# so that it doesn't download it when gradlew is called
RUN wget https://services.gradle.org/distributions/gradle-7.6-bin.zip

COPY ./interface-server /app/interface-server
COPY ./common /app/common
COPY ./settings.gradle /app
COPY ./gradlew* /app/interface-server

WORKDIR /app/interface-server

RUN ./gradlew build --no-daemon

CMD ["./start.sh"]
