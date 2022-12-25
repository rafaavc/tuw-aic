import type { TaxiDistances } from "@/models/taxi-distances";
import type { AreaViolation } from "@/models/area-violation";
import type { SpeedingIncident } from "@/models/speeding-incident";

export enum Topic {
  DRIVING_TAXIS,
  TAXI_DISTANCES,
  SPEEDING_INCIDENTS,
  AREA_VIOLATIONS,
}

export type TopicEventMapping = Record<Topic, string>

type TopicDataMappingBase = Record<Topic, any>

export interface TopicDataMapping extends TopicDataMappingBase {
  [Topic.DRIVING_TAXIS]: any
  [Topic.TAXI_DISTANCES]: TaxiDistances
  [Topic.AREA_VIOLATIONS]: AreaViolation
  [Topic.SPEEDING_INCIDENTS]: SpeedingIncident
}
