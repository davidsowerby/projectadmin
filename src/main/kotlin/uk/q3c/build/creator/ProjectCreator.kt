package uk.q3c.build.creator

import com.google.common.collect.ImmutableList
import java.io.File

/**
 * Created by David Sowerby on 10 Oct 2016
 */
interface ProjectCreator : ProjectConfiguration{

    fun buildersCount(): Int
}


interface ProjectConfiguration {
    var basePackage: String
    var projectDir : File
    var useMavenPublishing : Boolean

    fun projectDir(dir: File) : ProjectConfiguration
    fun basePackage(basePackage: String) : ProjectConfiguration

    fun getSteps(): ImmutableList<ConfigStep>
    fun mavenPublishing(value: Boolean): ProjectConfiguration

    fun source(language: Language, version: String): ProjectConfiguration
    /**
     * Adds a test set (for example 'integrationTest') which allows builder to create appropriate directories and dependencies.
     * If you use multiple test frameworks, call this method multiple times
     *
     * @param setName the name of the test set
     * @param testFramework which test framework to use.  This will enable identification of the appropriate dependencies
     * @param version the version of [testFramework] to use.  An empty String will cause the latest released version to be used
     */
    fun testSet(setName: String, testFramework: TestFramework, version: String = ""): ProjectConfiguration
}

enum class Language {
    JAVA, KOTLIN, GROOVY
}

enum class TestFramework {
    JUNIT, SPOCK
}