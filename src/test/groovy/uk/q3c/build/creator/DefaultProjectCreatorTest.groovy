package uk.q3c.build.creator

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import com.google.inject.Guice
import com.google.inject.Injector
import org.jetbrains.annotations.NotNull
import spock.lang.Specification

/**
 * Created by David Sowerby on 10 Oct 2016
 */
class DefaultProjectCreatorTest extends Specification {

    MockBuilder builder1 = new MockBuilder()
    MockBuilder builder2 = new MockBuilder()
    ProjectConfiguration configuration = Mock(ProjectConfiguration)
    ProjectCreator creator


    def "module configuration"() {
        given:
        Injector injector = Guice.createInjector(new ProjectCreatorModule())


        when:
        creator = injector.getInstance(ProjectCreator)

        then:
        creator instanceof DefaultProjectCreator
        creator.buildersCount() == 2
    }

    def "setProjectCreator() is called on all builders"() {
        given:
        Set<Builder> builders = ImmutableSet.of(builder1, builder2)

        when:
        creator = new DefaultProjectCreator(builders, configuration)

        then:
        builder1.creator == creator
        builder2.creator == creator
    }

    def "execute calls each builder with each step, and mavenPublishing if true"() {
        given:
        SourceLanguage sourceLanguage1 = new SourceLanguage(Language.JAVA, "1.8")
        SourceLanguage sourceLanguage2 = new SourceLanguage(Language.KOTLIN, "1.0.4")
        TestSet testSet1 = new TestSet("integrationTest", TestFramework.SPOCK, "groovy2.4")
        TestSet testSet2 = new TestSet("integrationTest", TestFramework.JUNIT, "4.10")
        Set<Builder> builders = ImmutableSet.of(builder1, builder2)
        List<ConfigStep> steps = ImmutableList.of(sourceLanguage1, testSet2, sourceLanguage2, testSet1)
        configuration.getSteps() >> steps
        configuration.useMavenPublishing >> true
        creator = new DefaultProjectCreator(builders, configuration)

        when:
        creator.execute()

        then:
        builder1.mavenPublishingCalled
        builder2.mavenPublishingCalled
        builder1.sourceLangugageCalled == 2
        builder2.sourceLangugageCalled == 2
        builder1.testSetCalled == 2
        builder2.testSetCalled == 2
        builder1.sourceLanguages.containsAll(sourceLanguage1, sourceLanguage2)
        builder2.sourceLanguages.containsAll(sourceLanguage1, sourceLanguage2)
        builder1.testSets.containsAll(testSet1, testSet2)
        builder2.testSets.containsAll(testSet1, testSet2)
        builder1.testSets.get(0) == testSet2
        builder2.testSets.get(0) == testSet2
        builder1.sourceLanguages.get(0) == sourceLanguage1
        builder2.sourceLanguages.get(0) == sourceLanguage1
    }

    def "delegation"() {

        given:
        Set<Builder> builders = ImmutableSet.of(builder1, builder2)
        creator = new DefaultProjectCreator(builders, configuration)
        File projectDir = new File("/home/temp")

        when:
        creator.setBasePackage("x")
        creator.setProjectDir(projectDir)

        then:
        1 * configuration.setProjectDir(projectDir)
        1 * configuration.setBasePackage("x")
    }


    class MockBuilder implements Builder {


        ProjectCreator creator
        int sourceLangugageCalled
        int testSetCalled
        boolean mavenPublishingCalled
        List<SourceLanguage> sourceLanguages = new ArrayList<>()
        List<TestSet> testSets = new ArrayList<>()

        @Override
        void setProjectCreator(@NotNull ProjectCreator creator) {
            this.creator = creator
        }

        @Override
        void write() {

        }

        @Override
        void set(@NotNull SourceLanguage step) {
            sourceLangugageCalled++
            sourceLanguages.add(step)
        }

        @Override
        void set(@NotNull TestSet step) {
            testSetCalled++
            testSets.add(step)
        }

        @Override
        void mavenPublishing() {
            mavenPublishingCalled = true
        }
    }
}
