package com.ackbox.random

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class KRandomPrimitivesTest {

    @Test
    internal fun `should generate random primitive or string value`() {
        assertNotNull(krandom<Char>())
        assertNotNull(krandom<Boolean>())
        assertNotNull(krandom<Byte>())
        assertNotNull(krandom<Short>())
        assertNotNull(krandom<Int>())
        assertNotNull(krandom<Long>())
        assertNotNull(krandom<Float>())
        assertNotNull(krandom<Double>())
        assertNotNull(krandom<String>())
    }

    @Test
    internal fun `should generate random generic objects`() {
        assertNotNull(krandom<List<String>>())
        assertNotNull(krandom<Map<String, Int>>())
    }

    @Test
    internal fun `should generate random array objects`() {
        assertNotNull(krandom<Array<String>>())
    }

    @Test
    internal fun `should generate random nested generics objects`() {
        assertNotNull(krandom<List<List<String>>>())
        assertNotNull(krandom<Map<String, Map<String, Int>>>())
    }

    @Test
    @Disabled("Unsupported for now")
    internal fun `should generate random nested array objects`() {
        assertNotNull(krandom<Array<Array<String>>>())
    }
}
