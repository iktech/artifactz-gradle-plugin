package io.iktech.artifactz.gradle.plugin.tasks

import io.artifactz.client.ServiceClientBuilder
import io.artifactz.client.exception.ClientException
import io.iktech.artifactz.gradle.plugin.client.FeedbackImpl
import io.iktech.artifactz.gradle.plugin.extensions.PublishArtifactExtension
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Push artifact task can be used to publish artifact details in artifactz.io service
 * @param extension task configuration
 */
open class PublishArtifactTask @Inject constructor(
    private val extension: PublishArtifactExtension
) : DefaultTask() {
    private val logger = LoggerFactory.getLogger(PublishArtifactTask::class.java)

    @TaskAction
    fun processDependencies() {
        val client = ServiceClientBuilder(extension.serverUrl, extension.accessToken)
            .withProxyUrl(extension.proxyUrl)
            .withProxyUsername(extension.proxyUsername)
            .withProxyPassword(extension.proxyPassword)
            .withSender("gradle-plugin")
            .withUserAgent("Artifactz Gradle Plugin/1.0.0")
            .provideFeedback(FeedbackImpl(logger))
            .build();

        if (extension.type.uppercase() != "JAR" &&
            extension.type.uppercase() != "WAR" &&
            extension.type.uppercase() != "EAR" &&
            extension.type != "DockerImage") {
            throw GradleException("Incorrect artifact type specified: " + extension.type)
        }

        var groupId: String? = null
        var artifactId: String? = null
        if (extension.type.uppercase() == "JAR" || extension.type.uppercase() == "WAR" || extension.type.uppercase() == "EAR") {
            groupId = project.group as String
            artifactId = project.name
        }

        try {
            client.publishArtifact(
                extension.stage,
                extension.stageDescription,
                project.name,
                extension.description,
                extension.flow,
                extension.type,
                groupId,
                artifactId,
                project.version as String
            )
        } catch (e: ClientException) {
            logger.error("Cannot publish artifact ", e)
            throw GradleException("Cannot publish artifact: " + e.message)
        }
    }
}