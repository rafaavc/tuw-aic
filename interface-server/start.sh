#!/bin/bash

echo "[WAIT] Waiting for Redis... ($REDIS_HOST)"
/wait-for "$REDIS_HOST" -t 60 || exit 104
echo "[WAIT] Redis connected!"

sleep 5s
echo "Starting interface server..."
java -jar "./build/libs/interface-server-0.0.1-SNAPSHOT.jar"
