#!/bin/bash

echo "[WAIT] Waiting for Kafka... ($KAFKA_BOOTSTRAP_SERVER)"
/wait-for "$KAFKA_BOOTSTRAP_SERVER" -t 60 || exit 101
echo "[WAIT] Kafka connected!"

echo "[WAIT] Waiting for Redis... ($REDIS_HOST)"
/wait-for "$REDIS_HOST" -t 60 || exit 104
echo "[WAIT] Redis connected!"

sleep 1s
echo "Starting nimbus..."
storm nimbus &
pid=$!
/wait-for "localhost:6627" -t 30 || exit 102
echo "Nimbus is online!"

sleep 5s
echo "Starting Storm UI..."
storm ui &
/wait-for "localhost:8080" -t 30 || exit 103
echo "Storm UI is online!"

echo "Waiting for 5s before submitting topology..."
sleep 5s
echo "Submitting topology..."
storm jar build/libs/consumer-1.0-SNAPSHOT.jar aic.g3t1.consumer.Main || exit 110
echo "Submission successful!"

echo "Consumer fully online!"
wait "$pid"
