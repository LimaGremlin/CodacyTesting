package com.wayapaychat.local.models.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by edetebenezer on 2019-09-17
 **/
@Entity(
    tableName = "WAYA ONBOARDING COMPLETED"
)
class OnBoardingDoneLocalModel(
    @PrimaryKey var isCompleted : Boolean
)
