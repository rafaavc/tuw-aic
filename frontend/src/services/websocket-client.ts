import { Environment } from "@/services/environment";
import type { Topic, TopicDataMapping } from "@/models/topic";
import { Observable } from "rxjs";
import SockJS from "sockjs-client/dist/sockjs";
import { CompatClient, Stomp } from "@stomp/stompjs";

export class WebsocketClient {

  private static readonly DATE_FORMAT = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)(Z|([+\-])(\d{2}):(\d{2}))$/

  private readonly stompClient: CompatClient

  constructor() {
    const websocket: WebSocket = new SockJS(Environment.websocketUrl)
    this.stompClient = Stomp.over(websocket)
  }

  connect(): Promise<void> {
    return new Promise((resolve) => {
      if (this.stompClient.connected) {
        resolve()
        return
      }
      this.stompClient.connect({}, () => resolve())
    })
  }

  on<T extends Topic>(topic: T): Observable<TopicDataMapping[T]> {
    return new Observable<TopicDataMapping[T]>((subscriber) => {
      this.stompClient.subscribe(Environment.eventNames[topic], (response) => {
        const message = JSON.parse(response.body, WebsocketClient.reviveDates)
        subscriber.next(message)
      })
    })
  }

  private static reviveDates(key: string, value: any): any {
    if (typeof value === 'string' && WebsocketClient.DATE_FORMAT.exec(value)) {
      return new Date(value)
    }
    return value
  }

}
