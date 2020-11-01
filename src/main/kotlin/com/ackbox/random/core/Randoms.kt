package com.ackbox.random.core

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import kotlin.random.Random

internal object Randoms {

    private const val CHAR_ASCII_START = 97
    private const val CHAR_ASCII_END = 122
    private const val MAX_EXCEPT_HELPER_ATTEMPTS = 10
    private const val STRING_LENGTH = 30

    fun char(): Char = positiveIntegerInRange(CHAR_ASCII_START, CHAR_ASCII_END + 1).toChar()

    fun boolean(): Boolean = Random.nextBoolean()

    fun positiveByte(): Byte = Random.nextInt(0, Byte.MAX_VALUE.toInt()).toByte()

    fun positiveShort(): Short = Random.nextInt(0, Short.MAX_VALUE.toInt()).toShort()

    fun positiveInteger(): Int = Random.nextInt(0, Int.MAX_VALUE)

    fun positiveLong(): Long = Random.nextLong(0, Long.MAX_VALUE)

    fun positiveFloat(): Float = Random.nextDouble(0.toDouble(), Float.MAX_VALUE.toDouble()).toFloat()

    fun positiveDouble(): Double = Random.nextDouble(0.0, Double.MAX_VALUE)

    fun byteBuffer(): ByteBuffer = ByteBuffer.wrap(string().toByteArray(StandardCharsets.UTF_8))

    fun byteArray(): ByteArray = byteArray(STRING_LENGTH)

    fun string(): String = string(STRING_LENGTH)

    fun string(length: Int): String {
        return (1 until length).map { char() }.joinToString(separator = "") { "$it" }
    }

    fun stringExcluding(vararg exclusions: String): String =
        stringExcluding(STRING_LENGTH, *exclusions)

    fun stringExcluding(length: Int, vararg exclusions: String): String =
        exclusionHelper(*exclusions) { string(length) }

    fun byteInRange(minInclusive: Byte, maxExclusive: Byte): Byte {
        val range = maxExclusive.toLong() - minInclusive.toLong()
        check(range > 0 && range <= Byte.MAX_VALUE) { "Desired range must be at least 1 and at most Byte.MAX_VALUE" }
        return Random.nextInt(minInclusive.toInt(), maxExclusive.toInt()).toByte()
    }

    fun positiveByteExcluding(vararg exclusions: Byte): Byte =
        exclusionHelper(*exclusions.toTypedArray()) { positiveByte() }.toByte()

    fun positiveShortInRange(minInclusive: Short, maxExclusive: Short): Short {
        val range = maxExclusive.toLong() - minInclusive.toLong()
        check(range > 0 && range <= Short.MAX_VALUE) { "Desired range must be at least 1 and at most Short.MAX_VALUE" }
        return Random.nextInt(minInclusive.toInt(), maxExclusive.toInt()).toShort()
    }

    fun positiveShortExcluding(vararg exclusions: Short): Short =
        exclusionHelper(*exclusions.toTypedArray()) { positiveShort() }.toShort()

    fun positiveIntegerInRange(minInclusive: Int, maxExclusive: Int): Int {
        val range = maxExclusive.toLong() - minInclusive.toLong()
        check(range > 0 && range <= Int.MAX_VALUE) { "Desired range must be at least 1 and at most Integer.MAX_VALUE" }
        return Random.nextInt(minInclusive, maxExclusive)
    }

    fun positiveIntegerExcluding(vararg exclusions: Int): Int =
        exclusionHelper(*exclusions.toTypedArray()) { positiveInteger() }

    fun positiveLongInRange(minInclusive: Long, maxExclusive: Long): Long {
        val range = maxExclusive - minInclusive
        check(range > 0) { "Desired range must greater then 0" }
        return Random.nextLong(minInclusive, maxExclusive)
    }

    fun positiveLongExcluding(vararg exclusions: Long): Long =
        exclusionHelper(*exclusions.toTypedArray()) { positiveLong() }

    fun positiveFloatInRange(minInclusive: Float, maxExclusive: Float): Float {
        val range = maxExclusive - minInclusive
        check(range > 0) { "Desired range must greater then 0" }
        return Random.nextDouble(minInclusive.toDouble(), maxExclusive.toDouble()).toFloat()
    }

    fun positiveFloatExcluding(vararg exclusions: Float): Float =
        exclusionHelper(*exclusions.toTypedArray()) { positiveFloat() }

    fun positiveDoubleInRange(minInclusive: Double, maxExclusive: Double): Double {
        val range = maxExclusive - minInclusive
        check(range > 0) { "Desired range must greater then 0" }
        return Random.nextDouble(minInclusive, maxExclusive)
    }

    fun positiveDoubleExcluding(vararg exclusions: Double): Double =
        exclusionHelper(*exclusions.toTypedArray()) { positiveDouble() }

    fun byteBufferExcluding(vararg exclusions: ByteBuffer): ByteBuffer =
        exclusionHelper(*exclusions) { byteBuffer() }

    fun byteArray(length: Int): ByteArray = Random.nextBytes(length)

    fun <T> enumValue(clazz: Class<T>): T {
        check(clazz.isEnum) { "Argument must be an enum class" }
        return clazz.enumConstants[Random.nextInt(0, clazz.enumConstants.size)]
    }

    @SafeVarargs
    fun <T : Enum<T>> enumValueExcluding(clazz: Class<T>, vararg exclusions: T): T =
        exclusionHelper(*exclusions) { enumValue(clazz) }

    @SafeVarargs
    private fun <T> exclusionHelper(vararg exclusions: T, valueSupplier: () -> T): T {
        return (0..MAX_EXCEPT_HELPER_ATTEMPTS.toLong())
            .map { valueSupplier() }
            .first { x -> !setOf(*exclusions).contains(x) }
            ?: throw IllegalArgumentException()
    }
}
