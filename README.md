# Artifactz.io gradle plugin

Gradle plugin to manage dependencies using artifactz.io service.
It provides the ability to complete dependencies with missing version
if they are registered at the particular stage at the artifactz.io service.

## Usage
To resolve the artifact version include the plugin into the list of the applied
plugins of the gradle project, for example.
```groovy
plugins {
    id 'io.iktech.artifactz' version '1.0.0'
    id 'java'
//  ...    
}
```

Next, in the dependencies section leave versions of the artifacts that you want to be resolved via artifactz.io service empty.

For example:
```groovy
dependencies {
    implementation 'org.apache.logging.log4j:log4j-core'
    implementation 'org.apache.logging.log4j:log4j-api'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.6.0'
    testImplementation 'org.mockito:mockito-junit-jupiter:3.7.7'
    testImplementation 'org.mockito:mockito-core:3.7.7'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine'
}
```

In order for the plugin to work the following gradle properties should be defined:

Name | Description
---|---
artifactz.serverUrl | The artifactz.io web service URL. By default, if omitted it points to https://artifactor.artifactz.io
artifactz.stage | The stage name from where plugin will try to get the artifact version
artifactz.apiToken | The artifactz.io API token allowing to read artifactz (for resolving only), or modify artifactz in order to run the publishArtifact and pushArtifact tasks
artifactz.proxyUrl | The proxy URL if build is executed from behind the proxy
artifactz.proxyUsername | The proxy username if proxy requires authentication
artifactz.proxyPassword | The proxy password if proxy requires authentication

The plugin will try to resolve the above properties in the way they described, i.e. in the form artifactz.<propertyName>, 
with artifactz as a prefix. If it cannot find the properties in the above form plugin will attempt to resolve them in the form
<prefix>_<propertyName>. Eventually, if that fails, plugin will attempt to resolve it as <prefix><PropertyName> with the propertyName
first letter capitalized.

## Tasks
The plugin also provides two additional tasks
`publishArtifact` and `pushArtifact` that allow user to publish artifacts from the gradle build to the artifactz.io service
and push the artifact through the flow from the gradle build respectively.

## `publishArtifact` task
The `publishArtifact` task allows to add or update artifact details and version in the artifactz.io.
The following extension parameters could be configured for the task execution:

Name | Description
---|---
serverUrl | The artifactz.io web service URL. By default, if omitted it points to https://artifactor.artifactz.io. If `artifactz.serverUrl` property is set it will be used.
accessToken | The artifactz.io API token. If it is not set in the extension the system property will be used.
proxyUrl | The proxy URL if build is executed from behind the proxy. If it is not set in the extension the system property will be used.
proxyUsername | The proxy username if proxy requires authentication. If it is not set in the extension the system property will be used.
proxyPassword | The proxy password if proxy requires authentication. If it is not set in the extension the system property will be used.
stage | The stage name where the artifact details will be published. By default, `Development` stage is used.
stageDescription | The stage description to set
description | The artifact description to set
flow | The flow name to attach artifact to
type | The artifact type. Could be 'JAR', 'WAR', 'EAR' and 'DockerImage'. By default, the `JAR` is used.

When artifact published the task uses the project name as the artifact name and artifact ID if artifact is a Java one, i.e. `JAR`, `WAR` or `EAR`.
The artifact `group` will be used as the group ID if artifact is a Java related.
The current version of the artifact will be used during publishing.

To execute the task run the following command:
```shell
./gradlew publishArtifact
```

To task configuration will look like this (in build.gradle):
```groovy
publishArtifact {
    stage = 'Development'
    stageDescription = 'Development Stage'
    description = 'Gradle plugin test artifact'
    flow = 'Test'
    type = 'JAR'
}
```

## `pushArtifact` task
The `pushArtifact` task allows to push artifact associated with the flow in the artifactz.io to the next stage of the flow.
The following extension parameters could be configured for the task execution:

Name | Description
---|---
serverUrl | The artifactz.io web service URL. By default, if omitted it points to https://artifactor.artifactz.io. If `artifactz.serverUrl` property is set it will be used.
accessToken | The artifactz.io API token. If it is not set in the extension the system property will be used.
proxyUrl | The proxy URL if build is executed from behind the proxy. If it is not set in the extension the system property will be used.
proxyUsername | The proxy username if proxy requires authentication. If it is not set in the extension the system property will be used.
proxyPassword | The proxy password if proxy requires authentication. If it is not set in the extension the system property will be used.
stage | The stage name where the artifact details will be published. By default, `Development` stage is used.
version | The version to push. If omitted, the current stage version is pushed.

When artifact pushed the task uses the project name as the artifact name.

To execute the task run the following command:
```shell
./gradlew pushArtifact
```

To task configuration will look like this (in build.gradle):
```groovy
pushArtifact {
    stage = 'UAT'
}
```
