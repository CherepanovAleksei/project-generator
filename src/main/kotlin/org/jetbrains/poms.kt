package org.jetbrains

val parentPom: (Set<String>) -> String = { modules ->
    var modulesXML = ""
    for(module in modules) {
        modulesXML += "            <module>$module</module>\n"
    }

    """
        <?xml version="1.0" encoding="UTF-8" standalone="no"?>
        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

          <modelVersion>4.0.0</modelVersion>

          <groupId>org.example</groupId>
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

    """.trimIndent()
}

val modulePom: (String, Project) -> String = { name, project ->
    //TODO add dependencies
    val dependencies = """
${if (project.hasKotlin) """
        <dependency>
          <groupId>org.jetbrains.kotlin</groupId>
          <artifactId>kotlin-stdlib</artifactId>
          <version>${'$'}{kotlin.version}</version>
        </dependency>
""".trimIndent() else ""}
    """.trimIndent()

    """
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <artifactId>parent-project</artifactId>
    <groupId>org.example</groupId>
    <version>1.0-SNAPSHOT</version>
  </parent>

  <groupId>org.example</groupId>
  <artifactId>$name</artifactId>
  <version>1.0-SNAPSHOT</version>

  <name>$name</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.7</maven.compiler.source>
    <maven.compiler.target>1.7</maven.compiler.target>
${if (project.hasKotlin) "    <kotlin.version>1.4.10</kotlin.version>" else ""}
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
$dependencies
    </dependencies>
        
${buildXML(project.hasKotlin)}
</project>
    """.trimIndent()
}

val buildXML = {
    isKotlin: Boolean ->
    if (isKotlin)
        """
  <build>
    <sourceDirectory>${'$'}{project.basedir}/src/main/kotlin</sourceDirectory>
    <testSourceDirectory>${'$'}{project.basedir}/src/test/kotlin</testSourceDirectory>

    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
      <plugins>
        <!-- clean lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#clean_Lifecycle -->
        <plugin>
          <artifactId>maven-clean-plugin</artifactId>
          <version>3.1.0</version>
        </plugin>
        <!-- default lifecycle, jar packaging: see https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_jar_packaging -->
        <plugin>
          <groupId>org.jetbrains.kotlin</groupId>
          <artifactId>kotlin-maven-plugin</artifactId>
          <version>${'$'}{kotlin.version}</version>
          <executions>
            <execution>
              <id>compile</id>
              <goals>
                <goal>compile</goal>
              </goals>
              <configuration>
                <sourceDirs>
                  <sourceDir>${'$'}{project.basedir}/src/main/kotlin</sourceDir>
                  <sourceDir>${'$'}{project.basedir}/src/main/java</sourceDir>
                </sourceDirs>
              </configuration>
            </execution>
            <execution>
              <id>test-compile</id>
              <goals> <goal>test-compile</goal> </goals>
              <configuration>
                <sourceDirs>
                  <sourceDir>${'$'}{project.basedir}/src/test/kotlin</sourceDir>
                  <sourceDir>${'$'}{project.basedir}/src/test/java</sourceDir>
                </sourceDirs>
              </configuration>
            </execution>
          </executions>
        </plugin>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.5.1</version>
          <executions>
            <!-- Replacing default-compile as it is treated specially by maven -->
            <execution>
              <id>default-compile</id>
              <phase>none</phase>
            </execution>
            <!-- Replacing default-testCompile as it is treated specially by maven -->
            <execution>
              <id>default-testCompile</id>
              <phase>none</phase>
            </execution>
            <execution>
              <id>java-compile</id>
              <phase>compile</phase>
              <goals>
                <goal>compile</goal>
              </goals>
            </execution>
            <execution>
              <id>java-test-compile</id>
              <phase>test-compile</phase>
              <goals>
                <goal>testCompile</goal>
              </goals>
              <configuration>
                <skip>false</skip>
              </configuration>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
        """.trimIndent()
    else
        """
          <build>
            <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
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
        """.trimIndent()
}

val javaFile = {
    name: String ->

    """
        package org.example;

        public class ${name.capitalize()}
        {
            public static void sayHello() {
                System.out.println( "Hi, ${name.capitalize()}, from Java!" );
            }
        }
        
    """.trimIndent()
}

val javaTestFile = {
        name: String ->

    """
package org.example;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ${name.capitalize()}Test 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
}

    """.trimIndent()
}

val kotlinFile = {
        name: String ->

    """
package org.example

class ${name.capitalize()} {
    fun sayHello(){
        println("Hi, ${name.capitalize()} from Kotlin!")
    }
}
    """.trimIndent()
}

val kotlinTestFile = {
        name: String ->

    """
package org.example

import org.junit.Test
import kotlin.test.assertEquals

class ${name.capitalize()}Test 
    @Test
    fun thingsShouldWork() {
        assertEquals(listOf(1,2,3).reversed(), listOf(3,2,1))
    }
}

    """.trimIndent()
}