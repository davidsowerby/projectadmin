package uk.q3c.build.creator

import com.google.inject.Guice
import com.google.inject.Injector
import spock.lang.Specification

/**
 * Created by David Sowerby on 10 Oct 2016
 */
class DefaultProjectCreatorTest extends Specification {


    def "module configuration"() {
        given:
        Injector injector = Guice.createInjector(new ProjectCreatorModule())


        when:
        ProjectCreator creator = injector.getInstance(ProjectCreator)
        FeatureBuilders bc = injector.getInstance(FeatureBuilders)

        then:
        creator instanceof DefaultProjectCreator
        bc.builderCount() == 1
    }

    def "java source adds directories, gradle plugin and sourceCompatibility"() {
        expect: false
    }

}
