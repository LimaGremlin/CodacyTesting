package com.wayapaychat.core.utils

import com.wayapaychat.domain.utils.RxExecutionThread
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class WayaRxExecutionThread @Inject constructor(): RxExecutionThread {

    override val observerThread: Scheduler
        get() = AndroidSchedulers.mainThread()
}
