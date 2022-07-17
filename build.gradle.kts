// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.google.com")
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Version.gradleVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlinVersion}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Version.gradleHiltVersion}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Version.navVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlinGradleVersion}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}