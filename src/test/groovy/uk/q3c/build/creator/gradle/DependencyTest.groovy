package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.KotlinObjectFactory

/**
 * Created by David Sowerby on 06 Dec 2016
 */
class DependencyTest extends BlockReaderSpecification {

    Dependency dependency

    final String krail = 'uk.q3c.krail:krail:0.9.9'
    final String testUtil = 'uk.q3c:q3c-testUtil:0.8.2'
    final String logback = 'logback-classic'
    final String gradleApi = 'gradleApi()'

    def setup() {
        KotlinObjectFactory.fileBuffer().reset()
    }

    def 'single line'() {
        given:
        dependency = new Dependency('compile', krail)

        when:
        dependency.write()

        then:
        resultLines().get(0) == "compile 'uk.q3c.krail:krail:0.9.9'"
        resultLines().get(1) == "" // new line
        resultLines().size() == 2
    }

    def "with exclude"() {
        given:
        String expected1 = "testCompile('uk.q3c:q3c-testUtil:0.8.2') {"
        String expected2 = "    exclude module: 'logback-classic'"
        String expected3 = '}'
        dependency = new Dependency('testCompile', testUtil).excludeModule(logback)

        when:
        dependency.write()

        then:
        List<String> result = resultLines()
        result.get(0) == expected1
        result.get(1) == expected2
        result.get(2) == expected3
        result.get(3) == ""
        result.size() == 4
    }

    def "in-built"() {
        given:
        dependency = new Dependency('compile', gradleApi)

        when:
        dependency.write()

        then:
        resultLines().get(0) == "compile gradleApi()"
        resultLines().get(1) == "" // new line
        resultLines().size() == 2
    }

    def "in-built with exclude"() {
        given:
        String expected1 = "testCompile(gradleApi()) {"
        String expected2 = "    exclude module: 'logback-classic'"
        String expected3 = '}'

        dependency = new Dependency('testCompile', 'gradleApi()').excludeModule(logback)

        when:
        dependency.write()

        then:
        List<String> result = resultLines()
        result.get(0) == (expected1)
        result.get(1) == (expected2)
        result.get(2) == (expected3)
        result.get(3) == ""
        result.size() == 4
    }

}