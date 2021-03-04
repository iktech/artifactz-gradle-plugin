package io.iktech.artifactz.gradle.plugin.extensions

/**
 * Push Artifact Extension is used to customise if necessary the details that will be sent to the artifactz.io
 * web service when pushArtifact task is executed
 */
open class PushArtifactExtension {
    /**
     * Web Service URL, by default points to the production instance
     */
    var serverUrl = "https://artifactor.artifactz.io"

    /**
     * Access token with correct write artifact rights
     */
    var accessToken: String? = null

    /**
     * Proxy URL if necessary
     */
    var proxyUrl: String? = null

    /**
     * Proxy username if necessary
     */
    var proxyUsername: String? = null

    /**
     * Proxy password if necessary
     */
    var proxyPassword: String? = null

    /**
     * Stage, where task will publish artifact to
     */
    var stage = "Development"

    /**
     * Optional version to push
     */
    var version: String? = null
}