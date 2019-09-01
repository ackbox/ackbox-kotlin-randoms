package com.ackbox.random

import java.nio.ByteBuffer
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.cast
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

@Suppress("UNCHECKED_CAST")
sealed class TypeToken() {

    override fun toString(): String = this::class.simpleName!!

    object CharType : TypeToken()

    object BooleanType : TypeToken()

    object ByteType : TypeToken()

    object ShortType : TypeToken()

    object IntType : TypeToken()

    object LongType : TypeToken()

    object FloatType : TypeToken()

    object DoubleType : TypeToken()

    object StringType : TypeToken()

    object ByteBufferType : TypeToken()

    object ByteArrayType : TypeToken()

    class EnumType(val clazz: Class<out EnumType>) : TypeToken() {

        fun cast(obj: Any?): EnumType = clazz.cast(obj)!!
    }

    class ArrayType(val type: KType) : TypeToken() {

        val elementType: KType = type.arguments.first().type!!

        fun newInstance(elements: Collection<Any?>): Any {
            val typeToken = valueOf(elementType)
            return when (typeToken) {
                is CharType -> elements.map { it as Char }.toCharArray()
                is BooleanType -> elements.map { it as Boolean }.toBooleanArray()
                is ByteType -> elements.map { it as Byte }.toByteArray()
                is ShortType -> elements.map { it as Short }.toShortArray()
                is IntType -> elements.map { it as Int }.toIntArray()
                is LongType -> elements.map { it as Long }.toLongArray()
                is FloatType -> elements.map { it as Float }.toFloatArray()
                is DoubleType -> elements.map { it as Double }.toDoubleArray()
                is StringType -> elements.map { it as String }.toTypedArray()
                is ByteBufferType -> elements.map { it as ByteBuffer }.toTypedArray()
                is ByteArrayType -> elements.map { it as ByteArray }.toTypedArray()
                is EnumType -> elements.map { typeToken.cast(it) }.toTypedArray()
                is ArrayType -> elements.map { it as Array<*> }.toTypedArray()
                is ListType -> elements.map { it as List<*> }.toTypedArray()
                is MapType -> elements.map { it as Map<*, *> }.toTypedArray()
                is ObjectType -> elements.map { typeToken.cast(it) }.toTypedArray()
            }
        }
    }

    class ListType(val type: KType) : TypeToken() {

        val elementType: KType = type.arguments.first().type!!

        fun newInstance(elements: Collection<Any?>): List<Any?> = listOf(*elements.toTypedArray())
    }

    class MapType(val type: KType) : TypeToken() {

        val keyType: KType = type.arguments.first().type!!
        val valueType: KType = type.arguments.last().type!!

        fun newInstance(entries: Collection<Pair<Any, Any?>>): Map<Any, Any?> = mapOf(*entries.toTypedArray())
    }

    class ObjectType(val type: KType) : TypeToken() {

        private val clazz: KClass<*> = type.classifier as KClass<*>
        private val primaryConstructor: KFunction<Any> = clazz.primaryConstructor!!
        val constructorArguments: List<KParameter> = primaryConstructor.parameters

        fun newInstance(args: Map<KParameter, Any?>): Any = primaryConstructor.callBy(args)

        fun cast(obj: Any?): Any = clazz.cast(obj)
    }

    companion object {

        fun valueOf(type: KType): TypeToken {
            val clazz = type.classifier as KClass<*>
            return when {
                clazz == Char::class -> CharType
                clazz == Boolean::class -> BooleanType
                clazz == Byte::class -> ByteType
                clazz == Short::class -> ShortType
                clazz == Int::class -> IntType
                clazz == Long::class -> LongType
                clazz == Float::class -> FloatType
                clazz == Double::class -> DoubleType
                clazz == String::class -> StringType
                clazz == ByteBuffer::class -> ByteBufferType
                clazz == ByteArray::class -> ByteArrayType
                clazz.java.isArray -> ArrayType(type)
                clazz.java.isEnum -> EnumType(clazz.java as Class<out EnumType>)
                clazz.isSubclassOf(Collection::class) || clazz.isSubclassOf(List::class) -> ListType(type)
                clazz.isSubclassOf(Map::class) -> MapType(type)
                else -> ObjectType(type)
            }
        }
    }
}
