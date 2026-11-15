allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val newBuildDir: Directory =
    rootProject.layout.buildDirectory
        .dir("../../build")
        .get()
rootProject.layout.buildDirectory.value(newBuildDir)

// Several pub plugins (e.g. dynamic_color 1.9.x, auto_orientation_v2 2.4.x)
// use a `kotlin { ... }` block but never apply the Kotlin Android plugin,
// which used to be provided implicitly by the old plugin wiring. Apply it to
// every Android library subproject as soon as the Android plugin is applied.
subprojects {
    if (name != "app") {
        pluginManager.withPlugin("com.android.library") {
            pluginManager.apply("org.jetbrains.kotlin.android")
        }
    }
}

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
