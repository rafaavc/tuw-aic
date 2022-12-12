
![DSG](./example/docs/dsg_logo.png)

# Advanced Internet Computing WS 2022 - Group 3 Topic 1

## Team

- **André Mategka** (Coordinator): e11809614@student.tuwien.ac.at
- **Philipp-Lorenz Glaser**: philipp-lorenz.glaser@tuwien.ac.at
- **Philipp Ginter**: e11704322@student.tuwien.ac.at
- **Rafael Cristino**: e12202238@student.tuwien.ac.at
- **Xavier Pisco**: e12206635@student.tuwien.ac.at

## Overview

### Directory structure

- **common** - Shared data structures and utilities
- **consumer** - Apache Storm nimbus, UI and topology submission
- **example** - Initial repository structure given by course organizers
- **kafka** - Apache Kafka container build configuration
- **producer** - Apache Kafka data seeder
- **redis** - Redis container build configuration
- **storm-supervisor** - Apache Storm supervisor container build configuration
- **taxi_data** - T-Drive taxi location data set (used by the producer)

### Work Distribution

Last updated: 12/12/2022

- **André Mategka**:
  - Apache Zookeeper: setup
  - Apache Storm: nimbus setup, supervisor setup, UI setup
  - Storm topology: setup, Kafka spout, distance-related bolts
  - Interim Presentation: slides
- **Philipp-Lorenz Glaser**:
  - Storm topology: location-related bolts
- **Philipp Ginter**:
  - Storm topology: speed-related bolts
  - Interim Presentation: demo
- **Rafael Cristino**:
  - Project structure: setup
  - Apache Kafka: setup
  - Redis: setup
- **Xavier Pisco**:
  - Data provider: implementation

## Architecture

- `zookeeper`:
  - Interfaces with `kafka` and `consumer`
- `kafka`:
  - Written to on port `9092` by `producer`
  - Read from port `9092` by `supervisor` (workers)
- `producer`:
  - Writes data to topic `"taxi"` on `kafka:9092`
- `redis`:
  - Written to on port `6379` by `supervisor` (workers)
- `consumer`:
  - Nimbus assigns tasks on `supervisor` on ports `6700`, `6701`, ..., `67xx`
  - Submitter submits topology to `consumer:6627` (`localhost`)
  - Storm UI connects to `consumer:6627` (`localhost`)
  - Storm UI exposed on port `8080`
- `supervisor`:
  - Receives tasks from nimbus on `consumer:6627`
  - Reads data from topic `"taxi"` on `kafka:9092`
  - Writes data to `redis:6279`

## Components

- `zookeeper` - The Apache Zookeeper instance used by Apache Kafka & Storm
- `kafka` - The Apache Kafka instance that holds captured sensor data
- `producer` - The Kafka data seeder simulating sensor data from driving taxis
- `redis` - The Redis instance where the processed results are stored
- `consumer` - The data consumer, which consists of multiple subcomponents:
  - Apache Storm nimbus - The leader node which manages the active topology
  - Apache Storm UI - A web-interface server which allows insights into the nimbus
  - Consumer - The Java application which submits the topology to the nimbus
    - Kafka spout - Reads entries from `kafka` and makes them available to the topology
    - Bolts - Data transformers which process data from other spouts and bolts
    - Redis sink - Writes entries to `redis` according to its connected input bolts
- `supervisor` - The Apache Storm supervisor which provides workers for processing the topology

## How to run

Using regular docker commands. Optionally the Makefile can be used.

```shell
docker compose build && docker compose up
```

The following environment variables may be set in the `.env` file:
- `TAXI_DATA_FOLDER` - The folder containing the taxi seeding data, relative to this folder
- `TAXI_DATA_SPEED` - The producer submission speed in timestamps per second
- `NUMBER_TAXIS` - The number of unique taxis submitted by the producer

## How to debug

- Simple errors are displayed in Storm UI.
- More severe errors have to be troubleshooted by viewing the log files:
  - Find the port of the suspicious task in Storm UI.
  - `docker exec -it <storm-supervisor container name> bash`
  - `cd /logs/workers-artifacts/aic-topology-*/<port>`
  - Look at the `worker.log` for log output, `worker.log.err` for Storm error output
- To inspect the Redis store contents, you can use [Redis Commander](https://github.com/joeferner/redis-commander).
