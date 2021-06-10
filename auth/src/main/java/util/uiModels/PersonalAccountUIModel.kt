package util.uiModels

data class PersonalAccountUIModel(
    val corporate: Boolean,
    var email: String,
    val firstName: String,
    var password: String,
    var phoneNumber: String,
    val referenceCode: String,
    val surname: String
)

