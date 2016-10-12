package uk.q3c.build.creator

import com.google.inject.Inject

/**
 * Created by David Sowerby on 10 Oct 2016
 */
class DefaultProjectCreator @Inject constructor(val featureBuilders: FeatureBuilders) : ProjectCreator {

    private var builders: Set<FeatureBuilder<*>>

    init {
        this.builders = featureBuilders.builders
    }


    override fun testSet(setName: String, testFramework: TestFramework, version: String): ProjectCreator {
        for (builder in builders) {
            builder.testSet(setName, testFramework, version)
        }
        return this
    }

    override fun mavenPublishing(): ProjectCreator {
        for (builder in builders) {
            builder.mavenPublishing()
        }
        return this
    }

    override fun write() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun source(language: Language, version: String): ProjectCreator {
        for (builder in builders) {
            builder.source(language, version)
        }
        return this
    }
}