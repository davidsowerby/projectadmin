package uk.q3c.build.creator.gradle

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import uk.q3c.build.creator.KotlinObjectFactory
import uk.q3c.build.creator.ProjectCreator
import uk.q3c.build.creator.gradle.buffer.FileBuffer
import uk.q3c.build.creator.gradle.element.BaseVersionElement
import uk.q3c.build.creator.gradle.element.BasicScriptElement
import uk.q3c.build.creator.gradle.element.PluginElement
import uk.q3c.util.testutil.FileTestUtil
import uk.q3c.util.testutil.TestResource

/**
 * Created by David Sowerby on 06 Dec 2016
 */
class GradleGroovyBuilderTest extends Specification {

    @Rule
    TemporaryFolder temporaryFolder
    File temp
    File outputFile
    GradleGroovyBuilder builder
    FileBuffer fileBuffer
    ProjectCreator mockProjectCreator = Mock(ProjectCreator)

    def setup() {
        temp = temporaryFolder.getRoot()
        mockProjectCreator.projectDir >> temp
        outputFile = new File(temp, 'build.gradle')
        builder = new GradleGroovyBuilder()
        builder.setProjectCreator(mockProjectCreator)
        fileBuffer = KotlinObjectFactory.fileBuffer()
        fileBuffer.reset()
    }

    def "java() adds plugin and sourceCompatibility"() {
        when:
        builder.javaSource('1.7')
        builder.execute()

        then:
        println fileBuffer.output()
        builder.plugins.elements.size() == 1
        builder.plugins.elements.contains(new PluginElement('java'))
        builder.elements.size() == 1
        builder.elements.contains(new BasicScriptElement("sourceCompatibility = '1.7'"))
    }

    def "buildscript with repos and dependencies"() {
        when:
        builder.buildscript().dependencies().dependency('compile', 'dep 1')

        then:
        builder.buildscript().dependencies().elements.size() == 1
        builder.buildscript().dependencies().elements.contains(new Dependency('compile', 'dep 1'))
    }

    def "baseVersion"() {
        when:
        builder.baseVersion('1.9.9')

        then:
        builder.elements.size() == 1
        builder.elements.contains(new BaseVersionElement("1.9.9"))
    }

    def "wrapper"() {
        when:
        builder.wrapper('3.2')

        then:
        builder.elements.size() == 1
        Task task = builder.elements.get(0)
        task.name == 'wrapper'
        task.type == 'Wrapper'
        task.dependsOn == ''
        task.plugin == ''
    }

    def "all aspects"() {
        given:
        File expectedOutput = TestResource.resource(this, 'expected.gradle')
        builder.buildscript()
                .repositories('mavenLocal()').jcenter()
        builder.buildscript().dependencies.compile('bsdep1')
        builder.dependencies()
                .compile('dep1', 'dep2', 'dep3')
                .runtime('dep4', 'dep5')
                .dependency('smokeCompile', 'blah')
        builder.plugins('java', 'groovy')
        builder.repositories('mavenLocal()')
        builder.dependencies.compile('depA', 'depB')
        builder.line("group 'uk.q3c.simplycd'")
                .config('testSets').line('line 1', 'line 2')
        builder.wrapper('2.10')
                .applyPlugin('wiggly')
                .applyFrom('wiggly.gradle')
                .task('hello', "", "", "")
        builder.task('hello2', "", "", "").type('Test').dependsOn('otherTask')
        builder.javaSource('1.8')
                .execute()

        expect:
        outputFile.exists()
        Optional<String> diffs = FileTestUtil.compare(outputFile, expectedOutput)
        if (diffs.isPresent()) println diffs.get()
        !diffs.isPresent()
    }

}
