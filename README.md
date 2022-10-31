
![DSG](./docs/dsg_logo.png)

# Advanced Internet Computing WS 2022 - Group 3 Topic 1

## Team

* **André Mategka** (Coordinator): e11809614@student.tuwien.ac.at
* **Philipp-Lorenz Glaser**: philipp-lorenz.glaser@tuwien.ac.at
* **Philipp Ginter**: e11704322@student.tuwien.ac.at
* **Rafael Cristino**: e12202238@student.tuwien.ac.at
* **Xavier Pisco**: e12206635@student.tuwien.ac.at

## Overview

### Directory structure

- **common** - code that can be used by other components
- **consumer** - example kafka consumer component
- **producer** - example kafka producer component
- **example** - initial repository structure given by course organizers
- **taxi_data** - folder containing all the data from the taxis, one file per taxi

## Architecture

TODO

## Components

TODO

## How to run

Using regular docker commands. Optionally the Makefile can be used.

```shell
docker compose build && docker compose up
```

In order to change the data, change the files inside the taxi_data to the ones you desire.

In order to change the speed, change the speed variable in the Producer class inside the producer package.

## How to debug

TODO
