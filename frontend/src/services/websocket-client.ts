import { io, Socket } from "socket.io-client";
import { Environment } from "@/services/environment";

export class WebsocketClient {

  private readonly ioSocket: Socket

  constructor() {
    this.ioSocket = io(Environment.websocketUrl)
  }

  get socket(): Socket {
    return this.ioSocket
  }

}
