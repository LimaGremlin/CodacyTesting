package com.wayapaychat.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wayapaychat.local.models.auth.OnBoardingDoneLocalModel

/**
 * Created by edetebenezer on 2019-09-17
 **/
@Dao
interface OnBoardingStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOnBoardingStatus(status: OnBoardingDoneLocalModel)

    @Query("SELECT * FROM `WAYA ONBOARDING COMPLETED`")
    fun getOnBoardingStatus(): List<OnBoardingDoneLocalModel>
}
