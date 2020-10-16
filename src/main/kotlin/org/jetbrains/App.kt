package org.jetbrains

import java.io.File



import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun createParentProject(root:File, modules: Set<String>) {
    val parentPom = File(root, "pom.xml")
    parentPom.writeText(parentPom(modules))
}

fun main(args: Array<String>) {

    val projectDir = File(System.getProperty("user.dir")).also {
        if(!it.isDirectory) throw Exception("Could not get project dir!")
    }

    val outDir = File(projectDir, "out")
    if(outDir.exists()) outDir.deleteRecursively()

    val newProjectFolder = File(outDir, "myProject")
    newProjectFolder.mkdirs()

    val configFile = File(projectDir, "config.json")
    val projects = parseConfig(configFile)
    createParentProject(newProjectFolder, projects.keys)
    for(project in projects) {
        val projectName = project.key
        val modulePath = File(newProjectFolder, projectName).also { it ->
            it.mkdirs()
        }
        createModule(projectName, project.value, modulePath)
    }
    //println("echo -ne '\\n' | mvn archetype:generate -DgroupId=org.baeldung -DartifactId=parent-project".runCommand(outDir))
}


fun parseConfig(configFile: File): Map<String, Project> {
    if(!configFile.exists()) throw Exception("Config does not exist!")
    val jsonString = configFile.readText()

    val gson = Gson()
    val listProject = object : TypeToken<Map<String, Project>>() {}.type

    return gson.fromJson(jsonString, listProject)
}

private fun createSrc(root: File){

}

fun createModule(moduleName: String, project: Project, modulePath: File) {
    val modulePom = File(modulePath, "pom.xml")
    modulePom.writeText(modulePom(moduleName, project))
    createJavaSrc(moduleName, modulePath)
    if(project.hasKotlin) {
        createKotlinSrc(moduleName, modulePath)
    }
}

fun createJavaSrc(moduleName: String, moduleRoot: File) {
    val moduleJava = File(moduleRoot, "src/main/java/org/example/${moduleName.capitalize()}.java")
    moduleJava.parentFile.mkdirs()
    moduleJava.writeText(javaFile(moduleName))

    //tests
    val moduleJavaTests = File(moduleRoot, "src/test/java/org/example/${moduleName.capitalize()}Test.java")
    moduleJavaTests.parentFile.mkdirs()
    moduleJavaTests.writeText(javaTestFile(moduleName))
}

fun createKotlinSrc(moduleName: String, moduleRoot: File) {
    val moduleKotlin = File(moduleRoot, "src/main/kotlin/org/example/${moduleName.capitalize()}.kt")
    moduleKotlin.parentFile.mkdirs()
    moduleKotlin.writeText(kotlinFile(moduleName))

    //tests
    val moduleKotlinTests = File(moduleRoot, "src/test/kotlin/org/example/${moduleName.capitalize()}Test.kt")
    moduleKotlinTests.parentFile.mkdirs()
    moduleKotlinTests.writeText(kotlinTestFile(moduleName))
}