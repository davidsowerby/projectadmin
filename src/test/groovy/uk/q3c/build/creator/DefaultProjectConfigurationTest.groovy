package uk.q3c.build.creator

import spock.lang.Specification
import uk.q3c.build.gitplus.gitplus.GitPlus
import uk.q3c.build.gitplus.local.GitLocal
import uk.q3c.build.gitplus.remote.GitRemote
/**
 * Created by David Sowerby on 13 Dec 2016
 */
class DefaultProjectConfigurationTest extends Specification {

    ProjectConfiguration configuration
    GitPlus gitPlus = Mock(GitPlus)
    GitRemote gitRemote = Mock(GitRemote)
    GitLocal gitLocal = Mock(GitLocal)

    def setup() {
        gitPlus.local >> gitLocal
        gitPlus.remote >> gitRemote
        configuration = new DefaultProjectConfiguration(gitPlus)
    }

    def "fluent set get"() {
        given:
        File projectDir = new File(".")
        String basePackage = "example.com"
        Map<String, String> issueLabels = new HashMap<>()
        String user = "someone"


        when:
        configuration.projectDir(projectDir)
        configuration.basePackage(basePackage)
        configuration.mavenPublishing(true)
        configuration.mergeIssueLabels(true)
        configuration.issueLabels(issueLabels)
        configuration.remoteRepoUser(user)

        then:
        configuration.projectDir == projectDir
        configuration.basePackage == basePackage
        configuration.useMavenPublishing
        1 * gitRemote.mergeIssueLabels(true)
        1 * gitRemote.issueLabels(issueLabels)
        1 * gitRemote.repoUser(user)

    }

    def "project name also transferred to GitPlus"() {
        given:
        String projectName = "wiggly"

        when:
        configuration.projectName(projectName)

        then:
        configuration.projectName == projectName
        1 * gitPlus.remote.repoName(projectName)
        1 * gitPlus.local.projectName(projectName)
    }

    def "baseVersion creates a step"() {
        given:
        String baseVersion = '0.0.5.6'

        when:
        configuration.baseVersion(baseVersion)

        then:
        configuration.steps.get(0) instanceof BaseVersion
        configuration.steps.get(0).baseVersion == baseVersion
    }

    def "test sets does not ignore duplicates"() {
        given:
        TestSet testSet1 = new TestSet("test", TestFramework.JUNIT, "4.12")
        TestSet testSet2 = new TestSet("test", TestFramework.SPOCK, "1.2")
        TestSet testSet3 = new TestSet("test", TestFramework.SPOCK, "1.2")

        when:
        configuration.testSet("test", TestFramework.JUNIT, "4.12")
        configuration.testSet("test", TestFramework.SPOCK, "1.2")
        configuration.testSet("test", TestFramework.SPOCK, "1.2")

        then:
        configuration.steps.size() == 3
        configuration.steps.contains(testSet1)
        configuration.steps.contains(testSet2)
        configuration.getSteps().contains(testSet3) // same as 2
    }

    def "source steps does not ignore duplicates"() {
        given:
        SourceLanguage source1 = new SourceLanguage(Language.JAVA, "1.6")
        SourceLanguage source2 = new SourceLanguage(Language.KOTLIN, "1.0.3")
        SourceLanguage source3 = new SourceLanguage(Language.KOTLIN, "1.0.3")

        when:
        configuration.source(Language.JAVA, "1.6")
        configuration.source(Language.KOTLIN, "1.0.3")
        configuration.source(Language.KOTLIN, "1.0.3")

        then:
        configuration.steps.size() == 3
        configuration.steps.contains(source1)
        configuration.steps.contains(source2)
        configuration.getSteps().contains(source3) // same as 2
    }
}
