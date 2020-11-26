package org.jetbrains

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileWriter

fun main(args: Array<String>){
    ConfigGenerator().generate()
}

class ConfigGenerator {
    fun generate() {
        val projectDir = File(System.getProperty("user.dir")).also {
            if (!it.isDirectory) throw Exception("Could not get project dir!")
        }

        val configFile = File(projectDir, "easyConfig.json")
        val easyConfig = parseConfig(configFile)

        generateConfig(easyConfig, projectDir)
    }

    fun generateConfig(easyConfig: EasyConfig, projectDir: File) {
        val oldConfig = File(projectDir, "old_config.json")
        if (oldConfig.exists()) oldConfig.delete()

        val newConfig = File(projectDir, "config.json")
        if (newConfig.exists()) newConfig.renameTo(oldConfig)

        val newProjectStructure: MutableMap<String, Project> = mutableMapOf()
        for (moduleNumber in 0 until easyConfig.numberOfModules) {
            var javaSources = Sources()
            var kotlinSources: Sources? = null
//BUG: Sources rewrite each other
            if (easyConfig.haveJavaToKotlinDependencies) {
                val moduleDependencyNumber = moduleNumber + 1
                if (moduleDependencyNumber < easyConfig.numberOfModules)
                    javaSources = Sources(kotlinDep = listOf("Module${moduleDependencyNumber}"))
            }
            if (easyConfig.haveKotlinToJavaDependencies) {
                val moduleDependencyNumber = moduleNumber + 1
                if (moduleDependencyNumber < easyConfig.numberOfModules)
                    kotlinSources = Sources(javaDep = listOf("Module${moduleDependencyNumber}"))
            }
            if (easyConfig.haveKotlinToKotlinDependencies) {
                val moduleDependencyNumber = moduleNumber + 1
                if (moduleDependencyNumber < easyConfig.numberOfModules)
                    kotlinSources = Sources(kotlinDep = listOf("Module${moduleDependencyNumber}"))
            }
            if (easyConfig.haveJavaToJavaDependencies) {
                val moduleDependencyNumber = moduleNumber + 1
                if (moduleDependencyNumber < easyConfig.numberOfModules)
                    javaSources = Sources(javaDep = listOf("Module${moduleDependencyNumber}"))
            }
            newProjectStructure["Module$moduleNumber"] = Project(
                    easyConfig.isAllModulesKotlinBuild,
                    javaSources,
                    kotlinSources
            )
        }
        val gson = GsonBuilder().setPrettyPrinting().create()
        val writer = FileWriter(newConfig)
        gson.toJson(newProjectStructure, writer)
        writer.close()
    }

    private fun parseConfig(configFile: File): EasyConfig {
        if (!configFile.exists()) throw Exception("Config does not exist!")
        val jsonString = configFile.readText()

        val gson = Gson()
        val easyConfigType = object : TypeToken<EasyConfig>() {}.type

        return gson.fromJson(jsonString, easyConfigType)
    }

    data class EasyConfig(
            val numberOfModules: Int = 1,
            val haveKotlinSrc: Boolean = false,
            val haveJavaToKotlinDependencies: Boolean = false,
            val haveKotlinToJavaDependencies: Boolean = false,
            val haveKotlinToKotlinDependencies: Boolean = false,
            val isAllModulesKotlinBuild: Boolean = false,
            val haveJavaToJavaDependencies: Boolean = false
    )
}