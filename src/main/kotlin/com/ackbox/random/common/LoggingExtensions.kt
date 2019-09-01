package com.ackbox.random.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified R : Any> R.logger(): Logger {
    return LoggerFactory.getLogger(this::class.java.name.substringBefore("\$Companion"))
}
