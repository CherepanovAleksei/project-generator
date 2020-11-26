package org.jetbrains

import java.io.File



import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun main(args: Array<String>) {
//    ConfigGenerator().generate()
    ProjectGenerator().generate()
}

class ProjectGenerator {
    fun generate() {
        val projectDir = File(System.getProperty("user.dir")).also {
            if (!it.isDirectory) throw Exception("Could not get project dir!")
        }

        val outDir = File(projectDir, "out")
        if (outDir.exists()) outDir.deleteRecursively()

        val newProjectFolder = File(outDir, "myProject")
        newProjectFolder.mkdirs()

        val configFile = File(projectDir, "config.json")
        val projects = parseConfig(configFile)
        createParentProject(newProjectFolder, projects.keys)
        for (project in projects) {

            val projectName = project.key
            val modulePath = File(newProjectFolder, projectName).also { it ->
                it.mkdirs()
            }
            createModule(projectName, project.value, modulePath)
        }
        //println("echo -ne '\\n' | mvn archetype:generate -DgroupId=org.baeldung -DartifactId=parent-project".runCommand(outDir))
    }

    private fun createParentProject(root: File, modules: Set<String>) {
        val parentPom = File(root, "pom.xml")
        parentPom.writeText(parentPom(modules))
    }

    private fun parseConfig(configFile: File): Map<String, Project> {
        if (!configFile.exists()) throw Exception("Config does not exist!")
        val jsonString = configFile.readText()

        val gson = Gson()
        val listProject = object : TypeToken<Map<String, Project>>() {}.type

        return gson.fromJson(jsonString, listProject)
    }

    private fun createModule(moduleName: String, project: Project, modulePath: File) {
        project.checkValidity(moduleName)
        val modulePom = File(modulePath, "pom.xml")
        modulePom.writeText(modulePom(moduleName, project))
        if(project.javaSources != null) {
            createJavaSrc(moduleName, project, modulePath)
        }
        if (project.kotlinSources != null) {
            createKotlinSrc(moduleName, project, modulePath)
        }
    }

    private fun createJavaSrc(moduleName: String, project: Project, moduleRoot: File) {
        val moduleJava = File(moduleRoot, "src/main/java/org/example/${moduleName.capitalize()}Java.java")
        moduleJava.parentFile.mkdirs()
        moduleJava.writeText(javaFile(moduleName, project.javaSources?.javaDep ?: emptyList(), project.javaSources?.kotlinDep ?: emptyList()))

        //tests
        val moduleJavaTests = File(moduleRoot, "src/test/java/org/example/${moduleName.capitalize()}JavaTest.java")
        moduleJavaTests.parentFile.mkdirs()
        moduleJavaTests.writeText(javaTestFile(moduleName))
    }

    private fun createKotlinSrc(moduleName: String, project: Project, moduleRoot: File) {
        val moduleKotlin = File(moduleRoot, "src/main/kotlin/org/example/${moduleName.capitalize()}Kotlin.kt")
        moduleKotlin.parentFile.mkdirs()
        moduleKotlin.writeText(kotlinFile(moduleName, project.kotlinSources?.javaDep ?: emptyList(), project.kotlinSources?.kotlinDep ?: emptyList()))

        //tests
        val moduleKotlinTests = File(moduleRoot, "src/test/kotlin/org/example/${moduleName.capitalize()}KotlinTest.kt")
        moduleKotlinTests.parentFile.mkdirs()
        moduleKotlinTests.writeText(kotlinTestFile(moduleName))
    }
}