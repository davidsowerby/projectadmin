package uk.q3c.build.creator.gradle

import uk.q3c.util.testutil.FileTestUtil
import uk.q3c.util.testutil.TestResource
/**
 * Created by David Sowerby on 07 Oct 2016
 */
class BuildscriptTest extends AbstractBuilderTest {


    Buildscript buildscript

    def setup() {
        buildscript = new Buildscript()
    }

    def "repositories has primary sub-elements added at startup"() {

        expect:
        buildscript.elements.size() == 2
        buildscript.elements.contains(buildscript.repositories)
        buildscript.elements.contains(buildscript.dependencies)

    }

    def "prints nothing when empty, only relevant sub-section if not empty"() {
        when:
        buildscript.write()

        then:
        fileBuffer.output().isEmpty()

        when:
        buildscript.repositories().mavenLocal()
        buildscript.write()

        then:
        !fileBuffer.output().isEmpty()
    }

    def "dependencies and repositories, kotlin API"() {
        given:
        BuildscriptTester tester = new BuildscriptTester()
        File expectedOutput = TestResource.resource(this, 'buildscript-kotlin.gradle')

        when:
        tester.dependenciesOnly(builder)
        tester.repositoriesOnly(builder)
        builder.execute()


        then:
        Optional<String> diffs = FileTestUtil.compare(builder.outputFile(), expectedOutput)
        if (diffs.isPresent()) println diffs.get()
        !diffs.isPresent()

    }
}
