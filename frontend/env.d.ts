/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_BACKEND_HOST: string | undefined
  readonly VITE_BACKEND_PROTOCOL_WS: string | undefined
  readonly VITE_BACKEND_PORT_WS: string | undefined
  readonly VITE_BACKEND_PATH_WS: string | undefined
  readonly VITE_WS_TOPIC_TAXIS: string | undefined
  readonly VITE_WS_TOPIC_SPEEDING: string | undefined
  readonly VITE_WS_TOPIC_LEAVING_AREA: string | undefined
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
