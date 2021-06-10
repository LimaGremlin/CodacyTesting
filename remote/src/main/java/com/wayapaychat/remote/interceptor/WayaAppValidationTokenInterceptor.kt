package com.wayapaychat.remote.interceptor

import com.wayapaychat.repository.local.auth.IWayaPreferenceRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * This interceptor intercepts every request and appends the
 * validation token to the header.
 * This is a necessary requirement from the backend.
 */
class WayaAppValidationTokenInterceptor @Inject constructor(
    private val preferenceRepository: IWayaPreferenceRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest =
            preferenceRepository.getToken(preferenceRepository.getLoggedInUserType())?.let {
                val originalRequest = chain.request()
                originalRequest.newBuilder().addHeader(
                    "token", it
                ).build()
            } ?: chain.request()

        return chain.proceed(newRequest)
    }
}
