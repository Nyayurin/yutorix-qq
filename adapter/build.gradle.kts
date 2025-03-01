import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
	alias(libs.plugins.multiplatform)
	alias(libs.plugins.serialization)
	alias(libs.plugins.atomicfu)
	alias(libs.plugins.android.library)
}

kotlin {
	jvmToolchain(21)

	jvm()

	androidTarget {
		publishLibraryVariants("release", "debug")
	}

	js {
		browser {
			webpackTask {
				mainOutputFileName = "yutorix-qq-adapter.js"
			}
		}
		nodejs()
		binaries.library()
	}

	@OptIn(ExperimentalWasmDsl::class)
	wasmJs {
		browser {
			webpackTask {
				mainOutputFileName = "yutorix-qq-adapter.js"
			}
		}
		nodejs()
		binaries.library()
	}

	// Apple(IOS & MacOS)
	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64(),
		macosX64(),
		macosArm64()
	).forEach {
		it.binaries.framework {
			baseName = "yutorix-qq-adapter"
			isStatic = true
		}
	}

	// Linux
	listOf(
		linuxX64(),
		linuxArm64()
	).forEach {
		it.binaries.staticLib {
			baseName = "yutorix-qq-adapter"
		}
	}

	// Windows
	mingwX64 {
		binaries.staticLib {
			baseName = "yutorix-qq-adapter"
		}
	}

	sourceSets {
		commonMain.dependencies {
			implementation(libs.yutori)
			implementation(project(":yutorix-qq-core"))
			api(libs.ktor.client.core)
			api(libs.ktor.client.content.negotiation)
		}
	}
}

android {
	namespace = "cn.yurn.yutorix.qq.adapter"
	compileSdk = 34

	defaultConfig {
		minSdk = 24
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_21
		sourceCompatibility = JavaVersion.VERSION_21
	}
}

publishing {
	publications.withType<MavenPublication> {
		pom {
			name = "Yutorix-QQ-Adapter"
			version = System.getenv("VERSION")
			description = "Kotlin Multiplatform library"
			url = "https://github.com/Nyayurn/yutorix-qq"

			developers {
				developer {
					id = "Nyayurn"
					name = "Yurn"
					email = "Nyayurn@outlook.com"
				}
			}
			scm {
				url = "https://github.com/Nyayurn/yutorix-qq"
			}
		}
	}
}