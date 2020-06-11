import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.*

@Serializer(forClass = DateValue::class)
object DateValueSerializer : KSerializer<DateValue> {
    private val regex = Regex("^(\\d{2})(\\d{2})$")

    override val descriptor: SerialDescriptor
        get() = DateValue.serializer().descriptor

    override fun serialize(encoder: Encoder, value: DateValue) {
    }

    override fun deserialize(decoder: Decoder): DateValue {
        val value = decoder.decodeString()

        val match = regex.find(value)
                ?: throw RuntimeException("Invalid date: $value")

        val day = match.groups[1]?.value?.toInt()
                ?: throw RuntimeException("Invalid day: $value")

        val month = match.groups[2]?.value?.toInt()
                ?: throw RuntimeException("Invalid month: $value")

        return DateValue(day, month)
    }
}

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
