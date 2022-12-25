export enum Topic {
  DRIVING_TAXIS,
  TAXI_DISTANCES,
  SPEEDING_INCIDENTS,
  AREA_VIOLATIONS,
}

export type TopicEventMapping = Record<Topic, string>
