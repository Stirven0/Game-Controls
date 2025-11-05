plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "com.aa.game_controls"
    compileSdk = 35

    defaultConfig {
        minSdk = 29
        vectorDrawables { useSupportLibrary = true }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    //implementation("com.github.Stirven0:Crash-Handler:v0.0.3")
    
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.core:core:1.12.0")
}

// Tarea que copia y renombra el AAR release
tasks.register<Copy>("releaseAar") {
    group       = "publishing"
    description = "Genera AAR release con versión"
    dependsOn("assembleRelease")
    from(tasks.named("bundleReleaseAar"))
    include("*.aar")
    into(layout.buildDirectory.dir("outputs/release"))
    rename("${project.getName()}-release.aar", "${project.getName()}-${rootProject.extra["libVersion"]}.aar")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.Stirven0"
                artifactId = "${project.getName()}"
                version = "${rootProject.extra["libVersion"]}"
            }
        }
    }
}