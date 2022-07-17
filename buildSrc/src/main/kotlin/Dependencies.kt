object Dependencies {

    //region common android
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlinVersion}"
    const val androidCoreKtx = "androidx.core:core-ktx:1.8.0"
    const val appCompat = "androidx.appcompat:appcompat:1.4.2"
    const val material = "com.google.android.material:material:1.6.1"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val androidLegacy = "androidx.legacy:legacy-support-v4:1.0.0"
    const val junit = "junit:junit:4.+"
    const val junitTestImplementation = "androidx.test.ext:junit:1.1.3"
    const val espressoTestImplementation = "androidx.test.espresso:espresso-core:3.4.0"
    const val androidLifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1"
    //end region

    //gson and coroutine
    const val gson = "com.google.code.gson:gson:2.8.6"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:2.9.0"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2"
    // end region

    // region ktx and safe args
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:2.4.2"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:2.4.2"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:1.4.1"
    //end region

    // region retrofit2 and logger
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val loggingIntercept = "com.squareup.okhttp3:logging-interceptor:4.8.1"
    const val moshi = "com.squareup.moshi:moshi:1.13.0"
    const val timber = "com.jakewharton.timber:timber:5.0.1"
    // end region

    // region coil
    const val coil = "io.coil-kt:coil:1.1.1"
    // end region

    // region hilt and jetpack viewmodel
    const val daggerHilt = "com.google.dagger:hilt-android:2.33-beta"
    const val daggerHiltCompilerKapt = "com.google.dagger:hilt-android-compiler:2.31.2-alpha"
    const val hiltLifecycle = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"
    const val hiltCompilerKapt = "androidx.hilt:hilt-compiler:1.0.0-beta01"
    // end region

    // region swipe refresh
    const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    const val motionLayout = "androidx.constraintlayout:constraintlayout:2.2.0-alpha03"
    const val shapeOfView = "io.github.florent37:shapeofview:1.4.7"
    // end region

    // region room
    const val roomRuntime = "androidx.room:room-runtime:${Version.roomVersion}"
    const val roomCompilerKapt = "androidx.room:room-compiler:${Version.roomVersion}"
    const val roomKtx = "androidx.room:room-ktx:${Version.roomVersion}"
    // end region

    // region permissionX
    const val permissionX = "com.guolindev.permissionx:permissionx:1.6.4"
    // end region
}