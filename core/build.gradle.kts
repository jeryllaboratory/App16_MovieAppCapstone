import org.gradle.internal.impldep.bsh.commands.dir
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp") version "2.0.10-1.0.24"
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}
apply(from = "../shared_dependencies.gradle")

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

// Get the values from local.properties
val accessToken: String = localProperties.getProperty("ACCESS_TOKEN", "")
val baseUrl: String = localProperties.getProperty("BASE_URL", "")
val baseUrlImage: String = localProperties.getProperty("BASE_URL_IMAGE", "")

android {
    namespace = "com.jeryl.app16_movieappcapstone.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        
        buildConfigField("String", "ACCESS_TOKEN", "\"$accessToken\"")
        buildConfigField("String", "BASE_URL_IMAGE", "\"$baseUrlImage\"")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Data Persistence & Databases
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.room.compiler)


    // Networking & APIs
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    annotationProcessor(libs.compiler)

    // Lifecycle & ViewModel
    api(libs.androidx.lifecycle.livedata.ktx)

    //    Koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation("io.insert-koin:koin-androidx-viewmodel:2.1.6")

    //Encrypt
    implementation(libs.android.database.sqlcipher)
    implementation(libs.androidx.sqlite.ktx)
}

