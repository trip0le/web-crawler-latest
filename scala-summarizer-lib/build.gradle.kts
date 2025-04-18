plugins {
    scala
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.scala-lang:scala-library:2.13.12")
    implementation("org.scala-lang.modules:scala-java8-compat_2.13:1.0.2")
    implementation("org.postgresql:postgresql:42.7.3")

    // Akka HTTP dependencies
    implementation("com.typesafe.akka:akka-actor_2.13:2.6.21")
    implementation("com.typesafe.akka:akka-stream_2.13:2.6.21")
    implementation("com.typesafe.akka:akka-http_2.13:10.2.10")
}

scala {
    zincVersion.set("1.9.5")
}

application {
    mainClass.set("com.example.summarizer.Main") // Correct the entry point
}

sourceSets {
    main {
        scala {
            setSrcDirs(listOf("src/main/scala"))
        }
        resources {
            setSrcDirs(listOf("src/main/resources"))
        }
    }
}

// Ensure the fatJar task is working and packaging all dependencies
tasks.register<Jar>("fatJar") {
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    manifest {
        attributes["Main-Class"] = "com.example.summarizer.Main" // Correct the entry point
    }

    // Collect compiled classes and resources
    from(sourceSets.main.get().output)

    // Include all runtime dependencies into the fat jar
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

// Set the default task to build fatJar and clean
defaultTasks("clean", "fatJar")
