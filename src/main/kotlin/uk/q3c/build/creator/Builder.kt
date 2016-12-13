package uk.q3c.build.creator

import java.io.File

/**
 * A common interface for all [Builder] implementations.  Each builder is passed each of the [configParam] values, enabling
 * a builder to set itself up as part of a configuration stage.
 *
 * ConfigParams currently enable selection of:
 *
 * * source language(s)
 * * test set(s) (which includes a test framework)
 *
 * Created by David Sowerby on 10 Oct 2016
 */
interface Builder {
    fun setProjectCreator(creator: ProjectCreator)
    fun execute()

    /**
     * [configStep] is passed to all builders for them to configure as appropriate (which may include ignoring the step)
     */
    fun configParam(configStep: ConfigStep)
    fun mavenPublishing()
    fun writeToFile(outputFile: File)
}


