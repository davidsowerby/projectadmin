package uk.q3c.build.creator.gradle

/**
 * Created by David Sowerby on 11 Oct 2016
 */
class TestSets : Config("testSets") {
    init {
        writeWhenEmpty = false
    }
}