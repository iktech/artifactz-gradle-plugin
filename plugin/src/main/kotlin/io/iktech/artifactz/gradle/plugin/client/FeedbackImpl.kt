package io.iktech.artifactz.gradle.plugin.client

import io.artifactz.client.Feedback
import io.artifactz.client.FeedbackLevel
import org.slf4j.Logger
import javax.inject.Inject

/**
 * Plugin implementation of the Service Client's library Feedback interface
 * @param logger the logger instance to use to report client's feedback
 * @see Feedback
 */
class FeedbackImpl @Inject constructor(private val logger: Logger): Feedback {
    override fun send(level: FeedbackLevel?, message: String?) {
        if (message != null) {
            when (level) {
                FeedbackLevel.DEBUG ->
                    logger.debug(message)
                FeedbackLevel.INFO ->
                    logger.info(message)
                FeedbackLevel.WARNING ->
                    logger.warn(message)
                else ->
                    logger.error(message)
            }
        }
    }
}