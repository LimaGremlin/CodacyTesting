package com.wayapaychat.core.wayagram

import java.io.Serializable

data class Vote(
    var option: String,
    var cout:Int = 0
):Serializable

data class Poll(
    val id: String = "",
    val postId: String = "",
    val isPaid: Boolean = false,
    var isVoted: Boolean = false,
    val amount: Double = 0.0,
    val expiresIn: Long = System.currentTimeMillis(),
    val voteLimit: Int = 1,
    val options: List<String> = listOf(),
    var listVotes: MutableList<Vote> = mutableListOf(),
    var isDeleted: Boolean = false,
    var createdAt: String = "",
    var updatedAt: String = "",
):Serializable