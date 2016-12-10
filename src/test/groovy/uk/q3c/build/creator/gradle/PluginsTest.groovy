package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.KotlinObjectFactory

/**
 * Created by David Sowerby on 04 Sep 2016
 */
class PluginsTest extends BlockReaderSpecification {

    Plugins plugins


    def setup() {
        plugins = new Plugins()
        KotlinObjectFactory.fileBuffer().reset()
    }

    def "output"() {

        when:
        plugins.java().groovy().maven().mavenPublish().plugins('wiggly1', 'wiggly2').write()

        then:
        List<String> result = resultLines()
        result.get(0) == 'plugins {'
        result.get(1) == "    id 'java'"
        result.get(2) == "    id 'groovy'"
        result.get(3) == "    id 'maven'"
        result.get(4) == "    id 'maven-publish'"
        result.get(5) == "    id 'wiggly1'"
        result.get(6) == "    id 'wiggly2'"
        result.get(7) == '}'

    }

    def "IDEs"() {
        when:
        plugins.eclipse().idea().write()

        then:
        List<String> result = resultLines()
        result.get(0) == 'plugins {'
        result.get(1) == "    id 'eclipse-wtp'"
        result.get(2) == "    id 'idea'"
        result.get(3) == "}"

    }
}
