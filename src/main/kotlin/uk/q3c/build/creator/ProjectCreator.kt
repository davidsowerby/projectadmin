package uk.q3c.build.creator

import com.google.common.collect.ImmutableList
import java.io.File

/**
 * Created by David Sowerby on 10 Oct 2016
 */
interface ProjectCreator : Features<ProjectConfiguration> ,ProjectConfiguration{



}


interface ProjectConfiguration : Features<ProjectConfiguration>{
    var basePackage: String
    var projectDir : File
    var useMavenPublishing : Boolean

    fun projectDir(dir: File) : ProjectConfiguration
    fun basePackage(basePackage: String) : ProjectConfiguration

    fun getSteps(): ImmutableList<ConfigStep>
    fun mavenPublishing(value: Boolean): ProjectConfiguration
}