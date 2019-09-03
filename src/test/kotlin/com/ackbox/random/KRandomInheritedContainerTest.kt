package com.ackbox.random

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class KRandomInheritedContainerTest {

    @Test
    internal fun `should generate random single level class object`() {
        assertNotNull(krandom<Level0Object>())
    }

    @Test
    internal fun `should generate random two level class object`() {
        assertNotNull(krandom<Level1Object>())
    }
}
