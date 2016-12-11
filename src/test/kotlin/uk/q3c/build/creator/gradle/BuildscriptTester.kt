package uk.q3c.build.creator.gradle

/**
 * Exercises the Kotlin API for [Buildscript]
 *
 * Created by David Sowerby on 11 Dec 2016
 */
class BuildscriptTester {

    fun dependenciesOnly(builder: GradleGroovyBuilder) {
        builder.buildscript {
            dependencies("compile") {
                +"compile1"
                +"compile2"
            }
        }
    }

    fun repositoriesOnly(builder: GradleGroovyBuilder) {
        builder.buildscript {
            repositories {
                +"a"
                +"b"
            }
        }
    }

}