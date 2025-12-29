package ai.mysmartassistant.mysa.di

import ai.mysmartassistant.mysa.data.location.CountryDialingCodeResolverImpl
import ai.mysmartassistant.mysa.domain.location.CountryDialingCodeResolver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    abstract fun bindDialingCodeResolver(
        impl: CountryDialingCodeResolverImpl
    ): CountryDialingCodeResolver
}