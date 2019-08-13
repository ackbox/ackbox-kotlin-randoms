package com.ackbox.random

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class KRandomSingleContainerTest {

    @Test
    internal fun `should generate random object with char attributes`() {
        assertNotNull(krandom<CharHolder.Data>())
        assertNotNull(krandom<CharHolder.Object>())
    }

    @Test
    internal fun `should generate random object with boolean attributes`() {
        assertNotNull(krandom<BooleanHolder.Data>())
        assertNotNull(krandom<BooleanHolder.Object>())
    }

    @Test
    internal fun `should generate random object with byte attributes`() {
        assertNotNull(krandom<ByteHolder.Data>())
        assertNotNull(krandom<ByteHolder.Object>())
    }

    @Test
    internal fun `should generate random object with short attributes`() {
        assertNotNull(krandom<ShortHolder.Data>())
        assertNotNull(krandom<ShortHolder.Object>())
    }

    @Test
    internal fun `should generate random object with int attributes`() {
        assertNotNull(krandom<IntHolder.Data>())
        assertNotNull(krandom<IntHolder.Object>())
    }

    @Test
    internal fun `should generate random object with long attributes`() {
        assertNotNull(krandom<LongHolder.Data>())
        assertNotNull(krandom<LongHolder.Object>())
    }

    @Test
    internal fun `should generate random object with float attributes`() {
        assertNotNull(krandom<FloatHolder.Data>())
        assertNotNull(krandom<FloatHolder.Object>())
    }

    @Test
    internal fun `should generate random object with double attributes`() {
        assertNotNull(krandom<DoubleHolder.Data>())
        assertNotNull(krandom<DoubleHolder.Object>())
    }

    @Test
    internal fun `should generate random object with string attributes`() {
        assertNotNull(krandom<StringHolder.Data>())
        assertNotNull(krandom<StringHolder.Object>())
    }

    @Test
    internal fun `should generate random object with byte-buffer attributes`() {
        assertNotNull(krandom<ByteBufferHolder.Data>())
        assertNotNull(krandom<ByteBufferHolder.Object>())
    }

    @Test
    internal fun `should generate random object with byte-array attributes`() {
        assertNotNull(krandom<ByteArrayHolder.Data>())
        assertNotNull(krandom<ByteArrayHolder.Object>())
    }

    @Test
    internal fun `should generate random object with nullable attributes`() {
        assertNotNull(krandom<NullableStringHolder.Object>())
        assertNotNull(krandom<NullableStringHolder.Data>())
    }
}
