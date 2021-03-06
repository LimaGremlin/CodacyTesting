package com.wayapaychat.remote.interceptor

import com.wayapaychat.repository.local.auth.IWayaPreferenceRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * An interceptor that appends the authorization token to each request.
 * @param preferenceRepository is the Repository module implementation
 * of the Waya shared preference
 */
class WayaAppAuthorizationTokenInterceptor @Inject constructor (
    private val preferenceRepository: IWayaPreferenceRepository
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val loggedInUserType = preferenceRepository.getLoggedInUserType()
        val newRequest = preferenceRepository.getToken(loggedInUserType)?.let {
            val request = chain.request()
            request.newBuilder().addHeader(
                "authorization", it
            ).build()
        } ?: chain.request()


        return chain.proceed(newRequest)
    }
}


















