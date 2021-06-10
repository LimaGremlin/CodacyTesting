package com.wayapaychat.domain.interactors.auth

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val IAuthRepository: AuthRepository
) : UseCaseWithParams<RegisterUseCase.Parameter, BaseDomainModel<String>>() {

    data class Parameter(
        val corporate: Boolean,
        val email: String,
        val firstName: String,
        val password: String,
        val phoneNumber: String,
        val referenceCode: String,
        val surname: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                corporate: Boolean,
                email: String,
                firstName: String,
                password: String,
                phoneNumber: String,
                referenceCode: String,
                surname: String
            ): Parameter =
                Parameter(
                    corporate,
                    email,
                    firstName,
                    password,
                    phoneNumber,
                    referenceCode,
                    surname,
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): Flow<BaseDomainModel<String>> =
        param?.let {
            IAuthRepository.registerUser(
                hashMapOf(
                    "corporate" to it.corporate,
                    "email" to it.email,
                    "firstName" to it.firstName,
                    "password" to it.password,
                    "phoneNumber" to it.phoneNumber,
                    "referenceCode" to it.referenceCode,
                    "surname" to it.surname
                )
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")

}
