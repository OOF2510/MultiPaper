plugins {
    java
    `maven-publish`
    id("com.gradleup.shadow") version "9.2.2"
    id("io.papermc.paperweight.patcher") version "2.0.0-beta.24"
}

paperweight {
    upstreams.register("purpur") {
        repo = github("PurpurMC", "Purpur")
        ref = providers.gradleProperty("purpurRef")

        patchFile {
            path = "purpur-api/build.gradle.kts"
            outputFile = file("MultiPaper-API/build.gradle.kts")
            patchFile = file("MultiPaper-API/build.gradle.kts.patch")
        }
        patchFile {
            path = "purpur-server/build.gradle.kts"
            outputFile = file("MultiPaper-Server/build.gradle.kts")
            patchFile = file("MultiPaper-Server/build.gradle.kts.patch")
        }

        patchDir("multipaperApi") {
            upstreamPath = "purpur-api"
            excludes = listOf("build.gradle.kts", "build.gradle.kts.patch", "paper-patches")
            patchesDir = file("patches/api")
            outputDir = file("MultiPaper-API")
        }
        patchDir("multipaperServer") {
            upstreamPath = "purpur-server"
            excludes = listOf("build.gradle.kts", "build.gradle.kts.patch", "paper-patches", "minecraft-patches")
            patchesDir = file("patches/server")
            outputDir = file("MultiPaper-Server")
        }
    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = Charsets.UTF_8.name()
        options.release = 25
    }
    tasks.withType<Javadoc>().configureEach {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources>().configureEach {
        filteringCharset = Charsets.UTF_8.name()
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://ci.emc.gs/nexus/content/groups/aikar/")
        maven("https://repo.aikar.co/content/groups/aikar")
        maven("https://repo.md-5.net/content/repositories/releases/")
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
        maven("https://jitpack.io")
    }
}

tasks.register("printMinecraftVersion") {
    doLast {
        println(providers.gradleProperty("mcVersion").get().trim())
    }
}
