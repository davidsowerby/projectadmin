package uk.q3c.build.creator.dir

import uk.q3c.build.creator.FeatureBuilder
import uk.q3c.build.creator.Language
import uk.q3c.build.creator.TestFramework
import java.io.File

/**
 * Created by David Sowerby on 11 Oct 2016
 */
class DirectoryFeatureBuilder : FeatureBuilder<DirectoryFeatureBuilder> {
    private lateinit var projectDir: File

    override fun write() {
        TODO()
    }

    override fun mavenPublishing(): DirectoryFeatureBuilder {
        TODO()
    }

    override fun testSet(setName: String, testFramework: TestFramework, version: String): DirectoryFeatureBuilder {
        TODO()
    }

    override fun setProjectDir(dir: File) {
        this.projectDir = dir
    }

    override fun source(language: Language, version: String): DirectoryFeatureBuilder {
        TODO()
    }


}