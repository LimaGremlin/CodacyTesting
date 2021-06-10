package com.wayapaychat.ng.di.modules.local

import android.content.Context
import androidx.room.Room
import com.wayapaychat.local.room.WayaDatabase
import com.wayapaychat.local.room.dao.OnBoardingStatusDao
import com.wayapaychat.local.room.dao.UserDataDao
import com.wayapaychat.local.utils.WayaLocalModuleConstants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by edetebenezer on 2019-08-11
 **/

@Module
@InstallIn(SingletonComponent::class)
class WayaAppRoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        WayaDatabase::class.java, DB_NAME
    ).build()

    @Provides
    fun provideOnBoardingDao(database: WayaDatabase): OnBoardingStatusDao =
        database.getOnBoardingDao()

    @Provides
    fun provideSaveUserDataDao(database: WayaDatabase): UserDataDao =
        database.getUserDataDao()
}
