package com.wayapaychat.core.utils.state

class WayaAppResource <out D> constructor(
    val state: WayaAppState,
    val message: String? = null,
    val data: D? = null
){
    companion object {
        @JvmStatic
        fun <D> success(
            data: D? = null,
            message: String? = null
        ): WayaAppResource<D> = WayaAppResource(
            state = WayaAppState.SUCCESS,
            data = data,
            message = message
        )

        @JvmStatic
        fun <D> warning(
            message: String? = null
        ): WayaAppResource<D> = WayaAppResource(
            state = WayaAppState.WARNING,
            message = message
        )

        @JvmStatic
        fun <D> validationFailed(
            message: String? = null
        ): WayaAppResource<D> = WayaAppResource(
            state = WayaAppState.VALIDATION_FAILED,
            message = message
        )

        @JvmStatic
        fun <D> failed(
            message: String?
        ): WayaAppResource<D> = WayaAppResource(
            state = WayaAppState.FAILED,
            data = null,
            message = message
        )

        @JvmStatic
        fun <D> loading(): WayaAppResource<D> = WayaAppResource(
            state = WayaAppState.LOADING,
            data = null,
            message = null
        )

        @JvmStatic
        fun <D> loadingMore(): WayaAppResource<D> = WayaAppResource(
            state = WayaAppState.LOADING_MORE,
            data = null,
            message = null
        )
    }
}
