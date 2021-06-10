package com.wayapaychat.domain.utils

import io.reactivex.Scheduler

interface  RxExecutionThread {
    val observerThread: Scheduler
}
