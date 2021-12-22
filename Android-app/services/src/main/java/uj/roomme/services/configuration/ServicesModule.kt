package uj.roomme.services.configuration

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uj.roomme.services.FlatService
import uj.roomme.services.UserService
import uj.roomme.services.configuration.ServiceConfiguration.SERVICE_LOCALHOST_EMULATOR_URL
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Provides
    @Singleton
    fun gsonConverterFactory(): GsonConverterFactory {
        val offsetDateTimeDeserializer = JsonDeserializer { json, typeOfT, context ->
            OffsetDateTime.parse(json.asString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }
        val gson = GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, offsetDateTimeDeserializer)
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun userService(gson: GsonConverterFactory) = createService<UserService>(gson)

    @Provides
    @Singleton
    fun flatService(gson: GsonConverterFactory) = createService<FlatService>(gson)

    private inline fun <reified T> createService(gsonConverterFactory: GsonConverterFactory): T {
        return Retrofit.Builder()
            .baseUrl(SERVICE_LOCALHOST_EMULATOR_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(T::class.java)
    }
}