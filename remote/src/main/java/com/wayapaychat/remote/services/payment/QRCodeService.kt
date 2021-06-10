package com.wayapaychat.remote.services.payment

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QRCodeService {

    @GET("qrcode-service/qrcode/send/otp/to/assign/qrcode/{phone_number}")
    suspend fun sendOTPToAssignQRCode(@Path("phone_number")phoneNumber: String)

    @POST("qrcode-service/qrcode/assign/qrcode")
    suspend fun assignQRCode(@Body request: HashMap<String, Any>)

}