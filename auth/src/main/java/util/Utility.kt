
/**
 * Created by edetebenezer on 2019-07-25
 **/
internal class Utility {
    companion object {
        object Constants {
            const val ANIMATION_COMPLETED = 98
        }
    }
}

fun isEmailValid(emailAddress: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
}

fun String.isValidEmailAddress(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
