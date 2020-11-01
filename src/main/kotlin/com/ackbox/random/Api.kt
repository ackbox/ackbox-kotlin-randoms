package com.ackbox.random

import com.ackbox.random.common.getKType
import com.ackbox.random.core.KRandom
import kotlin.reflect.KClass

data class KRandomConfig(
    var typeFactories: Map<KClass<*>, TypeFactory<*>> = mapOf(),
    var collectionSizeBounds: Pair<Int, Int> = 1 to 15,
    var stringSizesBounds: Pair<Int, Int> = 10 to 50
)

typealias TypeFactory<T> = () -> T

inline fun <reified T : Any> krandom(config: KRandomConfig = KRandomConfig()): T {
    return KRandom<T>(getKType<T>(), config).next()
}


