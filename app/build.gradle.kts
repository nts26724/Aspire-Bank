plugins {
//    id("com.android.application")
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    signingConfigs {
        create("my_config") {
            storeFile = file("D:\\Androi\\final exam\\keystore.jks")
            storePassword = "123456"
            keyPassword = "123456"
            keyAlias = "keystore"
        }
    }
    namespace = "com.example.app"
    compileSdk = 36

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "AMADEUS_API_KEY", "\"${project.properties["AMADEUS_API_KEY"]}\"")
        buildConfigField("String", "AMADEUS_API_SECRET", "\"${project.properties["AMADEUS_API_SECRET"]}\"")

        buildConfigField("String", "DUFFEL_API_KEY", "\"${project.properties["DUFFEL_API_KEY"]}\"")

        buildConfigField("String", "VNP_TMNCODE", "\"${project.properties["VNP_TMNCODE"]}\"")
        buildConfigField("String", "VNP_HASHSECRET", "\"${project.properties["VNP_HASHSECRET"]}\"")


    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("my_config")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.android.flexbox:flexbox:3.0.0")

    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.libraries.places:places:3.0.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}