package com.wayapaychat.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wayapaychat.local.room.models.WayaGramUser

@Dao
interface WayaGramDao {
    //Insert WayaGramUer data in database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: WayaGramUser)

    //GET waya gram user from his authentication Id
    @Query("SELECT * FROM waya_user_table WHERE authId = :id")
    suspend fun getUserByAuthId(id:String):WayaGramUser?

    /**
     * Search for items in table with imputed name
     */
    @Query("SELECT * FROM waya_user_table WHERE authId = :id")
    fun getLiveUserProfile(id:String): LiveData<WayaGramUser?>
}