package uk.q3c.build.creator.gradle

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.specs.BehaviorSpec
import uk.q3c.util.testutil.FileTestUtil
import uk.q3c.util.testutil.TestResource
import java.io.File

/**
 * Created by David Sowerby on 24 Jul 2017
 */
class BScriptTest : BehaviorSpec() {

    init {
        Given("a new Buildscript") {
            val buildscript = Buildscript()

            When("nothing changed") {


                Then("it contains empty blocks for repositories and dependencies") {
                    buildscript.elements.size shouldEqual 2
                    buildscript.elements.contains(buildscript.repositories) shouldEqual true
                    buildscript.elements.contains(buildscript.dependencies) shouldEqual true
                }
            }
        }
    }

    init {
        Given("a kotlin script") {
            val tester = BuildscriptTester()
            val builder = GradleGroovyBuilder()
            val temp = createTempDir()
            builder.outputDir = temp

            When("Gradle file built by script") {
                tester.dependenciesOnly(builder)
                tester.repositoriesOnly(builder)
                builder.execute()

                Then("Output should be the same as buildscript-kotlin.gradle") {
                    val outputFile = File(temp, "build.gradle")
                    val outputDiffs = FileTestUtil.compare(outputFile, TestResource.resource(builder, "buildscript-kotlin.gradle"))
                    if (outputDiffs.isPresent) {
                        println(outputDiffs.get())
                    }
                    outputDiffs.isPresent shouldBe false
                }
            }

        }
    }

}