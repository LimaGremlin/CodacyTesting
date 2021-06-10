package com.wayapaychat.domain.interactors

import kotlinx.coroutines.flow.Flow

abstract class UseCaseWithNoParamsNoFlow<R> {

    abstract suspend fun buildUseCase(): R
}
