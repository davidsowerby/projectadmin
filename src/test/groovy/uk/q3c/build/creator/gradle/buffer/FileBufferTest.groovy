package uk.q3c.build.creator.gradle.buffer

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import uk.q3c.build.creator.KotlinObjectFactory

/**
 * Created by David Sowerby on 31 Aug 2016
 */
class FileBufferTest extends Specification {

    @Rule
    TemporaryFolder temporaryFolder
    File temp

    FileBuffer buffer

    def setup() {
        buffer = KotlinObjectFactory.fileBuffer()
        buffer.reset()
        temp = temporaryFolder.getRoot()
    }

    def "append"() {
        when:
        buffer.append('a', 'b', 'c')

        then:
        buffer.output() == 'abc'
    }

    def "append with indent at 1"() {
        given:
        buffer.incrementIndent()

        when:
        buffer.append('a', 'b', 'c')

        then:
        buffer.output() == '    abc'
    }

    def "append with indent at 2"() {
        given:
        buffer.incrementIndent()
        buffer.incrementIndent()

        when:
        buffer.append('a', 'b', 'c')

        then:
        buffer.output() == '        abc'
    }

    def "appendLine"() {
        given:
        buffer.incrementIndent()

        when:
        buffer.appendLine('a', 'b', 'c')

        then:
        buffer.output() == '    abc\n'
    }


    def "inc and dec indent"() {
        when: "incremented"
        buffer.incrementIndent()

        then:
        buffer.getCurrentIndent() == 1

        when: "decremented"
        buffer.decrementIndent()

        then:
        buffer.getCurrentIndent() == 0
    }

    def "output"() {
        given:
        buffer.incrementIndent()
        String inputString = "a line in the sand"
        buffer.append(inputString)
        File file = new File(temp, 'build.gradle')

        when:
        buffer.writeToFile(file)

        then:
        file.exists()
        String s = FileUtils.readFileToString(file)
        s == '    ' + inputString

    }

}
