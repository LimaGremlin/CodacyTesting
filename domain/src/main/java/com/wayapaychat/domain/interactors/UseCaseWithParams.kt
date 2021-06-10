package com.wayapaychat.domain.interactors

import kotlinx.coroutines.flow.Flow

abstract class UseCaseWithParams<in P, R> {

    abstract suspend fun buildUseCase(param: P?): Flow<R>
}
