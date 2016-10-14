package uk.q3c.build.creator

/**
 * Created by David Sowerby on 10 Oct 2016
 */
interface Builder {
    fun setProjectCreator(creator: ProjectCreator)
    fun write()
    fun set(step: SourceLanguage)
    fun set(step: TestSet)
    fun mavenPublishing()
}


