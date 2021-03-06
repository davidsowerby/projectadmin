package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.Language
import uk.q3c.build.creator.SourceLanguage
import uk.q3c.build.creator.TestFramework
import uk.q3c.build.creator.TestSet

/**
 * Created by David Sowerby on 06 Dec 2016
 */
class GradleGroovyBuilderTest extends AbstractBuilderTest {

    GradleGroovyBuilder groovyBuilder

    def setup() {
    }

    @Override
    def createBuilder() {
        groovyBuilder = new GradleGroovyBuilder()
        groovyBuilder.outputDir = temp
        builder = groovyBuilder
    }

    def "java() adds plugin and sourceCompatibility"() {
        given:
        expectedOutputFileName = 'java2.gradle'
        builder.configParam(new SourceLanguage(Language.JAVA, '1.7'))

        when:
        builder.execute()

        then:
        outputAsExpected()
    }

    def "java default is 1.8"() {
        given:
        expectedOutputFileName = 'java.gradle'
        builder.configParam(new SourceLanguage(Language.JAVA, ''))

        when:
        builder.execute()

        then:
        outputAsExpected()
    }


    def "kotlin source, buildscript and main body updated"() {
        given:
        expectedOutputFileName = 'kotlin.gradle'
        builder.configParam(new SourceLanguage(Language.KOTLIN, '1.0.6'))

        when:
        builder.execute()

        then:
        outputAsExpected()
    }


    def "kotlin default is 1.0.6"() {
        given:
        expectedOutputFileName = 'kotlin.gradle'
        builder.configParam(new SourceLanguage(Language.KOTLIN, ''))

        when:
        builder.execute()

        then:
        outputAsExpected()
    }

    def "groovy source, adds plugin"() {
        given:
        expectedOutputFileName = 'groovy.gradle'
        builder.configParam(new SourceLanguage(Language.GROOVY, '2.4'))

        when:
        builder.execute()

        then:
        outputAsExpected()
    }

    def "test set, Junit, creates test set, applies plugin and compile dependency"() {
        given:
        expectedOutputFileName = 'junit.gradle'
        builder.configParam(new TestSet("test", TestFramework.JUNIT, "4.12"))

        when:
        builder.execute()

        then:
        outputAsExpected()
    }

    def "latest versions"() {
        given:
        expectedOutputFileName = 'latestVersions.gradle'
        builder.configParam(new TestSet("test", TestFramework.JUNIT, ""))
        builder.configParam(new TestSet("test", TestFramework.SPOCK, ""))

        when:
        builder.execute()

        then:
        outputAsExpected()
    }

    def "test set, Spock, creates test set,  and compile dependencies"() {
        given:
        expectedOutputFileName = 'spock.gradle'
        builder.configParam(new TestSet("test", TestFramework.SPOCK, "1.0.1"))

        when:
        builder.execute()

        then:
        outputAsExpected()
    }

    def "mavenPublishing"() {
        given:
        expectedOutputFileName = 'publishing.gradle'
        builder.mavenPublishing()

        when:
        builder.execute()

        then:
        outputAsExpected()
    }

    def "buildscript with repos and dependencies"() {
        when:
        groovyBuilder.buildscript().dependencies().dependency('compile', 'dep 1')

        then:
        groovyBuilder.buildscript().dependencies().elements.size() == 1
        groovyBuilder.buildscript().dependencies().elements.contains(new Dependency('compile', 'dep 1'))
    }

    def "baseVersion"() {
        given:
        expectedOutputFileName = 'baseVersion.gradle'
        groovyBuilder.baseVersion('0.0.1.99')

        when:
        builder.execute()

        then:
        outputAsExpected()
    }

    def "wrapper"() {
        when:
        groovyBuilder.wrapper('3.2')

        then:
        groovyBuilder.elements.size() == 1
        Task task = groovyBuilder.elements.get(0)
        task.name == 'wrapper'
        task.type == 'Wrapper'
        task.dependsOn == ''
        task.plugin == ''
    }

    def "all aspects"() {
        given:
        expectedOutputFileName = 'expected.gradle'
        groovyBuilder.buildscript()
                .repositories('mavenLocal()').jcenter()
        groovyBuilder.buildscript().dependencies.compile('bsdep1')
        groovyBuilder.dependencies()
                .compile('dep1', 'dep2', 'dep3')
                .runtime('dep4', 'dep5')
                .dependency('smokeCompile', 'blah')
        groovyBuilder.plugins('java', 'groovy')
        groovyBuilder.repositories('mavenLocal()')
        groovyBuilder.dependencies.compile('depA', 'depB')
        groovyBuilder.line("group 'uk.q3c.simplycd'")
                .config('testSets').line('line 1', 'line 2')
        groovyBuilder.wrapper('2.10')
                .applyPlugin('wiggly')
                .applyFrom('wiggly.gradle')
                .task('hello', "", "", "")
        groovyBuilder.task('hello2', "", "", "").type('Test').dependsOn('otherTask')
        groovyBuilder.configParam(new SourceLanguage(Language.JAVA, '1.8'))
        groovyBuilder.baseVersion('0.0.0.3')

        when:
        groovyBuilder.execute()

        then:
        outputAsExpected()
    }

    def "full monty, from configParam calls"() {
        given:
        expectedOutputFileName = 'fullMonty.gradle'
        groovyBuilder.mavenPublishing()
        groovyBuilder.configParam(new SourceLanguage(Language.JAVA, ''))
        groovyBuilder.configParam(new SourceLanguage(Language.KOTLIN, ''))
        groovyBuilder.configParam(new TestSet('test', TestFramework.JUNIT, ''))
        groovyBuilder.configParam(new TestSet('test', TestFramework.SPOCK, ''))
        groovyBuilder.configParam(new TestSet('integrationTest', TestFramework.SPOCK, ''))
        groovyBuilder.baseVersion('0.0.0.3')


        when:
        builder.execute()

        then:
        outputAsExpected()

    }

}
