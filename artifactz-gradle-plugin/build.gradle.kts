val tagName = System.getenv("RELEASE_TAG")
group = "io.iktech"
version = tagName ?: "1.1-SNAPSHOT"

plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`

    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.2.1"
}

repositories {
    // Use JCenter for resolving dependencies.
    mavenLocal()
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.iktech:artifactz-client:1.2.2")

    testImplementation(gradleTestKit())
    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

gradlePlugin {
    website.set("https://github.com/iktech/artifactz-gradle-plugin/blob/master/README.md")
    vcsUrl.set("https://github.com/iktech/artifactz-gradle-plugin")
    plugins {
        create("artifactzPlugin") {
            id = "io.iktech.artifactz"
            displayName = "Artifactz.io Plugin"
            description =
                "Plugin allowing dynamically resolve dependencies versions, tracked by the artifactz.io service"
            implementationClass = "io.iktech.artifactz.gradle.plugin.ArtifactzPlugin"
            version = tagName ?: "1.0-SNAPSHOT"
            tags.set(listOf("version", "artifact", "artifactz", "java"))
        }
    }
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    val token = project.properties["apiToken"]
    if (token != null) {
        systemProperty("apiToken", token)
    }
}

tasks.check {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}
