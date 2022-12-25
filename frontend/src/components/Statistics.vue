<script setup lang="ts">
import { WebsocketClient } from "@/services/websocket-client";
import { Topic } from "@/models/topic";
import { computed, reactive, ref } from "vue";
import type { AreaViolation } from "@/models/area-violation";
import type { SpeedingIncident } from "@/models/speeding-incident";
import { rounded } from "@/services/numbers";

const drivingTaxiCount = ref<number>(0)
const totalDistance = ref<number>(0)
const totalDistanceRounded = computed(() => rounded(totalDistance.value, 2))
const areaViolations = reactive<AreaViolation[]>([])
const speedingIncidents = reactive<SpeedingIncident[]>([])

const socket = new WebsocketClient()
socket.connect().then(() => {
  socket.on(Topic.TAXI_DISTANCES).subscribe((distances) => {
    const excludedTaxis = new Set(areaViolations.map((violation) => violation.taxiNumber))
    let newDrivingTaxiCount = 0
    let newTotalDistance = 0
    for (let [taxiNumber, distance] of Object.entries(distances)) {
      if (!excludedTaxis.has(taxiNumber)) {
        newDrivingTaxiCount++
        newTotalDistance += distance
      }
    }
    drivingTaxiCount.value = newDrivingTaxiCount
    totalDistance.value = newTotalDistance
  })
  socket.on(Topic.AREA_VIOLATIONS).subscribe((violation) => {
    areaViolations.push(violation)
  })
  socket.on(Topic.SPEEDING_INCIDENTS).subscribe((incident) => {
    speedingIncidents.push(incident)
  })
})
</script>

<template>
  <div class="row">
    <div class="col-12 col-md-4">
      <p class="h4">Currently driving taxis</p>
      <p>{{ drivingTaxiCount }}</p>
      <p class="h4">Overall distances of all taxis</p>
      <p>{{ totalDistanceRounded }} km</p>
    </div>
    <div class="col-12 col-md-4">
      <p class="h4">Area violations</p>
      <ul v-if="areaViolations.length > 0">
        <li v-for="areaViolation of areaViolations">
          Taxi {{ areaViolation.taxiNumber }}
        </li>
      </ul>
      <p v-else>
        No area violations so far.
      </p>
    </div>
    <div class="col-12 col-md-4">
      <p class="h4">Speeding incidents</p>
      <ul v-if="speedingIncidents.length > 0">
        <li v-for="speedingIncident of speedingIncidents">
          Taxi {{ speedingIncident.taxiNumber }}
        </li>
      </ul>
      <p v-else>
        No speeding incidents so far.
      </p>
    </div>
  </div>
</template>

<style scoped lang="scss">
</style>
