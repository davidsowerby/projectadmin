package uk.q3c.build.creator

import com.google.inject.Inject

/**
 * Created by David Sowerby on 10 Oct 2016
 */
class DefaultProjectCreator @Inject constructor(val builders: MutableSet<Builder>, val configuration: ProjectConfiguration) : ProjectCreator, ProjectConfiguration by configuration {

//    private var builders: Set<Builder>


    init {
//        this.builders = featureBuilders
        for (builder in builders) {
            builder.setProjectCreator(this)
        }
    }


    fun execute() {
        for (step in configuration.getSteps()) {
            for (builder in builders) {
                when(step){
                    is SourceLanguage -> builder.set(step)
                    is TestSet -> builder.set(step)
                    else -> {
                        throw UnknownStepException (step.javaClass.name+" is unknown")
                    }
                }
            }
        }
    }


}

class UnknownStepException(msg: String) : Throwable(msg) {

}
