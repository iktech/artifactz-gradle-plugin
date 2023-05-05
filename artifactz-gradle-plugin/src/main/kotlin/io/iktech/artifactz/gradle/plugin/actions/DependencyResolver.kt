package io.iktech.artifactz.gradle.plugin.actions

import io.artifactz.client.ServiceClient
import io.artifactz.client.ServiceClientBuilder
import io.iktech.artifactz.gradle.plugin.client.FeedbackImpl
import org.gradle.api.Action
import org.gradle.api.artifacts.DependencyResolveDetails
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * This action is taking configuration dependencies, detects if any of them are incomplete, i.e. do not have version defined
 * and query artifactor.artifactz.io service in attempt to get the version of the artifact from the specified stage,
 * in case it is registered there.
 * @param serverUrl the artifactz.io web service URL
 * @param accessToken the API token with 'read artifacts' access
 * @param proxyUrl the proxy server URL if required
 * @param proxyUsername the proxy username if proxy is required and if proxy authentication is enabled
 * @param proxyPassword the proxy password if proxy is required and if proxy authentication is enabled
 * @param stage the stage name from where resolver attemts to retrieve version information
 */
class DependencyResolver @Inject constructor(
    serverUrl: String,
    accessToken: String,
    proxyUrl: String?,
    proxyUsername: String?,
    proxyPassword: String?,
    private val stage: String) : Action<DependencyResolveDetails> {
    private val logger = LoggerFactory.getLogger(DependencyResolver::class.java)

    /**
     * Instance of the artifactz.io service client
     */
    private val client: ServiceClient = ServiceClientBuilder(serverUrl, accessToken)
                .withProxyUrl(proxyUrl)
                .withProxyUsername(proxyUsername)
                .withProxyPassword(proxyPassword)
                .withSender("gradle-plugin")
                .withUserAgent("Artifactz Gradle Plugin/1.0.0")
                .provideFeedback(FeedbackImpl(logger))
                .build()

    /**
     * Version cache used by resolver to cache version details for the different configurations to reduce number of calls
     * to the server
     */
    private val cache: HashMap<String, String> = HashMap()

    override fun execute(details: DependencyResolveDetails) {
        logger.info("Processing incomplete dependencies")
        if (details.requested.version == null || details.requested.version!!.trim() == "") {
            // If version is not specified in the dependency, build the java Id by concatenating group and name
            val id = details.requested.group + ":" + details.requested.name
            logger.debug("Trying to retrieve version info for '$id'")
            // First check if the version is already in the cache and set it to the dependency in question if found
            if (cache[id] == null) {
                // Otherwise, call the web service
                logger.debug("Could not find version in the cache, retrieving from artifactz.io")
                val result = client.retrieveJavaVersions(stage, id)
                if (result.artifacts != null && result.artifacts.size == 1) {
                    // If artifact is registered with the service, use the version that was just retrieved and cache it
                    // for the future use
                    val version = result.artifacts[0].version
                    logger.debug("Using artifact '$id:$version")
                    cache[id] = version
                    details.useVersion(version)
                }
            } else {
                val version = cache[id]!!
                logger.debug("Found version in the cache, using artifact '$id:$version'")
                details.useVersion(version)
            }
        }
    }
}