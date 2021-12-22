package uj.roomme.services.configuration

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uj.roomme.services.FlatService
import uj.roomme.services.UserService
import uj.roomme.services.configuration.ServiceConfiguration.SERVICE_LOCALHOST_EMULATOR_URL

//@Module
//@InstallIn(SingletonComponent::class)
class ServicesModule {

//    @Provides
//    @Singleton
    fun userService() = createService<UserService>()

//    @Provides
//    @Singleton
    fun orderService() = createService<FlatService>()

    private inline fun <reified T> createService() : T {
        return Retrofit.Builder()
            .baseUrl(SERVICE_LOCALHOST_EMULATOR_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(T::class.java)
    }
}