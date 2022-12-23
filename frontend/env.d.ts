/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_BACKEND_HOST: string | undefined
  readonly VITE_BACKEND_PROTOCOL_WS: string | undefined
  readonly VITE_BACKEND_PORT_WS: string | undefined
  readonly VITE_BACKEND_PATH_WS: string | undefined
  readonly VITE_WS_TOPIC_DRIVING_TAXIS: string | undefined
  readonly VITE_WS_TOPIC_TAXI_DISTANCES: string | undefined
  readonly VITE_WS_TOPIC_SPEEDING_INCIDENTS: string | undefined
  readonly VITE_WS_TOPIC_AREA_VIOLATIONS: string | undefined
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
