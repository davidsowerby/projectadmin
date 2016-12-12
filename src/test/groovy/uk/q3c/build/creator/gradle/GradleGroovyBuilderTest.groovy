package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.Language
import uk.q3c.build.creator.SourceLanguage
import uk.q3c.build.creator.gradle.element.BaseVersionElement
import uk.q3c.build.creator.gradle.element.BasicScriptElement
import uk.q3c.build.creator.gradle.element.PluginElement

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
        builder = groovyBuilder
    }

    def "java() adds plugin and sourceCompatibility"() {
        given:
        builder.configParam(new SourceLanguage(Language.JAVA, '1.7'))

        when:
        builder.execute()

        then:
        println fileBuffer.output()
        groovyBuilder.plugins.elements.size() == 1
        groovyBuilder.plugins.elements.contains(new PluginElement('java'))
        groovyBuilder.elements.size() == 1
        groovyBuilder.elements.contains(new BasicScriptElement("sourceCompatibility = '1.7'"))
    }

    def "kotlin source, buildscript and main body updated"() {
        given:
        expectedOutputFileName = 'kotlin.gradle'
        builder.configParam(new SourceLanguage(Language.KOTLIN, '1.0.4'))

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

    def "buildscript with repos and dependencies"() {
        when:
        groovyBuilder.buildscript().dependencies().dependency('compile', 'dep 1')

        then:
        groovyBuilder.buildscript().dependencies().elements.size() == 1
        groovyBuilder.buildscript().dependencies().elements.contains(new Dependency('compile', 'dep 1'))
    }

    def "baseVersion"() {
        when:
        groovyBuilder.baseVersion('1.9.9')

        then:
        groovyBuilder.elements.size() == 1
        groovyBuilder.elements.contains(new BaseVersionElement("1.9.9"))
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
        groovyBuilder.javaSource('1.8')

        when:
        groovyBuilder.execute()

        then:
        outputAsExpected()
    }


}
