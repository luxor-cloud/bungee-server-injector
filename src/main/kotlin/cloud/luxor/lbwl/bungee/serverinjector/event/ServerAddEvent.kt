package cloud.luxor.lbwl.bungee.serverinjector.event

import cloud.luxor.lbwl.bungee.serverinjector.Server
import net.md_5.bungee.api.plugin.Event

data class ServerAddEvent(val server: Server) : Event()