import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.3.61"
}

repositories {
    mavenCentral()
}

version = "0.0.2"

val mainTitle = "JMX Helper CLI"
val mainClasspath = "ca.ambiguities.java.helpers.jmx.JMXHelper"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("commons-cli:commons-cli:1.4")
    implementation("com.google.code.gson:gson:2.8.6")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<Jar> {
        manifest {
            attributes(mapOf("Implementation-Title" to mainTitle))
            attributes(mapOf("Implementation-Version" to archiveVersion))
            attributes(mapOf("Main-Class" to mainClasspath))
        }
        from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    }
}

tasks.register<Copy>("getDeps") {
    from(configurations.runtimeClasspath)
    into("libs/")
}