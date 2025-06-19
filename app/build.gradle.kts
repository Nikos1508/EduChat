import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.10"
}

android {
    namespace = "com.example.educhat"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.educhat"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProps = Properties()
        val localPropsFile = rootProject.file("local.properties")

        if (localPropsFile.exists()) {
            localProps.load(FileInputStream(localPropsFile))
        }

        val supabaseKey = localProps.getProperty("supabaseKey") ?: ""
        val supabaseUrl = localProps.getProperty("supabaseUrl") ?: ""

        buildConfigField("String", "supabaseKey", "\"$supabaseKey\"")
        buildConfigField("String", "supabaseUrl", "\"$supabaseUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Core libraries
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-ktx:1.8.0")

    // Compose BOM (use this BOM to align all Compose dependencies)
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))

    // Compose UI dependencies without explicit versions (managed by BOM)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-graphics")

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.google.android.material:material:1.10.0")
    implementation(libs.googleid)

    // Compose Preview/Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Supabase
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.0"))
    implementation("io.github.jan-tennert.supabase:gotrue-kt:1.3.2")
    implementation("io.github.jan-tennert.supabase:supabase-kt:1.3.3")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:1.3.2")
    implementation("io.github.jan-tennert.supabase:auth-kt:1.3.2")
    implementation("io.github.jan-tennert.supabase:realtime-kt:1.3.2")
    implementation("io.ktor:ktor-client-cio:3.1.3")

    // Networking (Ktor)
    implementation("io.ktor:ktor-client-android:3.1.3")

    //View Model
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")
}