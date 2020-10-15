package org.jetbrains

import java.io.File



import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun createParentProject(root:File, modules: Set<String>) {

    val parentPom = File(root, "pom.xml")

    var modulesXML = ""
    for(module in modules) {
        modulesXML += "            <module>$module</module>\n"
    }

    parentPom.writeText("""
        <?xml version="1.0" encoding="UTF-8" standalone="no"?>
        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

          <modelVersion>4.0.0</modelVersion>

          <groupId>org.mrn</groupId>
          <artifactId>parent-project</artifactId>
          <version>1.0-SNAPSHOT</version>
          <name>parent-project</name>
          
          <!-- FIXME change it to the project's website -->
          <url>http://www.example.com</url>
          <packaging>pom</packaging>
          <properties>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
            <maven.compiler.source>1.7</maven.compiler.source>
            <maven.compiler.target>1.7</maven.compiler.target>
          </properties>
          <dependencies>
            <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.11</version>
              <scope>test</scope>
            </dependency>
          </dependencies>
          
          <build>
            <pluginManagement>
              <!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
              <plugins>
                <!-- clean lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#clean_Lifecycle -->
                <plugin>
                  <artifactId>maven-clean-plugin</artifactId>
                  <version>3.1.0</version>
                </plugin>
                <!-- default lifecycle, jar packaging: see https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_jar_packaging -->
                <plugin>
                  <artifactId>maven-resources-plugin</artifactId>
                  <version>3.0.2</version>
                </plugin>
                <plugin>
                  <artifactId>maven-compiler-plugin</artifactId>
                  <version>3.8.0</version>
                </plugin>
                <plugin>
                  <artifactId>maven-surefire-plugin</artifactId>
                  <version>2.22.1</version>
                </plugin>
                <plugin>
                  <artifactId>maven-jar-plugin</artifactId>
                  <version>3.0.2</version>
                </plugin>
                <plugin>
                  <artifactId>maven-install-plugin</artifactId>
                  <version>2.5.2</version>
                </plugin>
                <plugin>
                  <artifactId>maven-deploy-plugin</artifactId>
                  <version>2.8.2</version>
                </plugin>
                <!-- site lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#site_Lifecycle -->
                <plugin>
                  <artifactId>maven-site-plugin</artifactId>
                  <version>3.7.1</version>
                </plugin>
                <plugin>
                  <artifactId>maven-project-info-reports-plugin</artifactId>
                  <version>3.0.0</version>
                </plugin>
              </plugins>
            </pluginManagement>
          </build>
          
          <modules>
$modulesXML
          </modules>
        </project>

    """.trimIndent())

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
    println("fin")
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