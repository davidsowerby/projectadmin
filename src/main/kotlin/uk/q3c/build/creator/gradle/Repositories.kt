package uk.q3c.build.creator.gradle

class Repositories : NamedBlock() {

    override fun blockName(): String {
        return "repositories"
    }

    fun repositories(vararg repositories: String): Repositories {
        for (repository in repositories) {
            +repository
        }
        return this
    }

    fun mavenLocal(): Repositories {
        +"mavenLocal()"
        return this
    }

    fun mavenCentral(): Repositories {
        +"mavenCentral()"
        return this
    }


    fun jcenter(): Repositories {
        +"jcenter()"
        return this
    }
}