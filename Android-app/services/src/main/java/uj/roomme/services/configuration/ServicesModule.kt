package uj.roomme.services.configuration

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uj.roomme.services.FlatService
import uj.roomme.services.UserService
import uj.roomme.services.configuration.ServiceConfiguration.SERVICE_LOCALHOST_EMULATOR_URL
import java.lang.reflect.Type
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

//@Module
//@InstallIn(SingletonComponent::class)
class ServicesModule {

    val dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
//    @Provides
//    @Singleton
    fun userService() = createService<UserService>()

//    @Provides
//    @Singleton
    fun flatService() = createService<FlatService>()

    private inline fun <reified T> createService() : T {
        val gson = GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java,
                JsonDeserializer { json, typeOfT, context ->
                    OffsetDateTime.parse(json.asString, dateTimeFormatter)
                }).create()
        return Retrofit.Builder()
            .baseUrl(SERVICE_LOCALHOST_EMULATOR_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(T::class.java)
    }
}