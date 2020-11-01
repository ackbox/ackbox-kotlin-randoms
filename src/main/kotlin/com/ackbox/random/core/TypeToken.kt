package com.ackbox.random.core

import com.ackbox.random.TypeFactory
import java.nio.ByteBuffer
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.cast
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

@Suppress("UNCHECKED_CAST")
sealed class TypeToken(val type: KType) {

    val clazz: KClass<*> = type.classifier as KClass<*>

    override fun toString(): String = this::class.simpleName!!

    class LeafType(type: KType, private val factory: TypeFactory<*>) : TypeToken(type) {

        fun newInstance(): Any? = factory.invoke()
    }

    class EnumType(type: KType) : TypeToken(type) {

        val enumClazz: Class<out EnumType> = clazz.java as Class<out EnumType>
    }

    class ArrayType(type: KType) : TypeToken(type) {

        val elementType: KType = type.arguments.first().type!!

        fun newInstance(elements: Collection<Any?>): Any {
            val typeToken = valueOf(elementType)
            return when {
                typeToken.clazz == Char::class -> elements.map { it as Char }.toCharArray()
                typeToken is LeafType && typeToken.clazz == Boolean::class -> elements.map { it as Boolean }.toBooleanArray()
                typeToken is LeafType && typeToken.clazz == Byte::class -> elements.map { it as Byte }.toByteArray()
                typeToken is LeafType && typeToken.clazz == Short::class -> elements.map { it as Short }.toShortArray()
                typeToken is LeafType && typeToken.clazz == IntArray::class -> elements.map { it as Int }.toIntArray()
                typeToken is LeafType && typeToken.clazz == Long::class -> elements.map { it as Long }.toLongArray()
                typeToken is LeafType && typeToken.clazz == Float::class -> elements.map { it as Float }.toFloatArray()
                typeToken is LeafType && typeToken.clazz == Double::class -> elements.map { it as Double }.toDoubleArray()
                typeToken is LeafType && typeToken.clazz == String::class -> elements.map { it as String }.toTypedArray()
                typeToken is ArrayType -> elements.map { it as Array<*> }.toTypedArray()
                typeToken is ListType -> elements.map { it as List<*> }.toTypedArray()
                typeToken is MapType -> elements.map { it as Map<*, *> }.toTypedArray()
                else -> elements.map { typeToken.clazz.cast(it)  }.toTypedArray()
            }
        }
    }

    class ListType(type: KType) : TypeToken(type) {

        val elementType: KType = type.arguments.first().type!!

        fun newInstance(elements: Collection<Any?>): List<Any?> = listOf(*elements.toTypedArray())
    }

    class MapType(type: KType) : TypeToken(type) {

        val keyType: KType = type.arguments.first().type!!
        val valueType: KType = type.arguments.last().type!!

        fun newInstance(entries: Collection<Pair<Any, Any?>>): Map<Any, Any?> = mapOf(*entries.toTypedArray())
    }

    class ObjectType(type: KType) : TypeToken(type) {

        val constructorArguments: List<KParameter> = clazz.primaryConstructor!!.parameters

        fun newInstance(args: Map<KParameter, Any?>): Any = clazz.primaryConstructor!!.callBy(args)
    }

    companion object {

        fun valueOf(type: KType): TypeToken {
            val clazz = type.classifier as KClass<*>
            return when {
                clazz == Char::class -> LeafType(type) { Randoms.char() }
                clazz == Boolean::class -> LeafType(type) { Randoms.boolean() }
                clazz == Byte::class -> LeafType(type) { Randoms.positiveByte() }
                clazz == Short::class -> LeafType(type) { Randoms.positiveShort() }
                clazz == Int::class -> LeafType(type) { Randoms.positiveInteger() }
                clazz == Long::class -> LeafType(type) { Randoms.positiveLong() }
                clazz == Float::class -> LeafType(type) { Randoms.positiveFloat() }
                clazz == Double::class -> LeafType(type) { Randoms.positiveDouble() }
                clazz == String::class -> LeafType(type) { Randoms.string() }
                clazz == ByteBuffer::class -> LeafType(type) { Randoms.byteBuffer() }
                clazz == ByteArray::class -> LeafType(type) { Randoms.byteArray() }
                clazz.java.isArray -> ArrayType(type)
                clazz.java.isEnum -> EnumType(type)
                clazz.isSubclassOf(Collection::class) || clazz.isSubclassOf(List::class) -> ListType(type)
                clazz.isSubclassOf(Map::class) -> MapType(type)
                else -> ObjectType(type)
            }
        }
    }
}
