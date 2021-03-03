package io.iktech.artifactz.gradle.plugin.tasks

import io.artifactz.client.ServiceClientBuilder
import io.artifactz.client.exception.ClientException
import io.iktech.artifactz.gradle.plugin.client.FeedbackImpl
import io.iktech.artifactz.gradle.plugin.extensions.PushArtifactExtension
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Push artifact task can be used to push artifact through the flow in artifactz.io service
 * @param extension task configuration
 */
open class PushArtifactTask @Inject constructor(
        private val extension: PushArtifactExtension
        ) : DefaultTask() {
    private val logger = LoggerFactory.getLogger(PushArtifactTask::class.java)

    @TaskAction
    fun pushArtifact() {
        val client = ServiceClientBuilder
            .withBaseUrl(extension.serverUrl)
            .withApiToken(extension.accessToken)
            .withProxyUrl(extension.proxyUrl)
            .withProxyUsername(extension.proxyUsername)
            .withProxyPassword(extension.proxyPassword)
            .withSender("gradle-plugin")
            .withUserAgent("Artifactz Gradle Plugin/1.0.0")
            .provideFeedback(FeedbackImpl(logger))
            .build();

        try {
            client.pushArtifact(
                extension.stage,
                project.name,
                extension.version
            )
        } catch (e: ClientException) {
            logger.error("Cannot push artifact ", e)
            throw GradleException("Cannot push artifact: " + e.message)
        }
    }
}