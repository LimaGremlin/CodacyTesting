package com.wayapaychat.ng.auth.presentation.utils

import android.content.Context
import com.google.gson.JsonObject
import com.wayapaychat.core.utils.removeQuotes
import com.wayapaychat.ng.auth.R
import com.wayapaychat.ng.auth.presentation.model.Pages
import com.wayapaychat.ng.auth.presentation.model.UserSignUpDetails
import okhttp3.internal.and
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.nio.charset.Charset
import java.util.*
import kotlin.jvm.Throws

fun JsonObject.toUserSignUpData():UserSignUpDetails{
    val data = this
    val user = data.get("user").asJsonObject
    return UserSignUpDetails(
        userId = user.get("id").asInt,
        isCorporate = data.get("corporate").asBoolean,
        pinCreated = data.get("pinCreated").asBoolean,
        email = user.get("email").toString().removeQuotes(),
        phoneNumber = user.get("phoneNumber").toString().removeQuotes(),
        firstName = user.get("firstName").toString().removeQuotes(),
        lastName = user.get("lastName").toString().removeQuotes(),
        token = data.get("token").toString().removeQuotes()

    )
}

fun getListPages(context: Context): List<Pages>{
    return listOf(
        Pages(
            R.drawable.ic_connect,
            "Wayapay",
            context.getString(R.string.free_internet_banking_bills_payment)
        ),
        Pages(
            R.drawable.ic_discover,
            "Wayachat",
            context.getString(R.string.chat_and_call_clients_friends_and_family)
        ),
        Pages(
            R.drawable.ic_transact,
            "Wayagram",
            context.getString(R.string.discover_beautiful_places_people_conversarions_vendors_and_clients)
        )
    )
}

fun confirmEmailFormat(email: String):Boolean{
    val regex = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])".toRegex()
    return regex.matches(email)
}

fun confirmPasswordFormat(email: String):Boolean{
    val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{8,}".toRegex()
    return regex.matches(email)
}

fun confirmTextMatches(text1: String, text2: String):Boolean{
    if(text1 == text2)
        return true
    return false
}

fun String.removePlus():String{
    var value = this
    if(value.startsWith("+"))value = value.drop(1)
    return value
}


/**
 * Convert byte array to hex string
 * @param bytes toConvert
 * @return hexValue
 */
fun bytesToHex(bytes: ByteArray): String? {
    val sbuf = StringBuilder()
    for (idx in bytes.indices) {
        val intVal: Int = bytes[idx] and 0xff
        if (intVal < 0x10) sbuf.append("0")
        sbuf.append(Integer.toHexString(intVal).toUpperCase())
    }
    return sbuf.toString()
}

/**
 * Get utf8 byte array.
 * @param str which to be converted
 * @return  array of NULL if error was found
 */
fun getUTF8Bytes(str: String): ByteArray? {
    return try {
        str.toByteArray(charset("UTF-8"))
    } catch (ex: Exception) {
        null
    }
}

/**
 * Load UTF8withBOM or any ansi text file.
 * @param filename which to be converted to string
 * @return String value of File
 * @throws java.io.IOException if error occurs
 */
@Throws(IOException::class)
fun loadFileAsString(filename: String?): String? {
    val BUFLEN = 1024
    val `is` = BufferedInputStream(FileInputStream(filename), BUFLEN)
    return try {
        val baos = ByteArrayOutputStream(BUFLEN)
        val bytes = ByteArray(BUFLEN)
        var isUTF8 = false
        var read: Int
        var count = 0
        while (`is`.read(bytes).also { read = it } != -1) {
            if (count == 0 && bytes[0] == 0xEF.toByte() && bytes[1] == 0xBB.toByte() && bytes[2] == 0xBF.toByte()) {
                isUTF8 = true
                baos.write(bytes, 3, read - 3) // drop UTF8 bom marker
            } else {
                baos.write(bytes, 0, read)
            }
            count += read
        }
        if (isUTF8) String(baos.toByteArray(), Charset.forName("UTF-8")) else String(baos.toByteArray())
    } finally {
        try {
            `is`.close()
        } catch (ignored: Exception) {
        }
    }
}

/**
 * Returns MAC address of the given interface name.
 * @param interfaceName eth0, wlan0 or NULL=use first interface
 * @return  mac address or empty string
 */
fun getMACAddress(interfaceName: String?): String? {
    try {
        val interfaces: List<NetworkInterface> =
            Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            if (interfaceName != null) {
                if (intf.name.toLowerCase() != interfaceName.toLowerCase()) continue
            }
            val mac: ByteArray = intf.getHardwareAddress() ?: return ""
            val buf = StringBuilder()
            for (aMac in mac) buf.append(String.format("%02X:", aMac))
            if (buf.length > 0) buf.deleteCharAt(buf.length - 1)
            return buf.toString()
        }
    } catch (ignored: Exception) {
    } // for now eat exceptions
    return ""
    /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
}

/**
 * Get IP address from first non-localhost interface
 * @param useIPv4   true=return ipv4, false=return ipv6
 * @return  address or empty string
 */
fun getIPAddress(useIPv4: Boolean): String? {
    try {
        val interfaces: List<NetworkInterface> =
            Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            val addrs: List<InetAddress> = Collections.list(intf.getInetAddresses())
            for (addr in addrs) {
                if (!addr.isLoopbackAddress()) {
                    val sAddr: String = addr.getHostAddress()
                    //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                    val isIPv4 = sAddr.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) return sAddr
                    } else {
                        if (!isIPv4) {
                            val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                            return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(0, delim)
                                .toUpperCase()
                        }
                    }
                }
            }
        }
    } catch (ignored: Exception) {
    } // for now eat exceptions
    return ""
}

