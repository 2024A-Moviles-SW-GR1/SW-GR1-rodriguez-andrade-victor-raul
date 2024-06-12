plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.toedter:jcalendar:1.4")
    implementation("com.formdev:flatlaf:1.6")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(16)
}