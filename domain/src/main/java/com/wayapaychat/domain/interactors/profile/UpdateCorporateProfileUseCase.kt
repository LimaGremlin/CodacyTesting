package com.wayapaychat.domain.interactors.profile

import com.wayapaychat.domain.interactors.UseCaseWithParams
import com.wayapaychat.domain.interactors.UseCaseWithParamsNoFlow
import com.wayapaychat.domain.models.BaseDomainModel
import com.wayapaychat.domain.repository.profile.IProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateCorporateProfileUseCase @Inject constructor(
    private val IProfileRepository: IProfileRepository
) : UseCaseWithParamsNoFlow<UpdateCorporateProfileUseCase.Parameter, BaseDomainModel<Nothing?>>() {

    data class Parameter(
        val address: String,
        val dateOfBirth: String,
        val district: String,
        val email: String,
        val firstName: String,
        val gender: String,
        val middleName: String,
        val phoneNumber: String,
        val surname: String,
        val businessType: String,
        val organisationName: String,
        val organisationType: String
    ) {
        companion object {
            @JvmStatic
            fun make(
                address: String,
                dateOfBirth: String,
                district: String,
                email: String,
                firstName: String,
                gender: String,
                middleName: String,
                phoneNumber: String,
                surname: String,
                businessType: String,
                organisationName: String,
                organisationType: String
            ): Parameter =
                Parameter(
                    address,
                    dateOfBirth,
                    district,
                    email,
                    firstName,
                    gender,
                    middleName,
                    phoneNumber,
                    surname,
                    businessType,
                    organisationName,
                    organisationType
                )
        }
    }

    override suspend fun buildUseCase(param: Parameter?): BaseDomainModel<Nothing?> =
        param?.let {
            IProfileRepository.updateCorporateProfile(
                hashMapOf(
                    "address" to it.address,
                    "dateOfBirth" to it.dateOfBirth,
                    "district" to it.district,
                    "email" to it.email,
                    "firstName" to it.firstName,
                    "gender" to it.gender,
                    "middleName" to it.middleName,
                    "phoneNumber" to it.phoneNumber,
                    "surname" to it.surname,
                    "organisationEmail" to it.email,
                    "businessType" to it.businessType,
                    "organisationName" to it.organisationName,
                    "organisationType" to it.organisationType,
                )
            )
        } ?: throw IllegalArgumentException("Parameters cannot be null")

}
