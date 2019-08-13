package com.ackbox.random

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class KRandomNestedContainerTest {

    @Test
    internal fun `should generate random nested objects`() {
        assertNotNull(krandom<Nested.Data>())
        assertNotNull(krandom<Nested.Object>())
    }

    @Test
    internal fun `should generate random nested generic objects`() {
        assertNotNull(krandom<NestedStringList.Data>())
        assertNotNull(krandom<NestedStringList.Object>())
        assertNotNull(krandom<NestedStringIntMap.Data>())
        assertNotNull(krandom<NestedStringIntMap.Object>())
    }

    @Test
    @Disabled("Unsupported for now")
    internal fun `should generate random nested array objects`() {
        assertNotNull(krandom<NestedStringArray.Data>())
        assertNotNull(krandom<NestedStringArray.Object>())
    }
}
