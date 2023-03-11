plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("com.squareup.sqldelight:runtime:1.5.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val androidMain by getting {
            // we have to provide android specific dependencies here
            dependencies {
                // to create local db - android specific
                implementation("com.squareup.sqldelight:android-driver:1.5.3")
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            // to create local db - ios specific
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:1.5.3")
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

sqldelight{
    // database setup
    database("KmmNoteDb") {
        packageName = "kotlinmm.app.kotlinmmapp"
        sourceFolders = listOf("sqldelight")
    }
}

android {
    namespace = "kotlinmm.app.kotlinmmapp"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}