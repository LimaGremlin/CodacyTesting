package com.wayapaychat.local.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromDoubleList(value: List<String>?): String? {
            if (value == null) {
                return null
            }
            return Gson().toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toDoubleList(value: String?): List<String>? {
            if (value == null) {
                return null
            }
            val gson = Gson()
            val type = object : TypeToken<List<String>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}
