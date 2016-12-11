package uk.q3c.build.creator.gradle


/**
 * Represents the 'buildscript' block in a Gradle grrovy build.gradle
 *
 * The [repositories] and [dependencies] are added to [elements] only when they have been called - this is to stop them
 * being generated as empty blocks if they have not been used.
 */
class Buildscript : NamedBlock() {

    val dependencies = Dependencies()
    val repositories = Repositories()

    init {
        elements.add(repositories)
        elements.add(dependencies)
    }

    override fun blockName(): String {
        return "buildscript"
    }

    fun repositories(init: Repositories.() -> Unit): Repositories {
        repositories.init()
        return repositories()
    }

    fun dependencies(scope: String, init: Dependencies.() -> Unit): Dependencies {
        dependencies.scope(scope)
        dependencies.init()
        return dependencies()
    }

    fun dependencies(): Dependencies {
        return dependencies
    }

    fun repositories(vararg repos: String): Repositories {
        for (repo in repos) {
            repositories.repositories(*repos)
        }
        return repositories
    }

    /**
     * If there are only two sub-elements, they will be Dependencies and Repositories.  If they are both empty, we do not want to print
     * anything
     */
    override fun isEmpty(): Boolean {
        if (dependencies.isEmpty() && repositories.isEmpty() && elements.size == 2) {
            return true
        }
        return false
    }
}