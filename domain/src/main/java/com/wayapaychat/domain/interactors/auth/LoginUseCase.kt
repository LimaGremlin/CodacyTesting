package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.models.auth.LoginUser
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithParams<LoginUseCase.Parameter, BaseDomainModel<LoginUser>>() {

    data class Parameter(
        val admin: Boolean,
        val email: String,
        val password: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                admin: Boolean,
                email: String,
                password: String
            ): Parameter =
                Parameter(
                    admin,
                    email,
                    password,
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): Flow<BaseDomainModel<LoginUser>> =
        param?.let {
            IAuthRepository.login(
                hashMapOf(
                    "admin" to it.admin,
                    "email" to it.email,
                    "password" to it.password
                )
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")

}
