package ai.mysmartassistant.mysa.di

import ai.mysmartassistant.mysa.data.auth.TruecallerCapabilityImpl
import ai.mysmartassistant.mysa.domain.location.CountryProvider
import ai.mysmartassistant.mysa.data.location.CountryProviderImpl
import ai.mysmartassistant.mysa.domain.auth.AuthRepository
import ai.mysmartassistant.mysa.data.auth.AuthRepositoryImpl
import ai.mysmartassistant.mysa.domain.auth.TruecallerCapability
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindCountryProvider(
        impl: CountryProviderImpl
    ): CountryProvider

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ) : AuthRepository

    @Binds
    abstract fun bindTruecallerCapability(
        impl: TruecallerCapabilityImpl
    ): TruecallerCapability
}