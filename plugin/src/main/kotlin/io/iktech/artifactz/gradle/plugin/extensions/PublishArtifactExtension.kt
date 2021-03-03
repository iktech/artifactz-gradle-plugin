package io.iktech.artifactz.gradle.plugin.extensions

/**
 * Publish Artifact Extension is used to customise if necessary the details that will be sent to the artifactz.io
 * web service when publishArtifact task is executed
 */
open class PublishArtifactExtension {
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
     * Optional stage description
     */
    var stageDescription: String? = null

    /**
     * Optional artifact description
     */
    var description: String? = null

    /**
     * Optional flow to attach to the published artifact
     */
    var flow: String? = null

    /**
     * Artifact type by default it uses JAR
     */
    var type = "JAR"
}