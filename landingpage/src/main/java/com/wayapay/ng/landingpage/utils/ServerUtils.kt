package com.wayapay.ng.landingpage.utils

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.wayapay.ng.landingpage.models.*
import com.wayapaychat.core.newtwork.getTempWallet
import com.wayapaychat.core.utils.removeQuotes
import com.wayapaychat.core.wayagram.Poll
import com.wayapaychat.core.wayagram.Vote
import com.wayapaychat.local.room.models.WayaGramUser
import com.wayapaychat.remote.services.models.EVentAsync
import com.wayapaychat.remote.services.models.PostAsysnc
import com.wayapaychat.remote.services.wayagram.GroupAsync
import com.wayapaychat.remote.services.wayagram.PagesAsync
import okhttp3.RequestBody
import java.util.*
import kotlin.collections.HashMap

fun JsonObject.getWayaPage():WayaPages{
    return WayaPages(
        pageId = this.get("id").toString().removeQuotes(),
        ownerId = this.get("userId").toString().removeQuotes(),
        categoryId = this.get("categoryId").toString().removeQuotes(),
        username = this.get("username").toString().removeQuotes(),
        pageName = this.get("title").toString().removeQuotes(),
        description = this.get("description").toString().removeQuotes(),
        websiteUrl = this.get("websiteUrl").toString().removeQuotes(),
        imageUrl = this.get("imageUrl").toString().removeQuotes(),
        headerUrl = this.get("headerImageUrl").toString().removeQuotes(),
            createdAt = this.get("createdAt").toString().removeQuotes(),
            updatedAt = this.get("updatedAt").toString().removeQuotes(),
        isPublic = this.get("isPublic").asBoolean,
            followers = this.get("numberOfFollowers").asLong,
            likes = this.get("pageLIkes").asLong,
            isFollowing = this.get("isFollowing").asBoolean,
            hasLiked = this.get("hasLikedThisPage").asBoolean
    )
}

fun JsonObject.getWayaGroup():WayaGroup{
    return WayaGroup(
            id = this.get("id").toString().removeQuotes(),
            ownerId = this.get("userId").toString().removeQuotes(),
            groupName = this.get("name").toString().removeQuotes(),
            description = this.get("description").toString().removeQuotes(),
            imageUrl = this.get("imageUrl").toString().removeQuotes(),
            headerUrl = this.get("headerImageUrl").toString().removeQuotes(),
            isPublic = this.get("isPublic").asBoolean,
            muted = this.get("mute").asBoolean,
            isDeleted = this.get("isDeleted").asBoolean,
            createdAt = this.get("createdAt").toString().removeQuotes(),
            updatedAt = this.get("updatedAt").toString().removeQuotes(),
    )
}

fun JsonObject.getWayaEvent():WayaEvent{
    return WayaEvent(
        id = this.get("id").toString().removeQuotes(),
        eventImageUrl = this.get("eventPoster").toString().removeQuotes(),
        creatorId = this.get("organizerId").toString().removeQuotes(),
        title = this.get("eventName").toString().removeQuotes(),
        eventLocation = this.get("location").toString().removeQuotes(),
        organizer = this.get("details").toString().removeQuotes(),
        startTime = this.get("eventStart").toString().removeQuotes().toLongOrNull() ?: System.currentTimeMillis(),
        endTime = this.get("eventEnd").toString().removeQuotes().toLongOrNull() ?: System.currentTimeMillis()
    )
}

fun JsonObject.getWayaGramUser():WayaGramUser{
    val userData = this.getAsJsonObject("user")
    return WayaGramUser(
        id = this.get("id").toString().removeQuotes(),
        avatar = this.get("avatar").toString().removeQuotes(),
        coverImage = this.get("coverImage").toString().removeQuotes() ?: "",
        username = this.get("username").toString().removeQuotes() ?: "",
        notPublic = this.get("notPublic").asBoolean,
        postCount = this.get("postCount").asInt,
        following = this.get("following").asInt,
        followers = this.get("followers").asInt,
        userId = userData.get("id").toString().removeQuotes() ?: "",
        email = userData.get("email").toString().removeQuotes() ?: "",
        firstName = userData.get("firstName").toString().removeQuotes() ?: "",
        surname =  userData.get("surname").toString().removeQuotes() ?: "",
        middleName = userData.get("middleName").toString().removeQuotes() ?: "",
        profileImage = userData.get("profileImage").toString().removeQuotes() ?: "",
        dateOfBirth =  userData.get("dateOfBirth").toString().removeQuotes() ?: "",
        gender = userData.get("gender").toString().removeQuotes() ?: "",
        district = userData.get("district").toString().removeQuotes() ?: "",
        address = userData.get("address").toString().removeQuotes() ?: "",
        phoneNumber = userData.get("phoneNumber").toString().removeQuotes() ?: "",
        authId = userData.get("userId").toString().removeQuotes() ?: "",
        city = userData.get("city").toString().removeQuotes() ?: "",
        corporate = userData.get("corporate").asBoolean,
    )
}

fun WayaPost.toMapRequestBody():MutableMap<String, RequestBody>{

    val profile_id = RequestBody.create(okhttp3.MultipartBody.FORM, "${this.userId}")
    val parent_id =
        if(this.parent_id == null) null
        else RequestBody.create(okhttp3.MultipartBody.FORM, "${this.parent_id}")
    val description = RequestBody.create(okhttp3.MultipartBody.FORM, "${this.text}")
    val group_id =
        if(this.group_id == null) null
        else RequestBody.create(okhttp3.MultipartBody.FORM, "${this.group_id}")
    val type =
        if(this.type == null) null
        else RequestBody.create(okhttp3.MultipartBody.FORM, this.serverType)
    val hashtags = RequestBody.create(okhttp3.MultipartBody.FORM, this.hashtags.toString())
    val mentions = RequestBody.create(okhttp3.MultipartBody.FORM, this.mentions.toString())
    val isPaid = RequestBody.create(okhttp3.MultipartBody.FORM, this.isPoolPaid.toString())
    val isPoll = RequestBody.create(okhttp3.MultipartBody.FORM, (this.type == PostType.VOTE).toString())
    val amount = RequestBody.create(okhttp3.MultipartBody.FORM, this.price.toString())
    val expiresIn = RequestBody.create(okhttp3.MultipartBody.FORM, this.poolEndTime.toString())
    val options = RequestBody.create(okhttp3.MultipartBody.FORM, this.listOptions.toString())

    val map =  mutableMapOf(
        "profile_id" to profile_id,
        "description" to description,
        //"hashtags" to hashtags,
        //"mentions" to mentions,
        "isPaid" to isPaid,
        "isPoll" to isPoll,
        "amount" to amount,
        "expiresIn" to expiresIn,
        //"options" to options
    )

    if(parent_id != null)map["parent_id"] = parent_id
    if(group_id != null)map["group_id"] = group_id
    if(type != null) map["type"] = type
    return map
}

fun WayaGroup.toGroupAsync():GroupAsync{
    return GroupAsync(
        userId = this.id,
        name = this.groupName,
        description = this.description,
        isPublic = this.isPublic,
    )
}

fun WayaGroup.toMapRequestBody():HashMap<String, RequestBody>{

    val userId = RequestBody.create(okhttp3.MultipartBody.FORM, this.ownerId)
    val name = RequestBody.create(okhttp3.MultipartBody.FORM, this.groupName.toString())
    val description = RequestBody.create(okhttp3.MultipartBody.FORM, this.description.toString())
    val isPublic = RequestBody.create(okhttp3.MultipartBody.FORM, this.isPublic.toString())

    return hashMapOf(
        "userId" to userId,
        "name" to name,
        "description" to description,
        "isPublic" to isPublic,
    )
}

fun WayaPost.getPostAsysnc():PostAsysnc{
    return PostAsysnc(
        profile_id = this.userId ?: "",
        parent_id = this.parent_id,
        description = this.text,
        group_id = this.group_id,
        type = this.serverType,
        hashtags = this.hashtags,
        mentions = this.mentions,
        isPaid = this.isPoolPaid,
        isPoll = this.type == PostType.VOTE,
        amount = this.price.toInt(),
        voteLimit = this.voteLimit,
        expiresIn = this.poolEndTime.toString(),
        options = this.listOptions
    )
}

fun JsonObject.toWayaPost(): WayaPost{
    val images = this.get("images").asJsonArray
    val profile = this.get("profile").asJsonObject
    val user = profile.get("user").asJsonObject
    val isPool = this.get("isPoll").asBoolean
    var poll = Poll()
    if(isPool)poll = this.get("poll").asJsonObject.toPool()
    val wayaUser = profile.getWayaGramUser()
    return WayaPost(
        id = this.get("id").toString().removeQuotes(),
        text = this.get("description").toString().removeQuotes(),
        hashtags = this.get("hashtags").asJsonArray.toListString(),
        mentions = this.get("mentions").asJsonArray.toListString(),
        type = this.get("type").toString().removeQuotes(),
        postType = PostType.NORMAL,
        parent_id = this.get("ParentId")?.toString() ?: "",
        group_id = this.get("GroupId")?.toString() ?: "",
        pageId = this.get("PageId")?.toString()?.removeQuotes() ?: "",
        isDeleted = this.get("isDeleted").asBoolean,
        isPoll = this.get("isPoll").asBoolean,
        createdAt = this.get("createdAt").toString().removeQuotes(),
        updatedAt = this.get("updatedAt").toString().removeQuotes(),
        avatar = profile.get("avatar").toString().removeQuotes(),
        username = profile.get("username").toString().removeQuotes(),
        gramUserId = profile.get("id").toString().removeQuotes(),
        userId = user.get("id").toString().removeQuotes(),
        userEmail = user.get("email").toString().removeQuotes(),
        firstName = user.get("firstName").toString().removeQuotes(),
        surname = user.get("surname").toString().removeQuotes(),
        authUserId = user.get("userId").toString().removeQuotes(),
        likes = this.get("likesCount").asLong,
        isLiked = this.get("isLiked").asBoolean,
        commentCount = this.get("commentCount").asLong,
        repostCont = this.get("repostCount").asLong,
        listImageUrl = images.toListOfImages(),
        user = wayaUser,
        poll = poll
    )
}

//Get list of vote options
fun JsonArray?.toListOptions(): List<String> {
    val options = mutableListOf<String>()
    if(this == null) return options
    for(i in 0 until  this.size()){
        options.add(this[i].toString().removeQuotes())
    }
    return options
}

//Get list votes from Post
fun JsonObject?.getVotes(list: MutableList<String>): MutableList<Vote> {
    val listVote = mutableListOf<Vote>()
    if(this == null) return listVote
    list.forEach {
        listVote.add(Vote(it, this.get(it).toString().removeQuotes().toIntOrNull() ?: 0))
    }
    return listVote
}

fun JsonObject.toPool(): Poll{
    val options = this.get("options").asJsonArray.toListOptions()
    return Poll(
        id = this.get("id").toString().removeQuotes(),
        postId = this.get("PostId").toString().removeQuotes(),
        isPaid = this.get("isPaid").asBoolean,
        amount = this.get("amount").asDouble,
        expiresIn = this.get("expiresIn").toString().removeQuotes().toLongOrNull() ?: System.currentTimeMillis(),
        voteLimit = this.get("voteLimit").asInt,
        options = options,
        isDeleted = this.get("isDeleted").asBoolean,
        createdAt = this.get("createdAt").toString().removeQuotes(),
        updatedAt = this.get("updatedAt").toString().removeQuotes(),
        listVotes = this.get("votes").asJsonObject.getVotes(options.toMutableList()),

    )
}

fun JsonArray.toListString(): List<String>{
    val strings = mutableListOf<String>()
    for(i in 0 until  this.size()){
        strings.add(this[i].toString().removeQuotes())
    }
    return strings
}

fun JsonArray?.toListOfImages(): List<String>{
    val images = mutableListOf<String>()
    if(this == null) return images

    for (i in 0 until this.size()){
        images.add(this[i].asJsonObject.get("imageURL").toString().removeQuotes())
    }

    return images
}

fun WayaEvent.toEventAsync():EVentAsync{
    return EVentAsync(
        organizerId = this.creatorId,
        location = this.eventLocation,
        eventName = this.title,
        details = this.organizer,
        eventStart = this.startTime.toString(),
        eventEnd =  this.endTime.toString()
    )
}

fun WayaEvent.toMapRequestBody():HashMap<String, RequestBody>{
    val organizerId = RequestBody.create(okhttp3.MultipartBody.FORM, this.creatorId)
    val location = RequestBody.create(okhttp3.MultipartBody.FORM, this.eventLocation)
    val eventName = RequestBody.create(okhttp3.MultipartBody.FORM, this.title)
    val details = RequestBody.create(okhttp3.MultipartBody.FORM, this.organizer)
    val eventStart = RequestBody.create(okhttp3.MultipartBody.FORM, this.startTime.toString())
    val eventEnd = RequestBody.create(okhttp3.MultipartBody.FORM, this.endTime.toString())

    return hashMapOf<String, RequestBody>(
        "organizerId" to organizerId,
        "location" to location,
        "eventName" to eventName,
        "details" to details,
        "eventStart" to eventStart,
        "eventEnd" to eventEnd
    )
}

fun WayaEvent.toEditRequestBody():HashMap<String, RequestBody>{
    val eventId = RequestBody.create(okhttp3.MultipartBody.FORM, this.id)
    val organizerId = RequestBody.create(okhttp3.MultipartBody.FORM, this.creatorId)
    val location = RequestBody.create(okhttp3.MultipartBody.FORM, this.eventLocation)
    val eventName = RequestBody.create(okhttp3.MultipartBody.FORM, this.title)
    val details = RequestBody.create(okhttp3.MultipartBody.FORM, this.organizer)
    val eventStart = RequestBody.create(okhttp3.MultipartBody.FORM, this.startTime.toString())
    val eventEnd = RequestBody.create(okhttp3.MultipartBody.FORM, this.endTime.toString())

    return hashMapOf<String, RequestBody>(
        "eventId" to eventId,
        "organizerId" to organizerId,
        "location" to location,
        "eventName" to eventName,
        "details" to details,
        "eventStart" to eventStart,
        "eventEnd" to eventEnd
    )
}

fun WayaPages.toMapRequestBody():HashMap<String, RequestBody>{

    val userId = RequestBody.create(okhttp3.MultipartBody.FORM, this.ownerId.toString())
    val categoryId = RequestBody.create(okhttp3.MultipartBody.FORM, this.categoryId.toString())
    val userName = RequestBody.create(okhttp3.MultipartBody.FORM, this.pageName.toString())
    val title = RequestBody.create(okhttp3.MultipartBody.FORM, this.pageName.toString())
    val description = RequestBody.create(okhttp3.MultipartBody.FORM, this.description.toString())
    val websiteUrl = RequestBody.create(okhttp3.MultipartBody.FORM, this.websiteUrl.toString())
    val isPublic = RequestBody.create(okhttp3.MultipartBody.FORM, this.isPublic.toString())

    return hashMapOf(
        "userId" to userId,
        "categoryId" to categoryId,
        "username" to userName,
        "title" to title,
        "description" to description,
        "websiteUrl" to websiteUrl,
        "isPublic" to isPublic,
    )
}

fun WayaPages.toPagesAsync():PagesAsync{
    return PagesAsync(
        userId = this.ownerId,
        categoryId = this.categoryId,
        username = this.pageName,
        title = this.pageName ?: "",
        description = this.description,
        websiteUrl = this.websiteUrl,
        isPublic = this.isPublic
    )
}