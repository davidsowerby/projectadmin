package uk.q3c.build.creator.dir

import uk.q3c.build.creator.Language
import uk.q3c.build.creator.SourceLanguage
import uk.q3c.build.creator.TestFramework
import uk.q3c.build.creator.TestSet
import uk.q3c.build.creator.gradle.AbstractBuilderTest

/**
 * Created by David Sowerby on 11 Dec 2016
 */
class DirectoryBuilderTest extends AbstractBuilderTest {


    def setup() {
        projectCreator.basePackage >> "example.com"
    }

    @Override
    def createBuilder() {
        builder = new DirectoryBuilder()
        builder.projectCreator = projectCreator
    }

    def "All languages"() {
        given:
        File javaDir = new File(temp, "src/main/java")
        File kotlinDir = new File(temp, "src/main/kotlin")
        File groovyDir = new File(temp, "src/main/groovy")
        File resourcesDir = new File(temp, "src/main/resources")

        String bpDir = "example/com"
        File javaDirBp = new File(javaDir, bpDir)
        File groovyDirBp = new File(groovyDir, bpDir)
        File kotlinDirBp = new File(kotlinDir, bpDir)
        File resourcesBp = new File(resourcesDir, bpDir)

        File javaDummyFile = new File(javaDirBp, "dummyFileJava.txt")
        File groovyDummyFile = new File(groovyDirBp, "dummyFileGroovy.txt")
        File kotlinDummyFile = new File(kotlinDirBp, "dummyFileKotlin.txt")
        File resourcesDummyFile = new File(resourcesBp, "dummyResources.txt")

        builder.configParam(new SourceLanguage(Language.JAVA, "1.6"))
        builder.configParam(new SourceLanguage(Language.KOTLIN, "1.0.3"))
        builder.configParam(new SourceLanguage(Language.GROOVY, "2.6"))

        when:
        builder.execute()

        then:
        javaDirBp.exists()
        javaDirBp.isDirectory()
        javaDummyFile.exists()

        kotlinDirBp.exists()
        kotlinDirBp.isDirectory()
        kotlinDummyFile.exists()

        groovyDirBp.exists()
        groovyDirBp.isDirectory()
        groovyDummyFile.exists()

        resourcesBp.exists()
        resourcesBp.isDirectory()
        resourcesDummyFile.exists()

    }

    def "test sets"() {
        given:
        File testDirJunit = new File(temp, "src/test/java/example/com")
        File testDirSpock = new File(temp, "src/test/groovy/example/com")
        File smokeTestDirSpock = new File(temp, "src/smoketest/groovy/example/com")

        builder.configParam(new TestSet("test", TestFramework.JUNIT, "4.12"))
        builder.configParam(new TestSet("test", TestFramework.SPOCK, "1.12"))
        builder.configParam(new TestSet("smoketest", TestFramework.SPOCK, "1.1"))

        when:

        builder.execute()


        then:
        testDirJunit.exists()
        testDirJunit.isDirectory()

        testDirSpock.exists()
        testDirSpock.isDirectory()

        smokeTestDirSpock.exists()
        smokeTestDirSpock.isDirectory()
    }


}
