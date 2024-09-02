import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
	java
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.openapi.generator") version "7.8.0"
	kotlin("jvm")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
val openapiGenOutputDir = "${layout.buildDirectory.asFile.get().path}/generated-sources/openapi-generated"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

sourceSets {
	main {
		java {
			srcDir("$openapiGenOutputDir/src/main/java")
		}
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation(kotlin("stdlib-jdk8"))

	// annotations and other dependencies for openapigenerator
	implementation("io.swagger.core.v3:swagger-annotations:2.2.22")
	implementation("io.swagger.core.v3:swagger-core:2.2.22")
	implementation("jakarta.validation:jakarta.validation-api:2.0.1")
	implementation("javax.servlet:javax.servlet-api:3.0.1")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("javax.annotation:javax.annotation-api:1.3.2")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")

	// jwt dependencies
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	runtimeOnly("org.postgresql:postgresql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

openApiGenerate {
	inputSpec = "$rootDir/openapi.yaml"
	outputDir = openapiGenOutputDir
	generatorName = "spring"
	generateApiTests = false
	generateModelTests = false
	generateApiDocumentation = false
	generateModelDocumentation = false

	configOptions = mutableMapOf(
		"interfaceOnly" to "true",
		"skipDefaultInterface" to "true",
	)
}

springBoot {
	mainClass = "com.example.MySpringApplication"
}

listOf(KotlinCompile::class, JavaCompile::class, BootRun::class).forEach{
	tasks.withType(it) {
		dependsOn("openApiGenerate")
	}
}


tasks.withType<Test> {
	useJUnitPlatform()
}
