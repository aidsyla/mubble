import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.spotless) apply false
    id("com.google.devtools.ksp") version "2.3.6" apply false
    id("com.google.dagger.hilt.android") version "2.59.2" apply false
}

val ktlintVersion = libs.versions.ktlint.get()

subprojects {
    apply<SpotlessPlugin>()
    configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            targetExclude("build/**/*.kt")
            ktlint(ktlintVersion)
                .editorConfigOverride(
                    mapOf(
                        "ktlint_function_naming_ignore_when_annotated_with" to "Composable, Test",
                        "ktlint_standard_backing-property-naming" to "disabled",

                        "ij_kotlin_allow_trailing_comma" to "false",
                        "ij_kotlin_allow_trailing_comma_on_call_site" to "false",

                        "ktlint_standard_binary-expression-wrapping" to "disabled",
                        "ktlint_standard_chain-method-continuation" to "disabled",
                        "ktlint_standard_class-signature" to "disabled",
                        "ktlint_standard_condition-wrapping" to "disabled",
                        "ktlint_standard_function-signature" to "disabled",

                        "ktlint_standard_function-expression-body" to "disabled",
                        "ktlint_standard_function-literal" to "disabled",
                        "ktlint_standard_multiline-loop" to "disabled",
                        "ktlint_standard_function-type-modifier-spacing" to "disabled",
                    )
                )
        }
        kotlinGradle {
            target("*.kts")
            ktlint(ktlintVersion)
                .editorConfigOverride(
                    mapOf(
                        "ktlint_function_naming_ignore_when_annotated_with" to "Composable, Test",
                        "ktlint_standard_backing-property-naming" to "disabled",

                        "ij_kotlin_allow_trailing_comma" to "false",
                        "ij_kotlin_allow_trailing_comma_on_call_site" to "false",

                        "ktlint_standard_binary-expression-wrapping" to "disabled",
                        "ktlint_standard_chain-method-continuation" to "disabled",
                        "ktlint_standard_class-signature" to "disabled",
                        "ktlint_standard_condition-wrapping" to "disabled",
                        "ktlint_standard_function-signature" to "disabled",

                        "ktlint_standard_function-expression-body" to "disabled",
                        "ktlint_standard_function-literal" to "disabled",
                        "ktlint_standard_multiline-loop" to "disabled",
                        "ktlint_standard_function-type-modifier-spacing" to "disabled",
                    )
                )
        }
    }
}
