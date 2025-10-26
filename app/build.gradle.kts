
plugins {
    id("com.android.application")
    
}

android {
    namespace = "com.example.gamecontrolsproyect"
    compileSdk = 35
    
    
    defaultConfig {
        applicationId = "com.example.gamecontrolsproyect"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
        
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    
    buildFeatures {
        viewBinding = true
        
    }
    
}

dependencies {
    implementation(project(":game-controls"))
    implementation("com.github.Stirven0:Crash-Handler:v0.0.3")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
}
