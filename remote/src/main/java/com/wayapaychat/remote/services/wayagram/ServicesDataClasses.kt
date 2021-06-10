package com.wayapaychat.remote.services.wayagram

import java.io.Serializable

data class PagesCategories(
    val id: String = "",
    val name: String = ""
):Serializable

data class PagesAsync(
    val userId: String = "",
    val categoryId: String = "",
    val username: String? = null, //name of group
    val title: String = "",
    val description: String? = null,
    val websiteUrl: String? = null,
    val isPublic: Boolean = true,
): Serializable

data class GroupAsync(
    val userId: String = "",
    val name: String? = null,
    val description: String? = null,
    val isPublic: Boolean = true,
    val mute: Boolean = false,
): Serializable