package com.wayapaychat.domain.interactors

import kotlinx.coroutines.flow.Flow

abstract class UseCaseWithNoParams<R> {

    abstract suspend fun buildUseCase(): Flow<R>
}
