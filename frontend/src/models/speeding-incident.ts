import type { TaxiNumber } from "@/models/taxi-number";

export type SpeedingIncident = {
  taxiNumber: TaxiNumber
  timestamp: Date
}
