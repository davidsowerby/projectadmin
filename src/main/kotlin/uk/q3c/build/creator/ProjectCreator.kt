package uk.q3c.build.creator

import com.google.common.collect.ImmutableList
import uk.q3c.build.gitplus.gitplus.GitPlus
import java.io.File

/**
 * The implementation of this interface works in two stages - the first stage prepares by making configuration calls (for example to [source], [testSet] and [mergeIssueLabels]
 * These calls are delegated to [ProjectConfiguration]
 *
 * The [execute] method is then called to generate the required output, by providing the configuration to the [Builder]
 * implementations.  These builder implementations are defined by the Guice [ProjectCreatorModule]
 *
 * Note that for any action which requires authorised access to the remote repo, appropriate [API keys are required](http://gitplus.readthedocs.io/en/master/build-properties/).
 *
 * Created by David Sowerby on 10 Oct 2016
 */
interface ProjectCreator : ProjectConfiguration {

    fun buildersCount(): Int
    fun execute()
}


interface ProjectConfiguration {
    val gitPlus: GitPlus // this will eventually be removed, see issue #13
    var basePackage: String
    var projectDir: File
    var useMavenPublishing: Boolean
    var mergeIssueLabels: Boolean
    var projectUserName: String
    var projectName: String

    fun projectDir(dir: File): ProjectConfiguration
    fun basePackage(basePackage: String): ProjectConfiguration

    fun getSteps(): ImmutableList<ConfigStep>
    fun mavenPublishing(value: Boolean): ProjectConfiguration

    fun source(language: Language, version: String): ProjectConfiguration
    /**
     * Adds a test set (for example 'integrationTest') which allows [Builder]s to create appropriate directories and dependencies.
     * If you use multiple test frameworks for a test set, call this method multiple times
     *
     * @param setName the name of the test set
     * @param testFramework which test framework to use.  This enables identification of the appropriate dependencies
     * @param version the version of [testFramework] to use.  An empty String will cause the latest released version to be used
     */
    fun testSet(setName: String, testFramework: TestFramework, version: String = ""): ProjectConfiguration

    /**
     * If true, merges the labels provided by [issueLabels] with the remote repo's existing issue labels.  See [GitRemote.mergeIssueLabels]
     * If false, this action is cancelled.
     */
    fun mergeIssueLabels(merge: Boolean=true): ProjectConfiguration

    /**
     * The issues to use for the remote repo.  If this is not set and [mergeIssueLabels] is set to true, the default labels provided by [GitPlusConfiguration] are used
     */
    fun issueLabels(labels: Map<String, String>): ProjectConfiguration

    fun projectName(projectName: String): ProjectConfiguration
    fun remoteRepoUser(username: String): ProjectConfiguration
    fun baseVersion(baseVersion: String): ProjectConfiguration
}

enum class Language {
    JAVA, KOTLIN, GROOVY
}

enum class TestFramework {
    JUNIT, SPOCK
}