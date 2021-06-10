package com.wayapaychat.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wayapaychat.local.BuildConfig
import com.wayapaychat.local.models.auth.OnBoardingDoneLocalModel
import com.wayapaychat.local.models.auth.UserDataLocalModel
import com.wayapaychat.local.room.dao.OnBoardingStatusDao
import com.wayapaychat.local.room.dao.UserDataDao
import com.wayapaychat.local.room.dao.WayaGramDao
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.local.utils.converters.BooleanConverter
import com.wayapaychat.local.utils.converters.BooleanListConverter
import com.wayapaychat.local.utils.converters.DoubleListConverter
import com.wayapaychat.local.utils.converters.StringListConverter

/**
 * Created by ayokunlepaul on 2019-07-17
 */
@Database(
    entities = [
        OnBoardingDoneLocalModel::class,
        UserDataLocalModel::class, WayaGramUser::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DoubleListConverter::class,
    BooleanListConverter::class,
    BooleanConverter::class,
    StringListConverter::class
)
abstract class WayaDatabase : RoomDatabase() {
    abstract fun getOnBoardingDao(): OnBoardingStatusDao
    abstract fun getUserDataDao(): UserDataDao
    abstract fun getWayaGramDao(): WayaGramDao

    companion object {
        @Volatile
        private var instance: WayaDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            WayaDatabase::class.java, "WayaApp.db"
        ).build()
    }
}
