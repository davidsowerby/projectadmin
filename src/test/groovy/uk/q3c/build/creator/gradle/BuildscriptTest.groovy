package uk.q3c.build.creator.gradle

import spock.lang.Specification
import uk.q3c.build.creator.KotlinObjectFactory
import uk.q3c.build.creator.gradle.buffer.FileBuffer

/**
 * Created by David Sowerby on 07 Oct 2016
 */
class BuildscriptTest extends Specification {

    Buildscript buildscript
    FileBuffer fileBuffer

    def setup() {
        fileBuffer = KotlinObjectFactory.fileBuffer()
        buildscript = new Buildscript()
    }

    def "repositories has primary sub-elements added at startup"() {

        expect:
        buildscript.elements.size() == 2
        buildscript.elements.contains(buildscript.repositories)
        buildscript.elements.contains(buildscript.dependencies)

    }

    def "prints nothing when empty"() {
        when:
        buildscript.write()

        then:
        fileBuffer.output().isEmpty()
    }
}
