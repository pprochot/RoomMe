package uj.roomme.app.configuration

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uj.roomme.app.validators.SignInValidator
import uj.roomme.app.validators.SignUpValidator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomMeModule {

    @Provides
    fun signInValidator(): SignInValidator {
        return SignInValidator()
    }

    @Provides
    fun signUpValidator(): SignUpValidator {
        return SignUpValidator()
    }
}