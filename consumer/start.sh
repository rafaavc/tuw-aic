#!/bin/bash

storm nimbus &
sleep 2s
storm jar build/libs/consumer-1.0-SNAPSHOT.jar aic.g3t1.consumer.Main
sleep 2s
storm ui
