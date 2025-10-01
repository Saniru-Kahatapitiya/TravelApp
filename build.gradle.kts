// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// Add this to resolve IllegalAccessError with Kapt
// Ensure you are using a compatible JDK (e.g., JDK 11)
import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
            freeCompilerArgs += "-Xskip-prerelease-check"
        }
    }
    tasks.withType<JavaCompile> {
        options.compilerArgs.add("--add-opens=java.base/com.sun.tools.javac.main=ALL-UNNAMED")
    }
}