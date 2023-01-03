#!/bin/bash

echo "[WAIT] Waiting for Zookeeper... ($WAIT_ZOOKEEPER)"
/wait-for "$WAIT_ZOOKEEPER" -t 60 || exit 100
echo "[WAIT] Zookeeper connected!"

echo "[WAIT] Waiting for nimbus... ($WAIT_NIMBUS)"
/wait-for "$WAIT_NIMBUS" -t 60 || exit 104
echo "[WAIT] Nimbus connected!"

sleep 1s
echo "Finishing storm container setup..."
/docker-entrypoint.sh || exit 111
echo "Starting supervisor..."
storm supervisor
