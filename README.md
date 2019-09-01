## KRandom
KRandom is a random object generator for Kotlin. Random object generation is a handy way to create fixtures or stubs for automated tests. KRandom brings an idiomatic way to generate random complex objects with little effort.

### Getting started

#### Generating random primitives

```kotlin
val randomChar = krandom<Char>()
val randomBoolean = krandom<Boolean>()
val randomByte = krandom<Byte>()
val randomShort = krandom<Short>()
val randomInt = krandom<Int>()
val randomLong = krandom<Long>()
val randomFloat = krandom<Float>()
val randomDouble = krandom<Double>()
val randomString = krandom<String>()
```

#### Generating random arrays and collections

```kotlin
val randomStringList = krandom<List<String>>()
val randomStringIntMap = krandom<Map<String, Int>>()
val randomStringArray = krandom<Array<String>>()
```

#### Generating random objects
Given the following domain objects:

```kotlin
data class User(val name: String, address: Address)
data class Address(val address: String, val postcode: String, val city: String, val state: String)
```

Let's generate random stubs for tests:

```kotlin
// generate object instances individually
val randomAddress = krandom<Address>()
val randomUser = User(krandom<String>(), randomAddress)
```

Or even:

```kotlin
// generates the entire object graph automatically
val randomUser = krandom<User>()
```

#### Customizing generation
KRandom also lets you control some parameters while generating random instances for certain types:

```kotlin
// set size of auto-generated collections to 10 <-> 50 elements
val config = KRandomConfig(collectionSizeBounds = 10 to 50)

val randomStringList = krandom<List<String>>(config)

check(randomStringList.size in 10..50)

val randomStringIntMap = krandom<Map<String, Int>>(config)

check(randomStringIntMap.size in 10..50)
```

Or while generating complex objects:

```kotlin
// set size of auto-generated strings to 5 <-> 20 characters
// only User#name string property
val config = KRandomConfig(stringSizesBounds = 5 to 20)

val randomAddress = krandom<Address>()
val randomUser = User(krandom<String>(config), randomAddress)

check(randomUser.name.length in 5..20)
``` 

Or even:

```kotlin
// set size of auto-generated strings to 5 <-> 20 characters
// that includes User#name as well as all string properties in Address class. 
val config = KRandomConfig(stringSizesBounds = 5 to 20)

val randomUser = krandom<User>(config)

check(randomUser.name.length in 5..20)
check(randomUser.address.city.length in 5..20)
// ...
```

All configurable parameters are declared in `KRandomConfig` class.

#### Overriding properties
If your complex objects are Kotlin's `data classes`, you can override properties of objects created by KRandom as following:

```kotlin
val newName = "John Doe"
val randomUser = krandom<User>().copy(name = newName)

check(randomUser.name == newName)
```

Or even:

```kotlin
val newState = "CA"
val randomAddress = krandom<Address>().copy(state = newState)
val randomUser = User(krandom<String>(), randomAddress)

check(randomAddress.state == newState)
check(randomUser.address.state == newState)
```

Or even:

```kotlin
val randomUser = krandom<User>().copy(address = krandom<Address>().copy(state = "CA"))

check(randomUser.address.state == newState)
```

### Supported types
- Char
- Boolean
- Byte
- Short
- Int
- Long
- Float
- Double
- String
- ByteBuffer
- ByteArray
- Enum
- Array
- List
- Map
- Complex objects composed of types listed above (see limitations section)

### Complex object generation limitations
- Complex object that are not `data class` are required to have a primary constructor.
- Complex objects are required to be composed by the types declared above.
- There is no support for nested arrays.