<script setup lang="ts">
import { WebsocketClient } from "@/services/websocket-client";
import { Topic } from "@/models/topic";
import { computed, reactive, ref } from "vue";
import { rounded } from "@/services/numbers";
import type { TaxiNotification } from "@/models/taxi-notification";

const drivingTaxiCount = ref<number>(0)
const totalDistance = ref<number>(0)
const totalDistanceRounded = computed(() => rounded(totalDistance.value, 2))
const areaViolations = reactive<TaxiNotification[]>([])
const speedingIncidents = reactive<TaxiNotification[]>([])

const socket = new WebsocketClient()
socket.connect().then(() => {
  socket.on(Topic.TAXIS).subscribe((data) => {
    drivingTaxiCount.value = data.amountOfTaxis
    totalDistance.value = data.totalDistance
  })
  socket.on(Topic.LEAVING_AREA).subscribe((violation) => {
    areaViolations.push(violation)
  })
  socket.on(Topic.SPEEDING).subscribe((incident) => {
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
