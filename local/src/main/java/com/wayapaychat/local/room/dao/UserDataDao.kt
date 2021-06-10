package com.wayapaychat.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wayapaychat.local.models.auth.UserDataLocalModel

@Dao
interface UserDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserDetails(userData: UserDataLocalModel)

    @Query("SELECT * FROM `WAYA USER DATA`")
    suspend fun getUserLoginData(): UserDataLocalModel
}
