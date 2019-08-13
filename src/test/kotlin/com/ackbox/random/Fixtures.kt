package com.ackbox.random

import java.nio.ByteBuffer

sealed class CharHolder {
    data class Data(val value: Char) : CharHolder()
    class Object(val value: Char) : CharHolder()
}

sealed class BooleanHolder {
    data class Data(val value: Boolean) : BooleanHolder()
    class Object(val value: Boolean) : BooleanHolder()
}

sealed class ByteHolder {
    data class Data(val value: Byte) : ByteHolder()
    class Object(val value: Byte) : ByteHolder()
}

sealed class ShortHolder {
    data class Data(val value: Short) : ShortHolder()
    class Object(val value: Short) : ShortHolder()
}

sealed class IntHolder {
    data class Data(val value: Int) : IntHolder()
    class Object(val value: Int) : IntHolder()
}

sealed class LongHolder {
    data class Data(val value: Long) : LongHolder()
    class Object(val value: Long) : LongHolder()
}

sealed class FloatHolder {
    data class Data(val value: Float) : FloatHolder()
    class Object(val value: Float) : FloatHolder()
}

sealed class DoubleHolder {
    data class Data(val value: Double) : DoubleHolder()
    class Object(val value: Double) : DoubleHolder()
}

sealed class ByteBufferHolder {
    data class Data(val value: ByteBuffer) : ByteBufferHolder()
    class Object(val value: ByteBuffer) : ByteBufferHolder()
}

sealed class ByteArrayHolder {
    data class Data(val value: ByteArray) : ByteArrayHolder() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Data
            if (!value.contentEquals(other.value)) return false
            return true
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    class Object(val value: ByteArray) : ByteArrayHolder()
}

sealed class StringHolder {
    data class Data(val value: String) : StringHolder()
    class Object(val value: String) : StringHolder()
}

sealed class NullableStringHolder {
    data class Data(val value: String?) : NullableStringHolder()
    class Object(val value: String?) : NullableStringHolder()
}

sealed class StringArrayHolder {
    data class Data(val value: Array<String>) : StringArrayHolder() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Data
            if (!value.contentEquals(other.value)) return false
            return true
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    class Object(val value: Array<String>) : StringArrayHolder()
}

sealed class NullableStringArrayHolder {
    data class Data(val value: Array<String?>) : NullableStringArrayHolder() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Data
            if (!value.contentEquals(other.value)) return false
            return true
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    class Object(val value: Array<String?>) : NullableStringArrayHolder()
}

sealed class StringNullableArrayHolder {
    data class Data(val value: Array<String>?) : StringNullableArrayHolder() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Data
            if (value != null) {
                if (other.value == null) return false
                if (!value.contentEquals(other.value)) return false
            } else if (other.value != null) return false
            return true
        }

        override fun hashCode(): Int = value?.contentHashCode() ?: 0
    }

    class Object(val value: Array<String>?) : StringNullableArrayHolder()
}

sealed class StringListHolder {
    data class Data(val value: List<String>) : StringListHolder()
    class Object(val value: List<String>) : StringListHolder()
}

sealed class NullableStringListHolder {
    data class Data(val value: List<String?>) : NullableStringListHolder()
    class Object(val value: List<String?>) : NullableStringListHolder()
}

sealed class StringNullableListHolder {
    data class Data(val value: List<String>?) : StringNullableListHolder()
    class Object(val value: List<String>?) : StringNullableListHolder()
}

sealed class StringIntMapHolder {
    data class Data(val value: Map<String, Int>) : StringIntMapHolder()
    class Object(val value: Map<String, Int>) : StringIntMapHolder()
}

sealed class NullableStringIntMapHolder {
    data class Data(val value: Map<String?, Int>) : NullableStringIntMapHolder()
    class Object(val value: Map<String?, Int>) : NullableStringIntMapHolder()
}

sealed class StringIntNullableMapHolder {
    data class Data(val value: Map<String, Int>?) : StringIntNullableMapHolder()
    class Object(val value: Map<String, Int>?) : StringIntNullableMapHolder()
}

sealed class Nested {

    data class Data(
        val charValue: Char,
        val booleanValue: Boolean,
        val byteValue: Byte,
        val shortValue: Short,
        val intValue: Int,
        val longValue: Long,
        val floatValue: Float,
        val doubleValue: Double,
        val stringValue: String,
        val byteBufferValue: ByteBuffer,
        val byteArrayValue: ByteArray,
        val arrayString: Array<String>,
        val collectionIntValue: Collection<Int>,
        val listIntValue: List<Int>,
        val mapStringKeyIntValue: Map<String, Int>,
        val enumValue: EnumOptions,
        val nested: StringHolder.Data
    ) : Nested()

    data class Object(
        val charValue: Char,
        val booleanValue: Boolean,
        val byteValue: Byte,
        val shortValue: Short,
        val intValue: Int,
        val longValue: Long,
        val floatValue: Float,
        val doubleValue: Double,
        val stringValue: String,
        val byteBufferValue: ByteBuffer,
        val byteArrayValue: ByteArray,
        val arrayString: Array<String>,
        val collectionIntValue: Collection<Int>,
        val listIntValue: List<Int>,
        val mapStringKeyIntValue: Map<String, Int>,
        val enumValue: EnumOptions,
        val nested: StringHolder.Data
    ) : Nested()
}

enum class EnumOptions { ONE, TWO }

sealed class NestedStringList {

    data class Data(val value: List<List<String>>) : NestedStringList()

    data class Object(val value: List<List<String>>) : NestedStringList()
}

sealed class NestedStringIntMap {

    data class Data(val value: Map<String, List<Int>>) : NestedStringIntMap()

    data class Object(val value: Map<String, List<Int>>) : NestedStringIntMap()
}

sealed class NestedStringArray {

    data class Data(val value: Array<Array<String>>) : NestedStringArray()

    data class Object(val value: Array<Array<String>>) : NestedStringArray()
}
