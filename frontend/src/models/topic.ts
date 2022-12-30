import type { TaxiData } from "@/models/taxi-data";
import type { TaxiNotification } from "@/models/taxi-notification";

export enum Topic {
  TAXIS = 'taxis',
  LEAVING_AREA = 'leaving_area',
  SPEEDING = 'speeding',
}

export type TopicEventMapping = Record<Topic, string>

type TopicDataMappingBase = Record<Topic, any>

export interface TopicDataMapping extends TopicDataMappingBase {
  [Topic.TAXIS]: TaxiData
  [Topic.LEAVING_AREA]: TaxiNotification
  [Topic.SPEEDING]: TaxiNotification
}
