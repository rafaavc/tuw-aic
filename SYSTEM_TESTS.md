# System Test Report

Run by: André Mategka

## Overview

| Test | Result |
|:----:|:------:|
|  01  |  PASS  |
|  02  |  PASS  |
|  03  |  PASS  |

## Test 01: Starting up

### Expected result

Starting the application as described in the `README.md` results in all
services spinning up successfully.
The Apache Storm UI is available under `localhost:8080`.
The frontend is available under `localhost:8081`.

### Actual result

{+ PASS +}

Matches expected result.
It can take a while for inter-container dependencies to resolve.
Starting may take 1-2 minutes, depending on the hardware the application is run
on.

## Test 02: Dashboard

### Expected result

The frontend contains all components as described in the assignment.
Taxis that leave the specified radius in the assignment do not contribute to
displayed statistics.
Components update in the correct intervals.
Displayed values appear plausible.

### Actual result

{+ PASS +}

The dashboard contains a live map with taxi icons, the number of currently
driving taxis, the overall distance of all taxis and expandable lists of area
violations and speeding incidents.
Taxis leaving the specified radius do not contribute to statistics, as can be
seen by the fact that the number of violations can exceed the number of
currently driving taxis.
The interface server fetches updates in 5-second intervals as described in the
assignment.
There used to be an error with the units displayed on the frontend which has
since been fixed. The values are correctly displayed now.

## Test 03: Working Storm topology

### Expected result

The workers of all bolts perform their operations without throwing errors.

### Actual result

{+ PASS +}

There have been some issues regarding Kryo serialization and average speed
calculation but these have since been fixed.
The topology bolts do not appear to throw any errors and complete their
tasks successfully, as has been verified inspecting the Redis store and
the notifications on the backend.
