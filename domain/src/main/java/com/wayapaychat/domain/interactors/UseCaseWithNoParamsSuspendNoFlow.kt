package com.wayapaychat.domain.interactors

import kotlinx.coroutines.flow.Flow

abstract class UseCaseWithNoParamsSuspendNoFlow<R> {

    abstract suspend fun buildUseCase(): R
}
