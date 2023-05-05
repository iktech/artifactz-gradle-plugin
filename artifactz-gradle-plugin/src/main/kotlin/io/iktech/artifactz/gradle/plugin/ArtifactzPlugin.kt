/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package io.iktech.artifactz.gradle.plugin

import io.iktech.artifactz.gradle.plugin.actions.DependencyManagementApplier
import io.iktech.artifactz.gradle.plugin.actions.DependencyResolver
import io.iktech.artifactz.gradle.plugin.extensions.PublishArtifactExtension
import io.iktech.artifactz.gradle.plugin.extensions.PushArtifactExtension
import io.iktech.artifactz.gradle.plugin.tasks.PublishArtifactTask
import io.iktech.artifactz.gradle.plugin.tasks.PushArtifactTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.LoggerFactory

/**
 * The plugin expands incomplete java dependencies in all configurations. Incomplete dependency is the one without version,
 * i.e. when it looks like this `io.iktech.test:test-data` instead of `io.iktech.test:test-data:1.0.0`. The plugin detects
 * such dependencies and tries to find out if their version is registered in the artifactz.io service. If such artifact has
 * been found plugin will apply the version retrieved from the artifactz.io service.
 *
 * By default, it uses 'Development' stage to retrieve versions. If necessary, the stage could be overwritten using properties.
 */
class ArtifactzPlugin: Plugin<Project> {
    private val logger = LoggerFactory.getLogger(DependencyResolver::class.java)
    private val PROPERTY_PREFIX = "artifactz"

    override fun apply(project: Project) {
        // Read the gradle properties that configure connection to the artifactz.io service
        logger.info("******* Applying ArtifactzPlugin")

        // By default, we use production instance of the artifactz.io service
        val serverUrl = readProperty(project, "serverUrl", "https://artifactor.artifactz.io")

        // By default, we try to retrieve version details from the `Development' stage
        val stage = readProperty(project, "stage", "Development")

        // API token is required
        val apiToken = readProperty(project, "apiToken") ?: throw GradleException("Artifactz API Token is not specified")

        // Get proxy details if necessary
        val proxyUrl = readProperty(project, "proxyUrl")
        val proxyUsername = readProperty(project, "proxyUsername")
        val proxyPassword = readProperty(project, "proxyPassword")

        val publishArtifactExtension = project.extensions.create("publishArtifact", PublishArtifactExtension::class.java)
        val pushArtifactExtension = project.extensions.create("pushArtifact", PushArtifactExtension::class.java)

        if (publishArtifactExtension.serverUrl == null) {
            publishArtifactExtension.serverUrl = serverUrl
        }

        if (pushArtifactExtension.serverUrl == null) {
            pushArtifactExtension.serverUrl = serverUrl
        }

        if (publishArtifactExtension.stage == null) {
            publishArtifactExtension.stage = stage
        }

        if (pushArtifactExtension.stage == null) {
            pushArtifactExtension.stage = stage
        }

        if (publishArtifactExtension.accessToken == null) {
            publishArtifactExtension.accessToken = apiToken
        }

        if (publishArtifactExtension.proxyUrl == null) {
            publishArtifactExtension.proxyUrl = proxyUrl
        }

        if (publishArtifactExtension.proxyUsername == null) {
            publishArtifactExtension.proxyUsername = proxyUsername
        }

        if (publishArtifactExtension.proxyPassword == null) {
            publishArtifactExtension.proxyPassword = proxyPassword
        }

        project.tasks.register("publishArtifact", PublishArtifactTask::class.java, publishArtifactExtension)

        if (pushArtifactExtension.accessToken == null) {
            pushArtifactExtension.accessToken = apiToken
        }

        if (pushArtifactExtension.proxyUrl == null) {
            pushArtifactExtension.proxyUrl = proxyUrl
        }

        if (pushArtifactExtension.proxyUsername == null) {
            pushArtifactExtension.proxyUsername = proxyUsername
        }

        if (pushArtifactExtension.proxyPassword == null) {
            pushArtifactExtension.proxyPassword = proxyPassword
        }

        project.tasks.register("pushArtifact", PushArtifactTask::class.java, pushArtifactExtension)

        project.configurations.all(DependencyManagementApplier(serverUrl, apiToken, proxyUrl, proxyUsername, proxyPassword, stage))
    }

    private fun readProperty(project: Project, name: String): String? {
        var property = project.properties["$PROPERTY_PREFIX.$name"] as String?
        if (property == null) {
            property = project.properties[PROPERTY_PREFIX + "_" + name] as String?
            if (property == null) {
                property = project.properties["$PROPERTY_PREFIX${name.replaceFirstChar(Char::uppercase)}"] as String?
            }
        }

        return property
    }

    private fun readProperty(project: Project, name: String, default: String): String {
        var property = readProperty(project, name)
        if (property == null) {
            property = default
        }

        return property
    }
}
