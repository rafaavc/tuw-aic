#!/bin/bash

echo "[WAIT] Waiting for Zookeeper... ($KAFKA_CFG_ZOOKEEPER_CONNECT)"
/wait-for "$KAFKA_CFG_ZOOKEEPER_CONNECT" -t 60 || exit 100
echo "[WAIT] Zookeeper connected!"
echo "Making sure old sessions are expired... (20s)"
sleep 5s
echo "Making sure old sessions are expired... (15s)"
sleep 5s
echo "Making sure old sessions are expired... (10s)"
sleep 5s
echo "Making sure old sessions are expired... (5s)"
sleep 5s
echo "Starting Kafka..."
/opt/bitnami/scripts/kafka/entrypoint.sh "/opt/bitnami/scripts/kafka/run.sh"
