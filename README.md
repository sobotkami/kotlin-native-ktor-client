# kotlin-native-ktor-client
Minimal example of Kotlin/Native with KTor client. 
It demonstrates how to use the KTor client, call a REST endpoint and use own serializer to parse a special form of the date.

Tested on Fedora 32.

```kotlin
@Serializable
data class DateValue(val day: Int, val month: Int)

@Serializable
data class NameDayDTO(var name: String, @Serializable(with = DateValueSerializer::class) var date: DateValue)

class NameDayApi(private val serverUrl: String = "https://svatky.adresa.info/json") {
    private val client: HttpClient = HttpClient {
        install(JsonFeature)
    }

    fun getNameDayList(): List<NameDayDTO> = runBlocking {
        client.get("$serverUrl/")
    }
}

fun main() {
    val api = NameDayApi()

    api.getNameDayList().forEach {
        println("${it.date.day}. ${it.date.month}. ${it.name}")
    }
}
```
