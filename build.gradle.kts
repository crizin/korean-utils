plugins {
	id("java")
	id("org.sonarqube") version "4.4.1.3373"
}

group = "io.github.crizin"
version = "1.0.0"

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.10.2"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.assertj:assertj-core:3.26.0")
}

sonar {
	properties {
		property("sonar.projectKey", "crizin_korean-utils")
		property("sonar.organization", "crizin")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-Xlint:-options")
}

tasks.test {
	useJUnitPlatform()
}
