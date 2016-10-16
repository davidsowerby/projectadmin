package uk.q3c.build.creator

import com.google.common.collect.ImmutableList
import com.google.inject.Inject
import uk.q3c.build.gitplus.gitplus.GitPlus
import java.io.File

/**
 * Created by David Sowerby on 13 Oct 2016
 */
class DefaultProjectConfiguration @Inject constructor(override val gitPlus: GitPlus) : ProjectConfiguration {

    private val steps: MutableList<ConfigStep> = mutableListOf()
    lateinit override var projectDir: File
    lateinit override var basePackage: String
    override var useMavenPublishing: Boolean = false
    override var mergeIssueLabels: Boolean = false
    lateinit override var projectName: String
    lateinit override var projectUserName: String


    override fun projectDir(dir: File): ProjectConfiguration {
        this.projectDir = dir
        return this
    }

    override fun basePackage(basePackage: String): ProjectConfiguration {
        this.basePackage = basePackage
        return this
    }

    override fun testSet(setName: String, testFramework: TestFramework, version: String): ProjectConfiguration {
        steps.add(TestSet(setName, testFramework, version))
        return this
    }

    override fun mavenPublishing(value: Boolean): ProjectConfiguration {
        useMavenPublishing = value
        return this
    }

    override fun mergeIssueLabels(merge: Boolean): ProjectConfiguration {
        gitPlus.configuration.mergeIssueLabels(merge)
        return this
    }

    override fun issueLabels(labels: Map<String, String>): ProjectConfiguration {
        gitPlus.configuration.issueLabels(labels)
        return this
    }

    override fun getSteps(): ImmutableList<ConfigStep> {
        return ImmutableList.copyOf(steps)
    }

    override fun source(language: Language, version: String): ProjectConfiguration {
        steps.add(SourceLanguage(language, version))
        return this
    }

    override fun projectName(projectName: String): ProjectConfiguration {
        this.projectName = projectName
        gitPlus.configuration.remoteRepoName(projectName)
        gitPlus.configuration.projectName(projectName)
        return this
    }

    override fun remoteRepoUser(username: String): ProjectConfiguration {
        this.projectUserName = username
        gitPlus.configuration.remoteRepoUser(username)
        return this
    }

}

interface ConfigStep {

}

data class SourceLanguage(val language: Language, val version: String) : ConfigStep {

}

data class TestSet(val setName: String, val testFramework: TestFramework, val version: String) : ConfigStep {

}

