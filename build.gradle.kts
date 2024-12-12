import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SonatypeHost

plugins {
	id("java-library")
	id("jacoco")
	id("com.vanniktech.maven.publish") version "0.29.0"
}

group = "io.github.crizin"
version = "0.0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.10.3"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.assertj:assertj-core:3.26.0")
}

jacoco {
	toolVersion = "0.8.12"
}

mavenPublishing {
	signAllPublications()
	publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

	coordinates(
		groupId = "io.github.crizin",
		artifactId = "korean-utils",
		version = "0.0.1",
	)

	configure(
		JavaLibrary(
			javadocJar = JavadocJar.Javadoc(),
			sourcesJar = true,
		)
	)

	pom {
		name.set("Korean Utils")
		description.set("A Java library that provides various utility functions for processing and manipulating Korean text")
		inceptionYear.set("2024")
		url.set("https://github.com/crizin/korean-utils")

		licenses {
			license {
				name.set("MIT License")
				url.set("https://opensource.org/licenses/MIT")
			}
		}

		developers {
			developer {
				id.set("crizin")
				name.set("JaeYong Lee")
				url.set("https://github.com/crizin")
			}
		}

		scm {
			url.set("https://github.com/crizin/korean-utils")
			connection.set("scm:git:git://github.com/crizin/korean-utils.git")
			developerConnection.set("scm:git:ssh://git@github.com/crizin/korean-utils.git")
		}
	}
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-Xlint:-options")
}

tasks.test {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
	}
}
