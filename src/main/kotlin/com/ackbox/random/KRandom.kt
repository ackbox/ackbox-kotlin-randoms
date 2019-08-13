package com.ackbox.random

import com.ackbox.random.TypeToken.ArrayType
import com.ackbox.random.TypeToken.BooleanType
import com.ackbox.random.TypeToken.ByteArrayType
import com.ackbox.random.TypeToken.ByteBufferType
import com.ackbox.random.TypeToken.ByteType
import com.ackbox.random.TypeToken.CharType
import com.ackbox.random.TypeToken.DoubleType
import com.ackbox.random.TypeToken.EnumType
import com.ackbox.random.TypeToken.FloatType
import com.ackbox.random.TypeToken.IntType
import com.ackbox.random.TypeToken.ListType
import com.ackbox.random.TypeToken.LongType
import com.ackbox.random.TypeToken.MapType
import com.ackbox.random.TypeToken.ObjectType
import com.ackbox.random.TypeToken.ShortType
import com.ackbox.random.TypeToken.StringType
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter

@Suppress("UNCHECKED_CAST")
class KRandom<T : Any>(private val type: KType, private val config: KRandomConfig) {

    fun next(): T {
        val typeToken = getTypeToken(type)
        LOG.trace("Retrieved type token [$typeToken] for [$type]")
        return if (typeToken is ObjectType) generateInstance(type) else generateValue(typeToken) as T
    }

    private fun <T> generateInstance(type: KType): T {
        val token = ObjectType(type)
        LOG.trace("Generating random instance for type [$token]")
        val argsSpec = token.constructorArguments
        LOG.trace("Found constructor with arguments [$argsSpec]")
        val args = argsSpec.associateWith { generateValue(getTypeToken(it.type)) }
        LOG.trace("Creating instance with [$args]")
        return token.newInstance(args) as T
    }

    private fun generateValue(typeToken: TypeToken): Any {
        LOG.trace("Generating value for type [$typeToken]")
        return when (typeToken) {
            is CharType -> Randoms.char()
            is BooleanType -> Randoms.boolean()
            is ByteType -> Randoms.positiveByte()
            is ShortType -> Randoms.positiveShort()
            is IntType -> Randoms.positiveInteger()
            is LongType -> Randoms.positiveLong()
            is FloatType -> Randoms.positiveFloat()
            is DoubleType -> Randoms.positiveDouble()
            is StringType -> Randoms.string(Randoms.positiveIntegerInRange(config.stringSizesBounds.first, config.stringSizesBounds.second))
            is ByteBufferType -> Randoms.byteBuffer()
            is ByteArrayType -> Randoms.byteArray()
            is EnumType -> Randoms.enumValue(typeToken.clazz)
            is ArrayType -> generateArray(typeToken)
            is ListType -> generateList(typeToken)
            is MapType -> generateMap(typeToken)
            is ObjectType -> generateInstance(typeToken.type)
        }
    }

    private fun generateList(token: ListType): List<Any?> {
        val size = computeSize()
        LOG.trace("Generating list values for class [${token.elementType}]")
        return token.newInstance((0 until size).map { generateInstanceForParameter(token.elementType, token.type) })
    }

    private fun generateMap(token: MapType): Map<Any, Any?> {
        val size = computeSize()
        val keyType = token.keyType
        val valueType = token.valueType
        LOG.trace("Generating map values for class [$keyType::$valueType]")
        val entries = (0 until size).map {
            generateInstanceForParameter(keyType, token.type) to generateInstanceForParameter(valueType, token.type)
        }
        return token.newInstance(entries)
    }

    private fun generateArray(token: ArrayType): Any {
        val size = computeSize()
        val elementType = token.elementType
        LOG.trace("Generating array values for class [$elementType]")
        return token.newInstance((0 until size).map { generateInstanceForParameter(elementType, token.type) })
    }

    private fun generateInstanceForParameter(paramType: KType, type: KType): Any {
        return when (val classifier = paramType.classifier) {
            is KClass<*> -> generateValue(getTypeToken(paramType))
            is KTypeParameter -> {
                val clazz = type.classifier as KClass<*>
                val typeParameterName = classifier.name
                val typeParameterId = clazz.typeParameters.indexOfFirst { it.name == typeParameterName }
                val parameterType = type.arguments[typeParameterId].type ?: getKType<Any>()
                LOG.trace("Generating value for type parameter [$parameterType]")
                generateInstance(parameterType)
            }
            else -> throw Error("Type of the classifier [$classifier] is not supported")
        }
    }

    private fun computeSize() = Randoms.positiveIntegerInRange(
        config.collectionSizeBounds.first,
        config.collectionSizeBounds.second
    )

    private fun getTypeToken(type: KType): TypeToken = TypeToken.valueOf(type)

    companion object {

        private val LOG = logger()
    }
}

inline fun <reified T : Any> krandom(config: KRandomConfig = KRandomConfig()): T {
    return KRandom<T>(getKType<T>(), config).next()
}

data class KRandomConfig(
    var collectionSizeBounds: Pair<Int, Int> = 1 to 15,
    var stringSizesBounds: Pair<Int, Int> = 10 to 50
)
