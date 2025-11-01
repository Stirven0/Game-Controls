// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.9.1" apply false
    id("com.android.library")     version "8.9.1" apply false
}

// Centraliza la versión de todos los módulos
extra["libVersion"] = "0.0.4"

tasks.register<Delete>("cleanAll") {
    group       = "build"
    description = "Deletes all build folders in every module"
    allprojects.forEach { delete(it.layout.buildDirectory) }
}

tasks.register<Delete>("clean") {
    description = "Deletes root build directory"
    delete(rootProject.layout.buildDirectory)
}
