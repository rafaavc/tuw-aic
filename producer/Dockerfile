FROM openjdk:18

WORKDIR /app

COPY . .

RUN ./gradlew jar --no-daemon

CMD ["./start.sh"]
