package uk.q3c.build.creator

/**
 * A common interface for all [Builder] implementations.  Each builder is passed each of the [configParam] values, enabling
 * a builder to set itself up as part of a configuration stage.
 *
 * ConfigParams currently enable selection of:
 *
 * * source language(s)
 * * test set(s) (which includes a test framework)
 * * base version (that is, the version without a build number)
 *
 * Created by David Sowerby on 10 Oct 2016
 */
interface Builder {
    /**
     * Builders may need information from the [ProjectCreator] - this method at the start of the [ProjectCreator.execute] method,
     * enabling builders to respond as appropriate
     */
    fun projectCreator(creator: ProjectCreator)
    fun execute()

    /**
     * [configStep] is passed to all builders for them to configure as appropriate (which may include ignoring the step)
     */
    fun configParam(configStep: ConfigStep)
    fun mavenPublishing()
}


