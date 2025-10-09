plugins {
    id("com.android.library")
}

android {
    namespace = "com.aa.game_controllers"
    compileSdk = 35

    defaultConfig {
        minSdk = 34
        targetSdk = 35
        
        vectorDrawables { 
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    //implementation code
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.5.1")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.5.1")
    implementation("androidx.core:core:1.12.0")
}
