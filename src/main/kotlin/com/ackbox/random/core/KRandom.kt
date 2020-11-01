package com.ackbox.random.core

import com.ackbox.random.KRandomConfig
import com.ackbox.random.common.getKType
import com.ackbox.random.common.logger
import com.ackbox.random.core.TypeToken.*
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

    private fun generateValue(typeToken: TypeToken): Any? {
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
            is StringType -> Randoms.string(computeSize())
            is ByteBufferType -> Randoms.byteBuffer()
            is ByteArrayType -> Randoms.byteArray()
            is EnumType -> Randoms.enumValue(typeToken.clazz)
            is ArrayType -> generateArray(typeToken)
            is ListType -> generateList(typeToken)
            is MapType -> generateMap(typeToken)
            is ObjectType -> generateInstance(typeToken.type)
            is FactoryGeneratedType -> typeToken.newInstance()
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
        }.filterNot { it.first == null }
        return token.newInstance(entries as Collection<Pair<Any, Any?>>)
    }

    private fun generateArray(token: ArrayType): Any {
        val size = computeSize()
        val elementType = token.elementType
        LOG.trace("Generating array values for class [$elementType]")
        return token.newInstance((0 until size).map { generateInstanceForParameter(elementType, token.type) })
    }

    private fun generateInstanceForParameter(paramType: KType, type: KType): Any? {
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

    private fun computeSize(): Int = Randoms.positiveIntegerInRange(
        config.collectionSizeBounds.first,
        config.collectionSizeBounds.second
    )

    private fun getTypeToken(type: KType): TypeToken {
        val factory = config.typeFactories[type.classifier as KClass<*>]
        return factory?.let { FactoryGeneratedType(type, it) } ?: TypeToken.valueOf(type)
    }

    companion object {

        private val LOG = logger()
    }
}
