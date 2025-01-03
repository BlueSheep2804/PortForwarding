package io.github.bluesheep2804.portforwarding

import com.charleskorn.kaml.YamlComment
import com.github.alexdlaird.ngrok.protocol.Region
import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
        @YamlComment("ngrok authtoken")
        val authToken: String = "AUTHTOKEN",
        @YamlComment("Port number to be forwarded")
        val port: Int = 25565,
        @YamlComment(
                "Region to be forwarded",
                "Valid values: [ap, au, eu, in, jp, sa, us, us-cal-1]."
        )
        val region: Region = Region.US
)
