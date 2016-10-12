package uk.q3c.build.creator

import java.io.File

/**
 * Created by David Sowerby on 10 Oct 2016
 */
interface FeatureBuilder<P> : Features <P> {
    fun setProjectDir(dir: File)
}


