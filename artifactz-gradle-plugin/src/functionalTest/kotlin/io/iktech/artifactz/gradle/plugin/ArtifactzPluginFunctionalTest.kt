/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package io.iktech.artifactz.gradle.plugin

import java.io.File
import org.gradle.testkit.runner.GradleRunner
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * A simple functional test for the 'io.iktech.artifactz' plugin.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ArtifactzPluginFunctionalTest {
    @Test fun `can run build`() {
        val apiToken: String = System.getProperty("apiToken")
        // Setup the test build
        val projectDir = File("build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("gradle.properties").writeText("""
artifactz_serverUrl=https://api.uat.artifactz.io
artifactz_apiToken=$apiToken
            """)
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText("""
plugins {
    id('io.iktech.artifactz')
    id('java')
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.apache.logging.log4j:log4j-core'
    implementation 'org.apache.logging.log4j:log4j-api'
}
        """)

        val sourceDir = File(projectDir, "src/main/java/io/iktech/test")
        sourceDir.mkdirs()

        sourceDir.resolve("Test.java").writeText("""
package io.iktech.test;

public class Test {
    public static void main(String[] args) {
        System.out.println("Hello world!");                   
    }
}
        """.trimIndent())
        // Run the build
        val runner = GradleRunner.create()
        runner.withDebug(true)
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("build")
        runner.withProjectDir(projectDir)
        runner.build()
    }

    @Test fun `can run publshArtifact task`() {
        val apiToken: String = System.getProperty("apiToken")
        // Setup the test build
        val projectDir = File("build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("gradle.properties").writeText("""
artifactz_serverUrl=https://api.uat.artifactz.io
artifactz_apiToken=$apiToken
            """)
        projectDir.resolve("settings.gradle").writeText("""
rootProject.name = 'gradle-plugin-test'
        """.trimIndent())
        projectDir.resolve("build.gradle").writeText("""
plugins {
    id('io.iktech.artifactz')
    id('java')
}

group = 'io.iktech'
version = '1.0.0-SNAPSHOT'

repositories {
    mavenCentral()
}

publishArtifact {
    description = 'Gradle plugin test artifact'
    flow = 'Test'
}

dependencies {
    implementation 'org.apache.logging.log4j:log4j-core'
    implementation 'org.apache.logging.log4j:log4j-api'
}
        """)

        val sourceDir = File(projectDir, "src/main/java/io/iktech/test")
        sourceDir.mkdirs()

        sourceDir.resolve("Test.java").writeText("""
            package io.iktech.test;
            
            public class Test {
                public static void main(String[] args) {
                    System.out.println("Hello world!");                   
                }
            }
        """.trimIndent())
        // Run the build
        val runner = GradleRunner.create()
        runner.withDebug(true)
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("publishArtifact")
        runner.withProjectDir(projectDir)
        runner.build()
    }

    @Test fun `can run pushArtifact task`() {
        val apiToken: String = System.getProperty("apiToken")
        // Setup the test build
        val projectDir = File("build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("gradle.properties").writeText("""
artifactzServerUrl=https://api.uat.artifactz.io
artifactzApiToken=$apiToken
            """)
        projectDir.resolve("settings.gradle").writeText("""
rootProject.name = 'gradle-plugin-test'
        """.trimIndent())
        projectDir.resolve("build.gradle").writeText("""
plugins {
    id('io.iktech.artifactz')
    id('java')
}

group = 'io.iktech'
version = '1.0.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.apache.logging.log4j:log4j-core'
    implementation 'org.apache.logging.log4j:log4j-api'
}
        """)

        val sourceDir = File(projectDir, "src/main/java/io/iktech/test")
        sourceDir.mkdirs()

        sourceDir.resolve("Test.java").writeText("""
            package io.iktech.test;
            
            public class Test {
                public static void main(String[] args) {
                    System.out.println("Hello world!");                   
                }
            }
        """.trimIndent())
        // Run the build
        val runner = GradleRunner.create()
        runner.withDebug(true)
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("pushArtifact")
        runner.withProjectDir(projectDir)
        runner.build()
    }
}
