package com.ackbox.random

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

internal class KRandomFactoryValuesTest {

    @Test
    internal fun `should generate string instances from factory`() {
        val factory = { STRING }
        val config = KRandomConfig(typeFactories = mapOf(String::class to factory))
        val subject = krandom<FactoryObject>(config)
        assertEquals(factory.invoke(), subject.stringValue)
    }

    @Test
    internal fun `should generate int instances from factory`() {
        val factory = { INT }
        val config = KRandomConfig(typeFactories = mapOf(Int::class to factory))
        val subject = krandom<FactoryObject>(config)
        assertEquals(factory.invoke(), subject.intValue)
    }

    @Test
    internal fun `should generate enum instances from factory`() {
        val factory = { ENUM }
        val config = KRandomConfig(typeFactories = mapOf(EnumOptions::class to factory))
        val subject = krandom<FactoryObject>(config)
        assertEquals(factory.invoke(), subject.enumValue)
    }

    @Test
    internal fun `should generate object instances from factory`() {
        val factory = { OBJECT }
        val config = KRandomConfig(typeFactories = mapOf(Any::class to factory))
        val subject = krandom<FactoryObject>(config)
        assertEquals(factory.invoke(), subject.anyValue)
    }

    companion object {

        private const val STRING = "STRING"
        private const val INT = 10
        private val ENUM = EnumOptions.ONE
        private val OBJECT = Any()
    }
}
