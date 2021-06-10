package com.wayapaychat.local.impl.auth

import com.wayapaychat.local.mappers.toUserDataLocalModel
import com.wayapaychat.local.mappers.toUserEntity
import com.wayapaychat.local.models.auth.OnBoardingDoneLocalModel
import com.wayapaychat.local.preference.IWayaPreference
import com.wayapaychat.local.room.dao.OnBoardingStatusDao
import com.wayapaychat.local.room.dao.UserDataDao
import com.wayapaychat.repository.local.auth.IAuthenticationLocal
import com.wayapaychat.repository.models.auth.LoginDataEntity
import com.wayapaychat.repository.models.auth.UserEntity
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WayaAuthenticationLocalImpl @Inject constructor(
    private val onBoardingStatusDao: OnBoardingStatusDao,
    private val userDataDao: UserDataDao,
    private val preference: IWayaPreference,
) : IAuthenticationLocal {

    override fun isUserFullyRegistered(): Observable<Boolean> =
        Observable.just(preference.isUserFullyRegistered())

    override suspend fun saveOnBoardingStatus(status: Boolean): Flow<String> {
        withContext(Dispatchers.IO) {
            onBoardingStatusDao.saveOnBoardingStatus(OnBoardingDoneLocalModel(status))
        }

        return flow {
            emit("Saved")
        }
    }

    override fun hasUserBeenFullyOnBoarded(): Flow<Boolean> =
        flow {
            if (onBoardingStatusDao.getOnBoardingStatus().isNotEmpty())
                emit(onBoardingStatusDao.getOnBoardingStatus()[0].isCompleted)
            else
                emit(false)
        }.flowOn(Dispatchers.IO)

    override suspend fun saveUserDetails(userData: LoginDataEntity): Boolean {
        withContext(Dispatchers.IO) {
            userDataDao.saveUserDetails(
                userData.toUserDataLocalModel()
            )
        }

        return true
    }

    override suspend fun getSavedUserData(): LoginDataEntity =
        userDataDao.getUserLoginData().toUserEntity()

}
