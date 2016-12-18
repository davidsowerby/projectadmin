package uk.q3c.build.creator.gradle
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
        expectedOutputFileName = 'buildscript-kotlin.gradle'


        when:
        tester.dependenciesOnly(builder)
        tester.repositoriesOnly(builder)
        builder.execute()


        then:
        outputAsExpected()

    }
}
