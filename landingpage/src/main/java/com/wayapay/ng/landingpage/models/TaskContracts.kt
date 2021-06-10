package com.wayapay.ng.landingpage.models

const val privacy_policy_url = "https://www.wayapaychat.com/terms-of-use"

object SelectOptions{
    const val AUCTION = "AUCTION"
    const val DONATION = "DONATION"
    const val POST = "POST"
    const val MOMENTS = "MOMENTS"
    const val PAGES = "PAGES"
    const val GROUP = "GROUP"
    const val EVENTS = "EVENTS"
}

object PostType{
    const val VOTE = "VOTE"
    const val PAID = "PAID"
    const val PRODUCT = "PRODUCT"
    const val NORMAL = "NORMAL"
    const val USER = "user"
    const val GROUP = "group"
    const val PAGE = "page"
}

object RequestCodes{
    const val EVENT_ACTIVITY_RESULT = 101
    const val POST_ACTIVITY_RESULT = 106
    const val PAGE_ACTIVITY_RESULT = 107
    const val GROUP_ACTIVITY_RESULT = 108
    const val DONATION_ACTIVITY_RESULT = 109
    const val EVENT_EDIT_ACTIVITY_RESULT = 102
    const val READ_REQUEST_CODE: Int = 103
    const val REQUEST_TAKE_PHOTO: Int = 104
    const val CROP_IMAGE = 105
}

object Tags{
    const val START_DATE = "START_DATE"
    const val START_TIME = "START_TIME"
    const val END_DATE = "END_DATE"
    const val END_TIME = "END_TIME"
    const val EVENTS = "EVENTS"
    const val EDIT = "EDIT"
    const val POST_MAIN = "POST_MAIN"
    const val INTEREST = "INTEREST"
    const val MY_GROUP = "MY_GROUP"
    const val JOINED_GROUP = "JOINED_GROUP"
    const val FOLLOWED_PAGE = "FOLLOWED_PAGE"
    const val MY_PAGE = "MY_PAGE"
}

object IntentBundles{
    const val EVENT_KEY = "EVENT:KEY"
    const val DONATION_KEY = "DONATION:KEY"
    const val POST_KEY = "POST KEY"
    const val IMAGE_URI_KEY = "IMAGE_URI_KEY"
    const val INTENT_ACTION_KEY = "INTENT_ACTION_KEY"
    const val PAGES_KEY = "PAGES:KEY"
    const val GRAM_PROFILE_KEY = "GRAM_PROFILE_KEY"
    const val PROFILE_KEY = "PROFILE_KEY"
    const val USER_ID_KEY = "USER_ID:KEY"
    const val GROUP_KEY = "GROUP:KEY"
    const val SEARCH_KEY = "SEARCH:KEY"
}

object RxCropActions{
    const val RECTANGLE = "rectangle:action"
    const val SQUARE = "square:action"
}

object LandingOptions{
    const val MAKE_RESERVATIONS = "MAKE_RESERVATIONS"
    const val SHARE_EVENT = "SHARE"
    const val EDIT_EVENT = "EDIT"
    const val REPORT_EVENT = "REPORT_EVENT"
}

val listUserEventOption = listOf(
     LandingOptions.MAKE_RESERVATIONS,
     LandingOptions.SHARE_EVENT,
    LandingOptions.REPORT_EVENT,
)

val listOwnerEventOption = listOf(
    LandingOptions.EDIT_EVENT,
    LandingOptions.MAKE_RESERVATIONS,
    LandingOptions.SHARE_EVENT,
)