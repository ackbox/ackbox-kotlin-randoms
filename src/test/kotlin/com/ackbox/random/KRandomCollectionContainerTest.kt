package com.ackbox.random

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class KRandomCollectionContainerTest {

    @Test
    internal fun `should generate random array of objects`() {
        assertNotNull(krandom<StringArrayHolder.Data>())
        assertNotNull(krandom<StringArrayHolder.Object>())
    }

    @Test
    internal fun `should generate random array of nullable objects`() {
        assertNotNull(krandom<NullableStringArrayHolder.Data>())
        assertNotNull(krandom<NullableStringArrayHolder.Object>())
    }

    @Test
    internal fun `should generate random nullable array of objects`() {
        assertNotNull(krandom<StringNullableArrayHolder.Data>())
        assertNotNull(krandom<StringNullableArrayHolder.Object>())
    }

    @Test
    internal fun `should generate random list of objects`() {
        assertNotNull(krandom<StringListHolder.Data>())
        assertNotNull(krandom<StringListHolder.Object>())
    }

    @Test
    internal fun `should generate random list of nullable objects`() {
        assertNotNull(krandom<NullableStringListHolder.Data>())
        assertNotNull(krandom<NullableStringListHolder.Object>())
    }

    @Test
    internal fun `should generate random nullable list of objects`() {
        assertNotNull(krandom<StringNullableListHolder.Data>())
        assertNotNull(krandom<StringNullableListHolder.Object>())
    }

    @Test
    internal fun `should generate random map of objects`() {
        assertNotNull(krandom<StringIntMapHolder.Data>())
        assertNotNull(krandom<StringIntMapHolder.Object>())
    }

    @Test
    internal fun `should generate random map of nullable objects`() {
        assertNotNull(krandom<NullableStringIntMapHolder.Data>())
        assertNotNull(krandom<NullableStringIntMapHolder.Object>())
    }

    @Test
    internal fun `should generate random nullable map of objects`() {
        assertNotNull(krandom<StringIntNullableMapHolder.Data>())
        assertNotNull(krandom<StringIntNullableMapHolder.Object>())
    }
}
