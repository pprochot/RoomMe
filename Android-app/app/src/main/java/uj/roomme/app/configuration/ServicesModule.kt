package uj.roomme.app.configuration

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.auth.ErrorCode
import uj.roomme.services.service.AuthService
import uj.roomme.services.BuildConfig
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.UserService
import uj.roomme.services.factory.RoomMeCallAdapterFactory
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServicesModule {

    @Provides
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
    fun authService(gson: GsonConverterFactory) = createService<AuthService>(gson)

    @Provides
    fun userService(gson: GsonConverterFactory) = createService<UserService>(gson)

    @Provides
    fun flatService(gson: GsonConverterFactory) = createService<FlatService>(gson)

    private inline fun <reified T> createService(gsonConverterFactory: GsonConverterFactory): T {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(RoomMeCallAdapterFactory.create())
            .build()
            .create(T::class.java)
    }
}