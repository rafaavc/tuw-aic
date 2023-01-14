<script setup lang="ts">
import { ref, inject, computed } from "vue"
import { type IndividualTaxiData, Topic } from "@/models"
import { WebsocketClient } from "@/services/websocket-client"
import taxiIcon from "@/assets/taxi.png"
import { rounded } from "@/services/numbers";

// map view variables
const center = ref<[number, number]>([116.390833, 39.915833])
const rotation = ref<number>(0)
const zoom = ref<number>(12.0)
const projection = ref<string>("EPSG:4326")

const taxis = ref<IndividualTaxiData[]>([])
const selectedTaxi = ref<IndividualTaxiData>()
const selectedTaxiDistance = computed(() => selectedTaxi.value?.distance ? rounded(selectedTaxi.value.distance / 1000, 2) : 0)
const selectedTaxiAvgSpeed = computed(() => selectedTaxi.value?.averageSpeed ? rounded(selectedTaxi.value.averageSpeed, 2) : 0)

const selectConditions: any = inject("ol-selectconditions")
const hover = selectConditions.pointerMove
const click = selectConditions.click // can be used for handling click events (currently not in use)

const centerMap = () => {
  center.value = [116.390833, 39.915833]
}

const featureSelected = (event: any) => {
  if (event.selected.length === 1 && event.selected[0].get("number")) {
    selectedTaxi.value = taxis.value.find(
      (e) => e.number === event.selected[0].get("number")
    )
  } else {
    selectedTaxi.value = undefined
  }
}

const socket = new WebsocketClient()
socket.connect().then(() => {
  socket.on(Topic.TAXIS).subscribe((data) => {
    taxis.value = data.taxis
  })
})
</script>

<template>
  <div class="container text-center py-2">
    <ol-map ref="map" id="taxiMap">
      <ol-view ref="view" :center="center" :rotation="rotation" :zoom="zoom" :projection="projection" />
      <ol-scaleline-control />
      <ol-zoom-control />
      <ol-zoomslider-control />
      <ol-control-printdialog />
      <ol-control-bar>
        <ol-control-button :title="'Center Map to Beijing'" :html="'C'" :handleClick="centerMap" />
      </ol-control-bar>
      <ol-fullscreen-control />
      <ol-tile-layer>
        <ol-source-osm />
      </ol-tile-layer>
      <ol-overlay :position="[selectedTaxi.location.longitude, selectedTaxi.location.latitude]" v-if="selectedTaxi" :offset="[75, -75]">
        <template v-slot="slotProps">
          <div class="card border-light shadow-lg" style="max-width: 20rem; text-align: left;">
            <div class="card-header"><h4 class="card-title">Taxi #{{ selectedTaxi.number }}</h4></div>
            <div class="card-body">
              <div class="row">
                <div class="col"><strong>Distance</strong></div>
                <div class="col">{{ selectedTaxiDistance }} km</div>
              </div>
              <div class="row">
                <div class="col"><strong>Average Speed</strong></div>
                <div class="col">{{ selectedTaxiAvgSpeed }} km/h</div>
              </div>
              <div class="row">
                <div class="col"><strong>Coordinates</strong></div>
                <div class="col">{{ selectedTaxi.location.longitude }}, {{ selectedTaxi.location.latitude }}</div>
              </div>
            </div>
          </div>
        </template>
      </ol-overlay>
      <ol-interaction-select @select="featureSelected" :condition="hover">
        <ol-style>
          <ol-style-icon :src="taxiIcon" :scale="0.2" :opacity="0.8"></ol-style-icon>
        </ol-style>
      </ol-interaction-select>
      <ol-vector-layer>
        <ol-source-vector>
          <ol-feature v-for="taxi in taxis" :key="taxi.number" :properties="taxi">
            <ol-geom-point :coordinates="[taxi.location.longitude, taxi.location.latitude]"></ol-geom-point>
          </ol-feature>
        </ol-source-vector>
        <ol-style>
          <ol-style-icon :src="taxiIcon" :scale="0.15"></ol-style-icon>
        </ol-style>
      </ol-vector-layer>
    </ol-map>
  </div>
</template>

<style scoped lang="scss">
#taxiMap {
  height: 800px;
}
</style>
