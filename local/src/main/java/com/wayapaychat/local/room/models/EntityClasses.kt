package com.wayapaychat.local.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "waya_user_table", indices = [Index(value = ["id", "userId", "authId", "username"], unique = true)])
data class WayaGramUser(
    @PrimaryKey
    var id: String = "",
    @ColumnInfo
    var avatar: String? = "",
    @ColumnInfo
    var coverImage: String? = "",
    @ColumnInfo
    var username: String = "",
    @ColumnInfo
    var userId:String = "",
    @ColumnInfo
    var authId: String = "",
    @ColumnInfo
    val notPublic: Boolean = false,
    @ColumnInfo
    val token: String = "",
    @ColumnInfo
    val firstName:String = "",
    @ColumnInfo
    val surname:String = "",
    @ColumnInfo
    val middleName:String = "",
    @ColumnInfo
    val profileImage: String? = "",
    @ColumnInfo
    val city: String = "",
    @ColumnInfo
    val corporate: Boolean = false,
    @ColumnInfo
    val address: String = "",
    @ColumnInfo
    val email:String = "",
    @ColumnInfo
    val phoneNumber:String = "",
    @ColumnInfo
    val referenceCode: String = "",
    @ColumnInfo
    val dateOfBirth: String = "",
    @ColumnInfo
    val gender: String = "",
    @ColumnInfo
    val district: String = "",
    @ColumnInfo
    val postCount: Int = 0,
    @ColumnInfo
    val following: Int = 0,
    @ColumnInfo
    val followers: Int = 0,
    @ColumnInfo
    var displayName: String = "$firstName $surname",
    var description: String = "",
):Serializable