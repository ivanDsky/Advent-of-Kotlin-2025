plugins {
    kotlin("jvm") version "2.2.21"
}

sourceSets {
    main {
        kotlin.srcDir("src")

        dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
            implementation("tools.aqua:z3-turnkey:4.14.1")
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}
