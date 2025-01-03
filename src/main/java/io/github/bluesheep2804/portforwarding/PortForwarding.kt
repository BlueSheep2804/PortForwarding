package io.github.bluesheep2804.portforwarding

import com.github.alexdlaird.ngrok.NgrokClient
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig
import com.github.alexdlaird.ngrok.protocol.CreateTunnel
import com.github.alexdlaird.ngrok.protocol.Proto
import org.bukkit.plugin.java.JavaPlugin

class PortForwarding : JavaPlugin() {
    private lateinit var ngrokClient: NgrokClient
    private lateinit var configManager: ConfigManager
    private val config: ConfigData
        get() = configManager.config
    private var url: String? = null
    override fun onEnable() {
        configManager = ConfigManager(this.dataFolder)

        val ngrokConfig = JavaNgrokConfig.Builder()
                .withAuthToken(config.authToken)
                .withRegion(config.region)
                .build()
        ngrokClient = NgrokClient.Builder()
                .withJavaNgrokConfig(ngrokConfig)
                .build()
        val createTunnel = CreateTunnel.Builder()
                .withProto(Proto.TCP)
                .withAddr(config.port)
                .build()

        val tunnel = runCatching {
            ngrokClient.connect(createTunnel)
        }.getOrElse {
            logger.warning("Port forwarding failed.")
            if (config.authToken == configManager.defaultConfig.authToken) {
                logger.warning("AUTHTOKEN seems to be the default.")
            }
            logger.warning("Check the config.yml.")
            this.pluginLoader.disablePlugin(this)
            return
        }

        url = tunnel.publicUrl
        logger.info(url)
    }

    override fun onDisable() {
        ngrokClient.disconnect(url)
    }
}
