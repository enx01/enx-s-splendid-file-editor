plugins {
    id("java")
    id("application")
}

group = "xyz.zzzoozo"
version = "1.0-SNAPSHOT"

application {
    mainClass = "xyz.zzzoozo.Main"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
