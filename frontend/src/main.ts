import { createApp } from "vue"
import App from "./App.vue"
import router from "./router"
import { VTooltip, VClosePopper, Dropdown, Tooltip, Menu, options } from "floating-vue"

import "./assets/styles.scss"

const app = createApp(App)

app.use(router)

options.distance = 12
app.directive('tooltip', VTooltip)
app.directive('close-popper', VClosePopper)
app.component('VDropdown', Dropdown)
app.component('VTooltip', Tooltip)
app.component('VMenu', Menu)

app.mount("#app")
