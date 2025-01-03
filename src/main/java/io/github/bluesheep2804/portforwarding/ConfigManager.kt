package io.github.bluesheep2804.portforwarding

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ConfigManager(private val file: File) {
    lateinit var config: ConfigData
    private val yamlConfiguration = YamlConfiguration(strictMode = false, breakScalarsAt = 120)
    private val yaml = Yaml(configuration = yamlConfiguration)
    val defaultConfig = ConfigData()

    init {
        reload()
    }

    fun reload() {
        val configFile = File(file, "config.yml")
        if (!file.exists()) {
            file.mkdir()
        }
        if (!configFile.exists()) {
            configFile.createNewFile()
            val config = ConfigData()
            val output = FileOutputStream(configFile)
            yaml.encodeToStream(ConfigData.serializer(), config, output)
        }
        val configFileInputStream = FileInputStream(configFile)
        config = yaml.decodeFromStream(ConfigData.serializer(), configFileInputStream)
    }
}