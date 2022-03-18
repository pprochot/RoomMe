package uj.roomme.app.configuration

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uj.roomme.app.validators.NewProductValidator
import uj.roomme.app.validators.SignInValidator
import uj.roomme.app.validators.SignUpValidator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ValidatorsModule {

    @Provides
    fun signInValidator(): SignInValidator = SignInValidator()

    @Provides
    fun signUpValidator(): SignUpValidator = SignUpValidator()

    @Provides
    fun newProductValidator(): NewProductValidator = newProductValidator()
}