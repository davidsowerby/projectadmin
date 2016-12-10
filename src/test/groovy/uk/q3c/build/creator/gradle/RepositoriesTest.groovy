package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.KotlinObjectFactory

/**
 * Created by David Sowerby on 04 Sep 2016
 */
class RepositoriesTest extends BlockReaderSpecification {


    Repositories repos


    def setup() {
        KotlinObjectFactory.fileBuffer().reset()
        repos = new Repositories()
    }

    def "repositories and shortcuts"() {
        given:
        repos.repositories('a', 'b').jcenter().mavenCentral().mavenLocal()

        when:
        repos.write()

        then:
        List<String> results = resultLines()
        results.get(0) == 'repositories {'
        results.get(1) == '    a'
        results.get(2) == '    b'
        results.get(3) == '    jcenter()'
        results.get(4) == '    mavenCentral()'
        results.get(5) == '    mavenLocal()'
        results.get(6) == '}'
    }


}
