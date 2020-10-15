package org.jetbrains

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

fun String.runCommand(workingDir: File): String? {
    try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        return proc.inputStream.bufferedReader().readText()
    } catch(e: IOException) {
        e.printStackTrace()
        return null
    }
}
fun main(args: Array<String>) {
    val projectDir = File(System.getProperty("user.dir")).also {
        if(!it.isDirectory) throw Exception("Could not get project dir!")
    }

    val outDir = File(projectDir, "out")
    if(outDir.exists()) outDir.deleteRecursively()
    outDir.mkdir()

    val newProjectFolder = File(outDir, "myProject")


//    if (rootD)
//    println("ls".runCommand(File(rootDir)))
    //println("Working Directory = $path")
//    val ad = File("$projectDir/src/main/resources/asd")
//    ad.listFiles().forEach {
//        //println(it.name)
//    }
    //Runtime.getRuntime().exec("mvn archetype:generate -DgroupId=org.mrn  -DartifactId=parent-project")
    println("echo -ne '\\n' | mvn archetype:generate -DgroupId=org.baeldung -DartifactId=parent-project".runCommand(outDir))
}

