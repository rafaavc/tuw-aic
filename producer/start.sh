#!/bin/bash

echo "[WAIT] Waiting for Kafka... ($KAFKA_BOOTSTRAP_SERVER)"
/wait-for "$KAFKA_BOOTSTRAP_SERVER" -t 60 || exit 101
echo "[WAIT] Kafka connected!"
echo "Starting producer..."
java -jar ./build/libs/producer-1.0-SNAPSHOT.jar
