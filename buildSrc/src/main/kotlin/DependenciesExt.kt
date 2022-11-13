import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.common() {
    add("implementation", Dependencies.kotlinStdlib)
    add("implementation", Dependencies.androidCoreKtx)
    add("implementation", Dependencies.appCompat)
    add("implementation", Dependencies.material)
    add("implementation", Dependencies.preferenceManager)
    add("implementation", Dependencies.constraintLayout)
    add("implementation", Dependencies.androidLegacy)
    add("implementation", Dependencies.androidLifecycle)
    add("implementation", Dependencies.coroutinesCore)
}

fun DependencyHandler.test() {
    add("testImplementation", Dependencies.junit)
    add("androidTestImplementation", Dependencies.junitTestImplementation)
    add("androidTestImplementation", Dependencies.espressoTestImplementation)
}

fun DependencyHandler.network() {
    add("implementation", Dependencies.serialization)
    add("implementation", Dependencies.moshi)
    add("implementation", Dependencies.moshiConverter)
    add("implementation", Dependencies.retrofit)
    add("implementation", Dependencies.loggingIntercept)
    add("implementation", Dependencies.timber)
    add("kapt", Dependencies.moshiKapt)
}

fun DependencyHandler.fragmentKtx() {
    add("implementation", Dependencies.navigationFragment)
    add("implementation", Dependencies.navigationUi)
    add("implementation", Dependencies.fragmentKtx)
}

fun DependencyHandler.hilt() {
    add("implementation", Dependencies.daggerHilt)
    add("implementation", Dependencies.hiltLifecycle)
    add("kapt", Dependencies.daggerHiltCompilerKapt)
    add("kapt", Dependencies.hiltCompilerKapt)
}

fun DependencyHandler.widget() {
    add("implementation", Dependencies.coil)
    add("implementation", Dependencies.swipeRefresh)
    add("implementation", Dependencies.shapeOfView)
    add("implementation", Dependencies.motionLayout)
    add("implementation", Dependencies.permissionX)
}

fun DependencyHandler.room() {
    add("implementation", Dependencies.roomRuntime)
    add("implementation", Dependencies.roomKtx)
    add("kapt", Dependencies.roomCompilerKapt)
}