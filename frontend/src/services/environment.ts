import { Topic, type TopicEventMapping } from "@/models/topic";
import { nameFromValue } from "@/services/enums";

export class Environment {

  private static readonly websocketUrlDefaults: UrlParts = {
    protocol: 'ws',
    host: 'localhost',
    port: 80,
    path: '',
  }

  private static websocketUrlMemo: string | null = null
  private static eventNamesMemo: TopicEventMapping | null = null

  static get websocketUrl(): string {
    if (this.websocketUrlMemo === null) {
      this.websocketUrlMemo = this.computeWebsocketUrl()
    }
    return this.websocketUrlMemo
  }

  static computeWebsocketUrl(): string {
    const {
      VITE_BACKEND_PROTOCOL_WS,
      VITE_BACKEND_HOST,
      VITE_BACKEND_PORT_WS,
      VITE_BACKEND_PATH_WS,
    } = import.meta.env
    const environmentParts: Partial<UrlParts> = {
      protocol: VITE_BACKEND_PROTOCOL_WS,
      host: VITE_BACKEND_HOST,
      port: Environment.tryParseInt(VITE_BACKEND_PORT_WS),
      path: VITE_BACKEND_PATH_WS,
    }
    const { protocol, host, port, path } = Object.assign({}, Environment.websocketUrlDefaults, environmentParts)
    return `${protocol}://${host}:${port}${path}`
  }

  static get eventNames(): TopicEventMapping {
    if (this.eventNamesMemo === null) {
      this.eventNamesMemo = this.computeEventNames()
    }
    return this.eventNamesMemo
  }

  static computeEventNames(): TopicEventMapping {
    const {
      VITE_WS_TOPIC_TAXIS,
      VITE_WS_TOPIC_SPEEDING,
      VITE_WS_TOPIC_LEAVING_AREA,
    } = import.meta.env
    const environmentTopics: Partial<TopicEventMapping> = {
      [Topic.TAXIS]: VITE_WS_TOPIC_TAXIS,
      [Topic.SPEEDING]: VITE_WS_TOPIC_SPEEDING,
      [Topic.LEAVING_AREA]: VITE_WS_TOPIC_LEAVING_AREA,
    }
    const undefinedTopics = Object.entries(environmentTopics)
      .filter(([_, v]) => typeof v === 'undefined')
      .map(([k, _]) => nameFromValue(Topic, k))
    if (undefinedTopics.length > 0) {
      throw new Error(`Topic event names not defined: ${undefinedTopics}`)
    }
    return environmentTopics as TopicEventMapping
  }

  private static tryParseInt(maybeInt: string | undefined): number | undefined {
    if (typeof maybeInt !== 'undefined') {
      const parsedInt = Number.parseInt(maybeInt)
      if(!Number.isNaN(parsedInt)) {
        return parsedInt
      }
    }
  }

}

type UrlParts = {
  protocol: string
  host: string
  port: number
  path: string
}
