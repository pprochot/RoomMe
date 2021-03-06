package uj.roomme.app.configuration

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uj.roomme.services.BuildConfig
import uj.roomme.services.factory.RoomMeCallAdapterFactory
import uj.roomme.services.service.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Provides
    fun gsonConverterFactory(): GsonConverterFactory {
        val offsetDateTimeDeserializer = JsonDeserializer { json, _, _ ->
            try {
                LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    .atOffset(ZoneOffset.UTC)
            } catch (ex: DateTimeParseException) {
                LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    .atOffset(ZoneOffset.UTC)
            }
        }
        val gson = GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, offsetDateTimeDeserializer)
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun authService(gson: GsonConverterFactory) = createService<AuthService>(gson)

    @Provides
    fun userService(gson: GsonConverterFactory) = createService<UserService>(gson)

    @Provides
    fun flatService(gson: GsonConverterFactory) = createService<FlatService>(gson)

    @Provides
    fun houseworkService(gson: GsonConverterFactory) = createService<HouseworkService>(gson)

    @Provides
    fun scheduleService(gson: GsonConverterFactory) = createService<ScheduleService>(gson)

    @Provides
    fun shoppingListService(gson: GsonConverterFactory) = createService<ShoppingListService>(gson)

    @Provides
    fun statisticsService(gson: GsonConverterFactory) = createService<StatisticsService>(gson)

    private inline fun <reified T> createService(gsonConverterFactory: GsonConverterFactory): T {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RoomMeCallAdapterFactory.create())
            .build()
            .create(T::class.java)
    }
}