#!/bin/bash

storm nimbus &
sleep 5s
storm jar build/libs/consumer-1.0-SNAPSHOT.jar aic.g3t1.consumer.Main
storm ui
