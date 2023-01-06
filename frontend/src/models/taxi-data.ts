import type { Float, Int, TaxiNumber } from "@/models/definitions";

export type TaxiData = {
  amountOfTaxis: Int
  totalDistance: Float
  taxis: IndividualTaxiData[]
}

export type IndividualTaxiData = {
  number: TaxiNumber
  distance: Float | null
  averageSpeed: Float | null
  location: TaxiLocation
}

export type TaxiLocation = {
  latitude: Float
  longitude: Float
}
