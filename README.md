![DSG](./dsg_logo.png)

# Advanced Internet Computing WS 2022 - Group 3 Topic 1

## Team

- **André Mategka** (Coordinator): e11809614@student.tuwien.ac.at
- **Philipp-Lorenz Glaser**: philipp-lorenz.glaser@tuwien.ac.at
- **Philipp Ginter**: e11704322@student.tuwien.ac.at
- **Rafael Cristino**: e12202238@student.tuwien.ac.at
- **Xavier Pisco**: e12206635@student.tuwien.ac.at

## Key Submission Details

This section contains all the key submission details
[as outlined on TUWEL](https://tuwel.tuwien.ac.at/mod/forum/discuss.php?d=353737).

### Project Description

This project contains all the components necessary for a taxi service
simulation using stream processing technologies.

A data producer reads in
taxi driving data from a research data set and submits it to an Apache Kafka
instance with configurable parameters (speed and amount).

An Apache Storm cluster consisting of a nimbus, supervisor and UI instance is
used to process this data. Workers process a topology which first reads in data
from Apache Kafka, transforms and filters the resulting tuples using stream
processing operators (called bolts), emits notifications for detected
violations and updates a Redis key-value store.

A Spring backend server receives the notifications from the Storm cluster and
periodically polls the Redis store for updated information.
The combined information is then emitted via a WebSocket interface.

The dashboard, realized with Vue and Nginx, connects to the backend server and
receives the WebSocket data. This information is visualized using an
interactive map displaying all taxi locations, as well as simple
textual statistics.

### Testing Environment

This project was tested exclusively on local hardware.

|                | Environment #1                                                           |
|---------------:|:-------------------------------------------------------------------------|
|           Name | André Mategka                                                            |
|        General | Virtualized environment (Windows Subsystem for Linux 2 on Windows 11)    |
|        Host OS | Windows 11 21H2 (Build 22000.1335)                                       |
|       Guest OS | Ubuntu 20.04.5 LTS (GNU/Linux 5.10.102.1-microsoft-standard-WSL2 x86_64) |
|         Docker | Version 20.10.20 (via Docker Desktop on WSL 2)                           |
| Docker Compose | Version 2.12.0                                                           |
|      Processor | Intel Core i7-12700KF                                                    |
|         Memory | 16 GiB (allocated to WSL 2)                                              |
|        Storage | 500 GB (for WSL 2)                                                       |
|       Browsers | Google Chrome 108.0.5359.126, Microsoft Edge 108.0.1462.76               |

|                | Environment #2                                                           |
|---------------:|:-------------------------------------------------------------------------|
|           Name | Philipp-Lorenz Glaser                                                    |
|             OS | macOS 12.5                                                               |
|         Docker | Version 20.10.14                                                         |
| Docker Compose | Version 1.29.2                                                           |
|      Processor | Apple M1 Pro                                                             |
|         Memory | 16 GiB                                                                   |
|        Storage | 500 GB                                                                   |
|       Browsers | Firefox 108.0.2                                                          |

|                | Environment #3                                                           |
|---------------:|:-------------------------------------------------------------------------|
|           Name | Rafael Cristino                                                          |
|             OS | Pop!_OS 22.04 (linux kernel 6.0.12-76060006-generic)                     |
|         Docker | Version 20.10.22                                                         |
| Docker Compose | Version 2.14.1                                                           |
|      Processor | Intel Core i5-8250U                                                      |
|         Memory | 16 GiB                                                                   |
|        Storage | 256 GB                                                                   |
|       Browsers | Firefox 108.0                                                            |

|                | Environment #4                                                           |
|---------------:|:-------------------------------------------------------------------------|
|           Name | Xavier Pisco                                                          |
|             OS | ArcoLinux (linux kernel 6.1.6)                     |
|         Docker | Version 20.10.22                                                         |
| Docker Compose | Version 2.14.2                                                           |
|      Processor | Intel Core i7-7700HQ                                                      |
|         Memory | 32 GiB                                                                   |
|        Storage | 256 GB                                                                   |
|       Browsers | Firefox 108.0.2                                                            |

You can view the most recent protocol for our manual system tests in the
[SYSTEM_TESTS.md](SYSTEM_TESTS.md).

### User Instructions

#### 1. Requirements

- [Docker](https://www.docker.com/)
  - Tested with Docker 20.10.20
  - Check with `docker --version`
  - If you're on Windows with WSL 2 support, you can use
    [Docker Desktop on WSL 2](https://docs.docker.com/desktop/install/windows-install/#system-requirements)
- [Docker Compose 2](https://docs.docker.com/compose/)
  - Tested with Docker Compose 2.12.0
  - Check with `docker compose version`

#### 2. Configuration

To run the application, first change the configuration in the `.env` file to
suit your needs.
You can leave the configuration as-is and proceed to the next step if the
default values are fine for you.

The following environment variables may be changed:
- `TAXI_DATA_FOLDER` - The folder containing the taxi seeding data, relative to this folder
- `TAXI_DATA_SPEED` - The producer submission speed in timestamps per second
- `NUMBER_TAXIS` - The number of unique taxis submitted by the producer
- `FORBIDDEN_CITY_LAT` - The latitude of the forbidden city in Beijing
- `FORBIDDEN_CITY_LON` - The longitude of the forbidden city in Beijing
- `PREDEFINED_AREA_RADIUS` - The radius (in km) around the forbidden city center where taxis can drive
- `PREDEFINED_AREA_DISCARD_RADIUS` - A radius around the forbidden city center in km; leaving taxis are discarded
- `PREDEFINED_SPEED_LIMIT` - The speed limit of the taxis in km/h

Other environment variables must be left as-is.

#### 3. Environment

Make sure your environment is clean.
If the docker containers are already running, you will likely run into errors.
All following commands assume you are currently located in the project
directory, where the `docker-compose.yml` resides.

You can run the following command to make sure the containers are not running:

```shell
docker compose down
```

Also make sure you do not have any services bound to the ports
`8080`, `8081` and `10002`.

#### 4. Start

Use the following commands to start the application:

```shell
docker compose build
docker compose up
```

See [Troubleshooting](#troubleshooting) for additional help.

Please make sure to let the initialization complete.
It may take 1-2 minutes for all components to come online due to
inter-component dependencies and necessary wait times.

You should now be able to...
- access Apache Storm UI on `localhost:8080`
- access the dashboard frontend on `localhost:8081`

#### 5. Stop

To stop it, you can use the following command:

```shell
docker compose down
```

#### Troubleshooting

If, for any reason, starting the application fails, you can attempt to use the
following commands:

```shell
docker compose down
docker compose build --no-cache
docker compose up -d --force-recreate
```

### Member Contributions

Last updated: 16th January 2023

- **André Mategka**:
  - Apache Zookeeper: setup
  - Apache Storm: nimbus setup, supervisor setup, UI setup
  - Storm topology: setup, Kafka spout, "Calculate distance" bolt, "Store information" sink
  - Interim Presentation: slides
  - Dashboard frontend: setup, statistics
  - Final presentation: slides
- **Philipp-Lorenz Glaser**:
  - Storm topology: "Update taxi location" bolt
  - Dashboard frontend: map
  - Benchmarking & optimization: support
  - Final presentation: demo
- **Philipp Ginter**:
  - Storm topology: "Calculate speed" bolt, "Calculate average speed" bolt
  - Interim presentation: demo preparation, demo
  - Benchmarking & optimization: lead
- **Rafael Cristino**:
  - Project structure: setup
  - Apache Kafka: setup
  - Redis: setup
  - Storm topology: notification sinks ("Notify dashboard once if a taxi is &hellip;")
  - Dashboard backend: implementation
  - Final presentation: demo
- **Xavier Pisco**:
  - Data provider: implementation
  - Benchmarking & optimization: support
  - Final presentation: demo preparation, demo

## Additional Details

### Directory structure

- **common** - Shared data structures and utilities
- **consumer** - Apache Storm nimbus, UI and topology submission
- **frontend** - Web frontend for the dashboard
- **interface-server** - Web backend for the dashboard
- **kafka** - Apache Kafka container build configuration
- **producer** - Apache Kafka data seeder
- **redis** - Redis container build configuration
- **storm-supervisor** - Apache Storm supervisor container build configuration
- **taxi_data** - T-Drive taxi location data set (used by the producer)

### Architecture

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
- `interface-server`:
  - Receives HTTP POST requests on port `10002` and paths `/notification/speeding` and `/notification/leaving-area`
  - Exposes WebSocket STOMP endpoint on port `10002` and path `/ws`
  - Every 5s, reads taxi information from Redis and publishes it on WebSocket STOMP topic `/topic/taxis`
  - Publishes speeding notifications on WebSocket STOMP topic `/topic/notifications/speeding`
  - Publishes leaving area notifications on WebSocket STOMP topic `/topic/notifications/leaving-area`
- `frontend`:
  - Exposes a web application on port `8081` which shows overall statistics and a map of driving taxis
  - Connects to WebSocket STOMP endpoint on `interface-server:10002`
  - Subscribes to topics `/topic/taxis`, `/topic/notifications/speeding` and `/topic/notifications/leaving-area`
  - Uses [OpenStreetMap](https://www.openstreetmap.org/) for its map data

### Components

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
- `interface-server` - The Spring dashboard backend which serves as the interface between Storm/Redis and the frontend
- `frontend` - The Vue dashboard frontend which visualizes the data received from the backend

### How to debug

- Simple errors are displayed in Storm UI.
- More severe errors have to be troubleshooted by viewing the log files:
  - Find the port of the suspicious task in Storm UI.
  - `docker exec -it <storm-supervisor container name> bash`
  - `cd /logs/workers-artifacts/aic-topology-*/<port>`
  - Look at the `worker.log` for log output, `worker.log.err` for Storm error output
- To inspect the Redis store contents, you can use [Redis Commander](https://github.com/joeferner/redis-commander).
