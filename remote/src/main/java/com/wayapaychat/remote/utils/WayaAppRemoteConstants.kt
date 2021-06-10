package com.wayapaychat.remote.utils

object WayaAppRemoteConstants {

    const val OKHTTP_CACHE = "Waya Super App Cache"
    const val OKHTTP_CACHE_SIZE = 10 * 1000 * 1000

    const val BASE_RETROFIT = "Clean_Retrofit"
    const val PASSWORD_RETROFIT = "Password_Retrofit"
    const val CLEAN_OKHTTP = "Clean_Okhttp"

    const val PROFILE_RETROFIT = "Profile"
    const val PROFILE_OKHTTP = "Profile_Okhttp"
    const val QR_CODE_RETROFIT = "QR_Code_Retrofit"
    const val WALLET_RETROFIT = "Wallet_Retrofit"
    const val CARD_RETROFIT = "Card_Retrofit"

    const val CONNECT_EXCEPTION = "Could not connect to the server. Please check your internet connection and try again."
    const val SOCKET_TIME_OUT_EXCEPTION = "Request timed out while trying to connect. Please ensure you have a strong signal and try again."
    const val UNKNOWN_NETWORK_EXCEPTION = "An unexpected error has occurred. Please check your network connection and try again."
    const val UNKNOWN_HOST_EXCEPTION = "Couldn't connect to the server at the moment. Please try again in a few minutes."
    const val FORBIDDEN_EXCEPTION = "You're not authorised to connect at this moment. Please contact support."
    const val PROTOCOL_EXCEPTION = "An error occurred while connecting to the server. Please try again."
}
