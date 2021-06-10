object DependenciesVersions {
    const val targetSdkVersion = 29
    const val compileSdkVersion = 29
    const val minSdkVersion = 19

    const val versionCode = 1
    const val versionName = "1.0"

    const val kotlin = "1.3.41"
    const val coreKtx = "1.3.2"
    const val fragmentKtx = "1.3.0"
    const val appCompat = "1.2.0"
    const val lifecycle = "2.3.0"
    const val junit = "4.12"
    const val espresso = "3.2.0"
    const val runner = "1.2.0"
    const val hdodenhofCircleImage = "3.1.0"

    const val javaxInject = "1"
    const val javaxAnnotation = "1.0"
    const val room = "2.2.0-alpha01"
    const val rxJava = "2.2.10"
    const val rxAndroid = "2.1.1"
    const val okhttp = "3.12.0"
    const val retrofit = "2.6.0"
    const val recyclerAnimator = "3.0.0"
    const val moshi = "1.11.0"
    const val picasso = "2.8"
    const val materialComponent = "1.4.0-alpha01"
    const val dagger = "2.31"
    const val stetho = "1.5.1"
    const val timber = "4.7.1"
    const val navigation = "2.3.3"
    const val constraintLayout = "2.0.4"
    const val rxAnimation = "0.0.6"
    const val scopes = "2.2.0"
    const val pinView = "2.1.2"
    const val shimmerLayout = "0.4.0"
    const val fresco = "2.0.0"
    const val multidex = "2.0.1"
    const val googleServices = "4.3.5"
    const val overscrollDecor = "1.0.4"
    const val viewPagerTransformer = "1.0.1@aar"
    const val pagerIndicator = "1.0.3"
    const val alexanderMaps = "1.1.0"
    const val googleMaps = "16.1.0"
    const val locationServices = "15.0.1"
    const val deviceName = "1.1.9"
    const val workManager = "2.2.0"
    const val stepView = "1.0.2"
    const val easingAnimation = "2.0@aar"
    const val androidAnimation = "2.3@aar"
    const val playServicesAuth = "17.0.0"
    const val playServicesAuthPhone = "17.1.0"
    const val roundProgressBar = "2.0.3"
    const val materialRatingBar = "1.3.2"
    const val mapsLibrary = "0.5"
    const val debugDatabaseLibrary = "1.0.6"
    const val gson = "2.8.5"
    const val fabric = "2.10.1"
    const val firebaseCore = "17.0.1"
    const val firebaseAnalytics = "17.0.1"
    const val firebaseMessaging = "20.0.0"
    const val fabricClasspath = "1.31.0"
    const val firebaseGradle = "2.5.2"
    const val coroutines = "1.3.9"

    const val gradle = "4.1.0"
    const val testExt = "1.1.1"
    const val mockk = "1.9.3"
    const val rxPermissions = "0.10.2"
    const val rxLocation = "1.0.5"
    const val rxBinding = "3.0.0"
    const val smartLocation = "3.3.3"
    const val places = "2.0.0"
    const val mapDirection = "1.2.0"
    const val glide = "4.9.0"
    const val ankoVersion = "0.10.8"
    const val lottie = "3.0.7"
    const val guava = "28.1-android"
    const val horizontalCalender = "1.3.4"
    const val materialDatePicker = "4.2.3"
    const val niceSpinner = "1.4.3"
    const val hilt = "2.32-alpha"
    const val hiltLifeCycle = "1.0.0-alpha03"
    const val dexter = "6.2.2"
    const val pix = "1.5.6"
    const val zxingCore = "3.2.1"
    const val zxingAndroid = "3.2.0@aar"
    const val iTextPDF = "5.5.10"
}

object WayaCoreDependencies {
    const val packageName = "com.wayapaychat"
    const val homeValue = "home"
    const val GOOGLE_MAPS_API_KEY = "AIzaSyBtDIkor7WntByDyzdh7pwCGqyfWiegyk8"
    const val GOOGLE_MAPS_API_KEY_BUILD_CONFIG = "\"AIzaSyBtDIkor7WntByDyzdh7pwCGqyfWiegyk8\""
    const val FABRIC_API_KEY = "05aaeca8f0c70e4668afe6d90f66e5b7cb84fef7"

    const val AES_ENCRYPTION_PASSWORD = "\"lbwyBzfgzUIvXZFShJuikaWvLJhIVq36\""

    const val BASE_URL = "\"http://68.183.60.114:8059/\""
    const val PASSWORD_URL = "\"http://46.101.41.187:8060/\""
    const val PROFILE_URL = "\"http://46.101.41.187:8080/\""
    const val QR_CODE_URL = "\"http://157.230.223.54:8064/\""
    const val WALLET_URL = "\"http://157.230.223.54:9009/\""
    const val CARD_URL = "\"http://157.230.223.54:3020/card-service/\""

    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${DependenciesVersions.kotlin}"
    const val coreKtx = "androidx.core:core-ktx:${DependenciesVersions.coreKtx}"
    const val appCompat = "androidx.appcompat:appcompat:${DependenciesVersions.appCompat}"
    const val androidFragmentKtx =
        "androidx.fragment:fragment-ktx:${DependenciesVersions.fragmentKtx}"
    const val viewModelLifeCycle =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${DependenciesVersions.lifecycle}"
    const val liveDataLifeCycle =
        "androidx.lifecycle:lifecycle-livedata-ktx:${DependenciesVersions.lifecycle}"
    const val runtimeKtxLiftCycle =
        "androidx.lifecycle:lifecycle-runtime-ktx:${DependenciesVersions.lifecycle}"
    const val lifecycleCompiler =
        "androidx.lifecycle:lifecycle-compiler:${DependenciesVersions.lifecycle}"
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${DependenciesVersions.navigation}"
    const val navigationUiKtx =
        "androidx.navigation:navigation-ui-ktx:${DependenciesVersions.navigation}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${DependenciesVersions.constraintLayout}"
    const val multidex = "androidx.multidex:multidex:${DependenciesVersions.multidex}"
    const val hilt = "com.google.dagger:hilt-android:${DependenciesVersions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${DependenciesVersions.hilt}"
    const val hiltViewModelLifeCycle =
        "androidx.hilt:hilt-lifecycle-viewmodel:${DependenciesVersions.hiltLifeCycle}"
    const val coroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${DependenciesVersions.coroutines}"
    const val viewModelScope =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${DependenciesVersions.scopes}"
    const val lifeCycleScope =
        "androidx.lifecycle:lifecycle-runtime-ktx:${DependenciesVersions.scopes}"
    const val liveDataScope =
        "androidx.lifecycle:lifecycle-livedata-ktx:${DependenciesVersions.scopes}"
    const val guava = "com.google.guava:guava:${DependenciesVersions.guava}"
}

object WayaGooglePlayDependenciesDependencies {
    const val googleMaps =
        "com.google.android.gms:play-services-maps:${DependenciesVersions.googleMaps}"
    const val googleLocation =
        "com.google.android.gms:play-services-location:${DependenciesVersions.locationServices}"
    const val places = "com.google.android.libraries.places:places:${DependenciesVersions.places}"
    const val placesCompat =
        "com.google.android.libraries.places:places-compat:${DependenciesVersions.places}"
    const val playServicesAuth =
        "com.google.android.gms:play-services-auth:${DependenciesVersions.playServicesAuth}"
    const val playServicesAuthPhone =
        "com.google.android.gms:play-services-auth-api-phone:${DependenciesVersions.playServicesAuthPhone}"
}

object WayaWorkManagerDependencies {
    const val workManager = "androidx.work:work-runtime-ktx:${DependenciesVersions.workManager}"
    const val workManagerRx = "androidx.work:work-rxjava2:${DependenciesVersions.workManager}"
}

object WayaTestDependencies {
    const val junit = "junit:junit:${DependenciesVersions.junit}"
    const val mockk = "io.mockk:mockk:${DependenciesVersions.mockk}"
    const val coreTesting = "androidx.arch.core:core-testing:${DependenciesVersions.lifecycle}"
    const val roomTesting = "androidx.room:room-testing:${DependenciesVersions.room}"
}

object WayaAndroidTestDependencies {
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    const val espresso = "androidx.test.espresso:espresso-core:${DependenciesVersions.espresso}"
    const val testRunner = "androidx.test:runner:${DependenciesVersions.runner}"
    const val textExt = "androidx.test.ext:junit:${DependenciesVersions.testExt}"
    const val mockk = "io.mockk:mockk-android:${DependenciesVersions.mockk}"
    const val workManagerTesting = "androidx.work:work-testing:${DependenciesVersions.workManager}"
}

object WayaViewDependencies {
    const val materialComponent =
        "com.google.android.material:material:${DependenciesVersions.materialComponent}"
    const val rxAnimation = "com.mikhaellopez:rxanimation:${DependenciesVersions.rxAnimation}"
    const val shimmerLayout = "com.facebook.shimmer:shimmer:${DependenciesVersions.shimmerLayout}"
    const val fresco = "com.facebook.fresco:fresco:${DependenciesVersions.fresco}"
    const val pinView =
        "com.github.mukeshsolanki:android-otpview-pinview:${DependenciesVersions.pinView}"
    const val stepView = "com.params.stepview:stepview:${DependenciesVersions.stepView}"
}

object WayaNetworkDependencies {
    const val okhttp = "com.squareup.okhttp3:okhttp:${DependenciesVersions.okhttp}"
    const val loggingInterceptor =
        "com.squareup.okhttp3:logging-interceptor:${DependenciesVersions.okhttp}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${DependenciesVersions.retrofit}"
    const val gsonConverter =
        "com.squareup.retrofit2:converter-gson:${DependenciesVersions.retrofit}"
    const val rxJavaAdapter =
        "com.squareup.retrofit2:adapter-rxjava2:${DependenciesVersions.retrofit}"
    const val stetho = "com.facebook.stetho:stetho:${DependenciesVersions.stetho}"
    const val stethoOkhttp = "com.facebook.stetho:stetho-okhttp3:${DependenciesVersions.stetho}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${DependenciesVersions.moshi}"
}

object WayaAsyncDependencies {
    const val rxJava = "io.reactivex.rxjava2:rxjava:${DependenciesVersions.rxJava}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${DependenciesVersions.rxAndroid}"
}

object WayaDependencyInjectionDependencies {
    const val javax = "javax.inject:javax.inject:${DependenciesVersions.javaxInject}"
    const val javaxAnnotation =
        "javax.annotation:jsr250-api:${DependenciesVersions.javaxAnnotation}"
    const val dagger = "com.google.dagger:dagger:${DependenciesVersions.dagger}"
    const val daggerAndroidSupport =
        "com.google.dagger:dagger-android-support:${DependenciesVersions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${DependenciesVersions.dagger}"
    const val daggerAndroidProcessor =
        "com.google.dagger:dagger-android-processor:${DependenciesVersions.dagger}"
}

object WayaPersistenceDependencies {
    const val room = "androidx.room:room-runtime:${DependenciesVersions.room}"
    const val roomKtx = "androidx.room:room-ktx:${DependenciesVersions.room}"
    const val roomRx = "androidx.room:room-rxjava2:${DependenciesVersions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${DependenciesVersions.room}"
    const val gson = "com.google.code.gson:gson:${DependenciesVersions.gson}"
}

object WayaUtilityDependencies {
    const val timber = "com.jakewharton.timber:timber:${DependenciesVersions.timber}"
    const val hdodenhofCircleImage =
        "de.hdodenhof:circleimageview:${DependenciesVersions.hdodenhofCircleImage}"
    const val picasso = "com.squareup.picasso:picasso:${DependenciesVersions.picasso}"
    const val pagerIndicator =
        "com.romandanylyk:pageindicatorview:${DependenciesVersions.pagerIndicator}"
    const val recyclerAnimator =
        "jp.wasabeef:recyclerview-animators:${DependenciesVersions.recyclerAnimator}"
    const val overscrollDecor =
        "me.everything:overscroll-decor-android:${DependenciesVersions.overscrollDecor}"
    const val viewPagerTransformer =
        "com.eftimoff:android-viewpager-transformers:${DependenciesVersions.viewPagerTransformer}"
    const val easingAnimation =
        "com.daimajia.easing:library:${DependenciesVersions.easingAnimation}"
    const val androidAnimation =
        "com.daimajia.androidanimations:library:${DependenciesVersions.androidAnimation}"
    const val deviceName =
        "com.jaredrummler:android-device-names:${DependenciesVersions.deviceName}"
    const val roundCornerProgressBar =
        "com.akexorcist:RoundCornerProgressBar:${DependenciesVersions.roundProgressBar}"
    const val materialRatingBar =
        "me.zhanghai.android.materialratingbar:library:${DependenciesVersions.materialRatingBar}"
    const val mapsLibrary =
        "com.google.maps.android:android-maps-utils:${DependenciesVersions.mapsLibrary}"
    const val debugDatabaseLibrary =
        "com.amitshekhar.android:debug-db:${DependenciesVersions.debugDatabaseLibrary}"
    const val rxPermissions =
        "com.github.tbruyelle:rxpermissions:${DependenciesVersions.rxPermissions}"
    const val rxLocation = "com.patloew.rxlocation:rxlocation:${DependenciesVersions.rxLocation}"
    const val rxBinding = "com.jakewharton.rxbinding3:rxbinding:${DependenciesVersions.rxBinding}"
    const val rxBindingCore =
        "com.jakewharton.rxbinding3:rxbinding-core:${DependenciesVersions.rxBinding}"
    const val rxBindingAppCompat =
        "com.jakewharton.rxbinding3:rxbinding-appcompat:${DependenciesVersions.rxBinding}"
    const val rxBindingMaterial =
        "com.jakewharton.rxbinding3:rxbinding-material:${DependenciesVersions.rxBinding}"
    const val smartLocation =
        "io.nlopez.smartlocation:library:${DependenciesVersions.smartLocation}"
    const val smartLocationRx = "io.nlopez.smartlocation:rx:${DependenciesVersions.smartLocation}"
    const val mapDirection =
        "com.akexorcist:google-direction-library:${DependenciesVersions.mapDirection}"
    const val glide = "com.github.bumptech.glide:glide:${DependenciesVersions.glide}"
    const val ankoVersion = "org.jetbrains.anko:anko-commons:${DependenciesVersions.ankoVersion}"
    const val lottie = "com.airbnb.android:lottie:${DependenciesVersions.lottie}"
    const val horizontalCalender =
        "devs.mulham.horizontalcalendar:horizontalcalendar:${DependenciesVersions.horizontalCalender}"
    const val materialDatePicker =
        "com.wdullaer:materialdatetimepicker:${DependenciesVersions.materialDatePicker}"
    const val niceSpinner = "com.github.arcadefire:nice-spinner:${DependenciesVersions.niceSpinner}"
    const val alexanderMaps =
        "com.github.jd-alexander:library:${DependenciesVersions.alexanderMaps}"
    const val dexter =
        "com.karumi:dexter:${DependenciesVersions.dexter}"
    const val pix =
        "com.fxn769:pix:${DependenciesVersions.pix}"

    //QR Code Generator
    const val zxingCore = "com.google.zxing:core:${DependenciesVersions.zxingCore}"
    const val zxingAndroid = "com.journeyapps:zxing-android-embedded:${DependenciesVersions.zxingAndroid}"

    const val iTextPDF = "com.itextpdf:itextg:${DependenciesVersions.iTextPDF}"
}

object WayaFirebaseDependencies {
    const val messaging =
        "com.google.firebase:firebase-messaging:${DependenciesVersions.firebaseMessaging}"
    const val analytics =
        "com.google.firebase:firebase-analytics:${DependenciesVersions.firebaseAnalytics}"
}

object WayaClasspaths {
    const val gradle = "com.android.tools.build:gradle:${DependenciesVersions.gradle}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${DependenciesVersions.kotlin}"
    
    const val navigation =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${DependenciesVersions.navigation}"
    const val fabric = "io.fabric.tools:gradle:${DependenciesVersions.fabricClasspath}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${DependenciesVersions.hilt}"
    const val googleServices = "com.google.gms:google-services:${DependenciesVersions.googleServices}"
    const val firebaseGradle =
        "com.google.firebase:firebase-crashlytics-gradle:${DependenciesVersions.firebaseGradle}"
}

object WayaAnalyticsDependencies {
    const val fabric = "com.crashlytics.sdk.android:crashlytics:${DependenciesVersions.fabric}"
    const val analytics =
        "com.google.firebase:firebase-analytics:${DependenciesVersions.firebaseAnalytics}"
    const val firebaseCore =
        "com.crashlytics.sdk.android:crashlytics:${DependenciesVersions.fabric}"
}

object NavigationHome {
    const val authHome =
        "${WayaCoreDependencies.packageName}.auth.${WayaCoreDependencies.homeValue}"
    const val paymentHome =
        "${WayaCoreDependencies.packageName}.payment.${WayaCoreDependencies.homeValue}"
    const val settingsHome =
        "${WayaCoreDependencies.packageName}.settings.${WayaCoreDependencies.homeValue}"
    const val profileHome =
        "${WayaCoreDependencies.packageName}.profile.${WayaCoreDependencies.homeValue}"
    const val onboardingHome =
        "${WayaCoreDependencies.packageName}.onboarding.${WayaCoreDependencies.homeValue}"
    const val landingHome =
        "${WayaCoreDependencies.packageName}.landing.${WayaCoreDependencies.homeValue}"
    const val walletHome =
        "${WayaCoreDependencies.packageName}.wallet.${WayaCoreDependencies.homeValue}"
    const val qrCodeProfileHome =
        "${WayaCoreDependencies.packageName}.qr_profile.${WayaCoreDependencies.homeValue}"
    const val searchHome =
        "${WayaCoreDependencies.packageName}.search.${WayaCoreDependencies.homeValue}"
    const val gramProfileHome =
        "${WayaCoreDependencies.packageName}.gram_profile.${WayaCoreDependencies.homeValue}"
}
