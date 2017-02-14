package uk.q3c.build.creator

import com.google.inject.Inject

/**
 * Created by David Sowerby on 10 Oct 2016
 */
class DefaultProjectCreator @Inject constructor(val builders: MutableSet<Builder>, val configuration: ProjectConfiguration) : ProjectCreator, ProjectConfiguration by configuration {

    override fun execute() {
        prepareGitPlus()
        for (builder in builders) {
            builder.projectCreator(this)
        }
        for (step in configuration.getSteps()) {
            for (builder in builders) {
                builder.configParam(step)
            }
        }
        if (useMavenPublishing) {
            for (builder in builders) {
                builder.mavenPublishing()
            }
        }

        for (builder in builders) {
            builder.execute() // create directories etc
        }

        // This will create repos if config correctly set up
        configuration.gitPlus.execute()

        // this could duplicate the merge if already done by previous step.  This should be fixable when gitPlus API fixed
        // see https://github.com/davidsowerby/gitplus/issues/77
        if (configuration.mergeIssueLabels) {
            configuration.gitPlus.remote.mergeLabels()
        }
    }

    private fun prepareGitPlus() {
        configuration.gitPlus.local.create = createNewProject
        configuration.gitPlus.remote.create = createNewProject
        configuration.gitPlus.remote.mergeIssueLabels = mergeIssueLabels
        configuration.gitPlus.local.projectName = projectName
        configuration.gitPlus.remote.repoUser = projectUserName
        configuration.gitPlus.remote.publicProject = publicProject
        configuration.gitPlus.local.projectDirParent = projectDir.parentFile
    }

    override fun buildersCount(): Int {
        return builders.size
    }


}

