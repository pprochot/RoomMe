package uj.roomme.services.configuration

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uj.roomme.domain.auth.ErrorCode
import uj.roomme.services.AuthService
import uj.roomme.services.BuildConfig
import uj.roomme.services.FlatService
import uj.roomme.services.UserService
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Provides
    @Singleton
    fun gsonConverterFactory(): GsonConverterFactory {
        val offsetDateTimeDeserializer = JsonDeserializer { json, _, _ ->
            OffsetDateTime.parse(json.asString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }
        val errorCodeDeserializer = JsonDeserializer { json, _, _ ->
            ErrorCode.fromCode(json.asInt)
        }
        val gson = GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, offsetDateTimeDeserializer)
            .registerTypeAdapter(ErrorCode::class.java, errorCodeDeserializer)
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun userService(gson: GsonConverterFactory) = createService<UserService>(gson)

    @Provides
    @Singleton
    fun flatService(gson: GsonConverterFactory) = createService<FlatService>(gson)

    @Provides
    @Singleton
    fun authService(gson: GsonConverterFactory) = createService<AuthService>(gson)

    private inline fun <reified T> createService(gsonConverterFactory: GsonConverterFactory): T {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_URL)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(T::class.java)
    }
}