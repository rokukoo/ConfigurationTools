plugins {
    id("java")
}

group = "cn.neptunex"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        name = "spigotmc-repo"
        setUrl("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        setUrl("https://maven.elmakers.com/repository/")
    }
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.4")
    annotationProcessor("org.projectlombok:lombok:1.18.4")
    testCompileOnly("org.projectlombok:lombok:1.18.4")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.4")

    implementation("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    implementation("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}