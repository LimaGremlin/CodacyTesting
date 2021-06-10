package com.wayapaychat.remote.base

data class BaseDataWrapperRemote<T>(val error: Boolean?, val code: Int?, val message: String?, val response: T? = null)
