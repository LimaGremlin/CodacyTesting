package com.wayapaychat.repository.models

data class BaseRepositoryDataWrapper<T>(val error: Boolean?, val code: Int?, val message: String?, val response: T? = null)
