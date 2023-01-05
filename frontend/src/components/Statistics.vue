<script setup lang="ts">
import { WebsocketClient } from "@/services/websocket-client";
import { type TaxiNotification, type TaxiNumber, Topic } from "@/models";
import { computed, reactive, ref } from "vue";
import { rounded } from "@/services/numbers";
import { pushToValue } from "@/services/maps";

const drivingTaxiCount = ref<number>(0)
const totalDistance = ref<number>(0)
const totalDistanceRounded = computed(() => rounded(totalDistance.value, 2))
const areaViolations = reactive<Map<TaxiNumber, TaxiNotification[]>>(new Map())
const speedingIncidents = reactive<Map<TaxiNumber, TaxiNotification[]>>(new Map())

const socket = new WebsocketClient()
socket.connect().then(() => {
  socket.on(Topic.TAXIS).subscribe((data) => {
    drivingTaxiCount.value = data.amountOfTaxis
    totalDistance.value = data.totalDistance
  })
  socket.on(Topic.LEAVING_AREA).subscribe((violation) => {
    const { taxiNumber } = violation
    pushToValue(areaViolations, taxiNumber, violation)
  })
  socket.on(Topic.SPEEDING).subscribe((incident) => {
    const { taxiNumber } = incident
    pushToValue(speedingIncidents, taxiNumber, incident)
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
      <ul v-if="areaViolations.size > 0">
        <li v-for="[taxiNumber, violations] of areaViolations.entries()">
          Taxi {{ taxiNumber }}
          <span v-if="violations.length > 1">
            ({{ violations.length }}x)
          </span>
        </li>
      </ul>
      <p v-else>
        No area violations so far.
      </p>
    </div>
    <div class="col-12 col-md-4">
      <p class="h4">Speeding incidents</p>
      <ul v-if="speedingIncidents.size > 0">
        <li v-for="[taxiNumber, incidents] of speedingIncidents.entries()">
          Taxi {{ taxiNumber }}
          <span v-if="incidents.length > 1">
            ({{ incidents.length }}x)
          </span>
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
