package com.wayapaychat.remote.services.models

import java.io.Serializable

data class EVentAsync(
        var eventPoster: String = "",
        var organizerId: String = "",
        var location: String = "",
        var eventName: String = "",
        var details: String = "",
        var eventStart: String = "",
        var eventEnd: String = ""
): Serializable

data class PostAsysnc(
        var profile_id:String = "",
        var parent_id:String? = null,
        var description:String? = null,
        var group_id:String? = null,
        var page_id:String? = null,
        var type:String? = null,
        var hashtags:List<String> = emptyList(),
        var mentions:List<String> = emptyList(),
        var isPoll:Boolean = false,
        var isPaid:Boolean = false,
        var forceTerms:Boolean = false,
        var amount:Int = 0,
        var voteLimit:Int = 1,
        var expiresIn:String? = null,
        var terms:String? = null,
        var options:List<String> = emptyList<String>(),
):Serializable