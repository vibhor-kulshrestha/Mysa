package ai.mysmartassistant.mysa.di

import ai.mysmartassistant.mysa.domain.session.SessionRepository
import ai.mysmartassistant.mysa.data.session.SessionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {
    @Binds
    abstract fun bindSessionRepository(
        impl: SessionRepositoryImpl
    ): SessionRepository
}