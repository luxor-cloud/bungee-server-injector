package cloud.luxor.lbwl.bungee.serverinjector

import cloud.luxor.lbwl.bungee.serverinjector.event.ServerAddEvent
import cloud.luxor.lbwl.bungee.serverinjector.event.ServerRemoveEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.event.EventHandler
import java.net.InetSocketAddress

class ServerInjectorPlugin : Plugin(), Listener {

    private var consumer: Consumer? = null

    override fun onEnable() {
        logger.info("Reading configuration")
        // TODO: read configuration

        this.logger.info("Starting kafka consumer")
        this.consumer?.run()
    }

    override fun onDisable() {
        this.logger.info("Stopping kafka consumer")
        this.consumer?.stop()
    }

    @EventHandler
    fun onServerAdd(event: ServerAddEvent) {
        // make sure this is synchronized because we don't know if the underlying map
        // provided by this.proxy.servers is tread-safe.
        synchronized(this) {
            this.proxy.servers[event.server.id] = this.proxy.constructServerInfo(
                    event.server.id, // name
                    InetSocketAddress(event.server.ip, 25565),
                    event.server.id, // motd
                    false
            )
        }
    }

    @EventHandler
    fun onServerRemove(event: ServerRemoveEvent) {
        // see above comment.
        synchronized(this) {
            this.proxy.servers.remove(event.server.id)
        }
    }
}