package com.wayapaychat.domain.interactors.profile

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val IProfileRepository: IProfileRepository
) : UseCaseWithParams<UpdateUserProfileUseCase.Parameter, BaseDomainModel<Boolean>>() {

    data class Parameter(
        val userId: Int,
        val address: String,
        val dateOfBirth: String,
        val district: String,
        val email: String,
        val firstName: String,
        val gender: String,
        val middleName: String,
        val phoneNumber: String,
        val surname: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                userId: Int,
                address: String,
                dateOfBirth: String,
                district: String,
                email: String,
                firstName: String,
                gender: String,
                middleName: String,
                phoneNumber: String,
                surname: String
            ): Parameter =
                Parameter(
                    userId,
                    address,
                    dateOfBirth,
                    district,
                    email,
                    firstName,
                    gender,
                    middleName,
                    phoneNumber,
                    surname
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): Flow<BaseDomainModel<Boolean>> =
        param?.let {
            IProfileRepository.updateUserInformation(
                it.userId.toString(),
                hashMapOf(
                    "address" to it.address,
                    "dateOfBirth" to it.dateOfBirth,
                    "district" to it.district,
                    "email" to it.email,
                    "firstName" to it.firstName,
                    "gender" to it.gender,
                    "middleName" to it.middleName,
                    "phoneNumber" to it.phoneNumber,
                    "surname" to it.surname
                )
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")
}
