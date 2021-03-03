package io.iktech.artifactz.gradle.plugin.actions

import org.gradle.api.Action
import org.gradle.api.artifacts.Configuration
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * The action that is applied to each configuration that attempts to resolve incomplete dependencies.
 * @param serverUrl the artifactz.io web service URL
 * @param accessToken the API token with 'read artifacts' access
 * @param proxyUrl the proxy server URL if required
 * @param proxyUsername the proxy username if proxy is required and if proxy authentication is enabled
 * @param proxyPassword the proxy password if proxy is required and if proxy authentication is enabled
 * @param stage the stage name from where resolver attemts to retrieve version information
 */
class DependencyManagementApplier @Inject constructor(private val serverUrl: String,
                                                      private val accessToken: String,
                                                      private val proxyUrl: String?,
                                                      private val proxyUsername: String?,
                                                      private val proxyPassword: String?,
                                                      private val stage: String): Action<Configuration> {
    /**
     * The shared instance of the dependency resolver to use for all configurations
     */
    private val dependencyResolver = DependencyResolver(serverUrl, accessToken, proxyUrl, proxyUsername, proxyPassword, stage)
    private val logger = LoggerFactory.getLogger(DependencyManagementApplier::class.java)

    override fun execute(configuration: Configuration) {
        logger.info("Applying artifactz plugin to " + configuration.name)
        configuration.resolutionStrategy.eachDependency(dependencyResolver)
    }
}