package uk.q3c.build.creator.gradle
/**
 * Created by David Sowerby on 07 Oct 2016
 */
class BuildscriptTest extends AbstractBuilderTest {


    Buildscript buildscript

    def setup() {
        buildscript = new Buildscript()
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

}
