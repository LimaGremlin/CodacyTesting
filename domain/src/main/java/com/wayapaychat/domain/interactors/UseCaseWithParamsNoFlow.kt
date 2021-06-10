package com.wayapaychat.domain.interactors

import kotlinx.coroutines.flow.Flow

abstract class UseCaseWithParamsNoFlow<in P, R> {

    abstract suspend fun buildUseCase(param: P?): R
}
