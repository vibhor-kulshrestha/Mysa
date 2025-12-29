package ai.mysmartassistant.mysa.di

import ai.mysmartassistant.mysa.data.onboard.OnboardRepositoryImpl
import ai.mysmartassistant.mysa.domain.onboard.OnboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingModule {

    @Binds
    abstract fun bindOnBoardingRepository(
        impl: OnboardRepositoryImpl
    ): OnboardRepository

}