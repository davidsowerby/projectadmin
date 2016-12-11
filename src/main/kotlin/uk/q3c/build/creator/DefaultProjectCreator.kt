package uk.q3c.build.creator

import com.google.inject.Inject

/**
 * Created by David Sowerby on 10 Oct 2016
 */
class DefaultProjectCreator @Inject constructor(val builders: MutableSet<Builder>, val configuration: ProjectConfiguration) : ProjectCreator, ProjectConfiguration by configuration {

    init {
        for (builder in builders) {
            builder.setProjectCreator(this)
        }
    }


    override fun execute() {
        for (step in configuration.getSteps()) {
            for (builder in builders) {
                when (step) {
                    is SourceLanguage -> builder.configParam(step)
                    is TestSet -> builder.configParam(step)
                    else -> {
                        throw UnknownStepException(step.javaClass.name + " is unknown")
                    }
                }
            }
        }
        if (useMavenPublishing) {
            for (builder in builders) {
                builder.mavenPublishing()
            }
        }

        // This will create repos if config correctly set up
        configuration.gitPlus.execute()

        // this could duplicate the merge if already done by previous step.  This should be fixable when gitPlus API fixed
        // see https://github.com/davidsowerby/gitplus/issues/77
        if (configuration.mergeIssueLabels) {
            configuration.gitPlus.remote.mergeLabels()
        }
    }

    override fun buildersCount(): Int {
        return builders.size
    }


}

class UnknownStepException(msg: String) : Throwable(msg)
