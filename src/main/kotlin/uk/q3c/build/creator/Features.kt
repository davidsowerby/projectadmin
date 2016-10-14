package uk.q3c.build.creator

/**
 * Created by David Sowerby on 11 Oct 2016
 */
interface Features<P> {
    fun source(language: Language, version: String): P
    /**
     * Adds a test set (for example 'integrationTest') which allows builder to create appropriate directories and dependencies.
     * If you use multiple test frameworks, call this method multiple times
     *
     * @param setName the name of the test set
     * @param testFramework which test framework to use.  This will enable identification of the appropriate dependencies
     * @param version the version of [testFramework] to use.  An empty String will cause the latest released version to be used
     */
    fun testSet(setName: String, testFramework: TestFramework, version: String = ""): P

}

enum class Language {
    JAVA, KOTLIN, GROOVY
}

enum class TestFramework {
    JUNIT, SPOCK
}
