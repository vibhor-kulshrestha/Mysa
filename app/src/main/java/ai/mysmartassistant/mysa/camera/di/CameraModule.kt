package ai.mysmartassistant.mysa.camera.di

import ai.mysmartassistant.mysa.camera.data.CameraRepositoryImpl
import ai.mysmartassistant.mysa.camera.domain.repository.CameraRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CameraModule {
    @Binds
    abstract fun provideCameraRepository(
        impl: CameraRepositoryImpl
    ): CameraRepository
}