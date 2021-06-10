package com.litApp.litMechMerchantApp.commons

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import com.google.android.gms.maps.model.LatLng
import com.wayapaychat.core.utils.showShortToast
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class GetAddressesAsyncTask(
    private val context: Context,
    private val addressFetched: (address: Address?) -> Unit
) : AsyncTask<Pair<Double, Double>, Void?, List<Address>>() {

    override fun doInBackground(vararg locations: Pair<Double, Double>): List<Address> {
        val location = locations[0]
        var addresses: List<Address> = ArrayList()
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            addresses = geocoder.getFromLocation(location.first, location.second, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return addresses
    }

    override fun onPostExecute(addresses: List<Address>) {
        super.onPostExecute(addresses)

        if (addresses.isEmpty()) {
            context.showShortToast("Unable to get your address at this time Please try again later")
            addressFetched(null)
        } else {
            addressFetched(addresses[0])
        }

    }
}
