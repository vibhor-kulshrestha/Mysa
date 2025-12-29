package ai.mysmartassistant.mysa.di

import ai.mysmartassistant.mysa.data.device.DeviceIdProviderImpl
import ai.mysmartassistant.mysa.data.device.DeviceInfoProviderImpl
import ai.mysmartassistant.mysa.domain.device.DeviceIdProvider
import ai.mysmartassistant.mysa.domain.device.DeviceInfoProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceModule {

    @Binds
    abstract fun bindDeviceInfoProvider(
        impl: DeviceInfoProviderImpl
    ): DeviceInfoProvider

    @Binds
    abstract fun bindDeviceIdProvider(
        impl: DeviceIdProviderImpl
    ): DeviceIdProvider
}