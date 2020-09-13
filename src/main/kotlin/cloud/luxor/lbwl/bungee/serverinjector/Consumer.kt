package cloud.luxor.lbwl.bungee.serverinjector

import cloud.luxor.lbwl.bungee.serverinjector.event.ServerAddEvent
import net.md_5.bungee.api.ProxyServer
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.lang.Exception
import java.time.Duration
import java.util.*
import java.util.logging.Level

class Consumer(
        val proxy: ProxyServer,
        private val topic: String,
        private val pollTimeout: Long,
        properties: Properties
) {

    // TODO: deserializer needs to be protobuf
    private val consumer = KafkaConsumer<String, String>(properties)
    private var consumerThread = Thread.currentThread()

    fun run() {
        this.consumer.subscribe(mutableListOf(this.topic))
        this.consumerThread = Thread {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    val records = this.consumer.poll(Duration.ofMillis(this.pollTimeout))
                    records.forEach {
                        // TODO: call this.proxy.pluginManager.callEvent(ServerAddEvent(Server("id", "ip")))
                        // TODO: call this.proxy.pluginManager.callEvent(ServerRemoveEvent(Server("id", "ip")))
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    this.proxy.logger.log(Level.SEVERE, ex.message)
                }
            }
        }
        this.consumerThread.start()
    }

    fun stop() {
        this.consumerThread.interrupt()
        this.consumer.close()
    }
}