package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.gradle.element.BasicScriptElement

/**
 * Defines a set of dependencies.  Note that because it is a set, a duplicate entry (the same artifact and version, but
 * possibly a different scope) will overwrite a previous entry.
 */
class Dependencies : NamedBlock() {

    override fun blockName(): String {
        return "dependencies"
    }

    var currentScope: String = "compile"

    /**
     * Adds a string as an element constructed as [currentScope] + the string.  The string is quoted, unless it contains parenthesis
     * (for example in 'gradleApi()'), then the string remains unquoted
     */
    override operator fun String.unaryPlus() {
        if (this.contains("()")) {
            elements.add(BasicScriptElement("$currentScope $this"))
        } else {
            if (this.contains("$")) {
                elements.add(BasicScriptElement("$currentScope \"$this\""))
            } else {
                elements.add(BasicScriptElement("$currentScope '$this'"))
            }
        }
    }

    /**
     * Callback used to enable Kotlin DSL approach
     */
    internal fun dependency(scope: String, dependency: String, init: Dependency.() -> Unit): Dependency {
        val dep = Dependency(scope, dependency)
        dep.init()
        elements.add(dep)
        return dep
    }

    /**
     * Add a dependency with the given [scope] and [dependency] descriptor
     */
    fun dependency(scope: String, dependency: String): Dependency {
        val dep = Dependency(scope, dependency)
        elements.add(dep)
        return dep
    }

    /**
     * Add a dependency with using [currentScope] and the [dependency] descriptor
     */
    fun dependency(dependency: String): Dependency {
        val dep = Dependency(currentScope, dependency)
        elements.add(dep)
        return dep
    }

    /**
     * Add all the [dependencies] to [scope]
     */
    fun dependencies(scope: String, vararg dependencies: String): Dependencies {
        for (dependency in dependencies) {
            dependency(scope, dependency)
        }
        return this
    }

    /**
     * Add all the [compileDependencies] with scope set to 'compile'
     */
    fun compile(vararg compileDependencies: String): Dependencies {
        return dependencies("compile", *compileDependencies)
    }


    /**
     * Add all the [runtimeDependencies] with scope set to 'runtime'
     */
    fun runtime(vararg runtimeDependencies: String): Dependencies {
        return dependencies("runtime", *runtimeDependencies)
    }

    /**
     * Add all the [testCompileDependencies] with scope set to 'testCompile'
     */
    fun testCompile(vararg testCompileDependencies: String): Dependencies {
        return dependencies("testCompile", *testCompileDependencies)
    }

    /**
     * Add all the [integrationTestCompile] with scope set to 'integrationTestCompileDependencies'
     */
    fun integrationTestCompile(vararg integrationTestCompileDependencies: String): Dependencies {
        return dependencies("integrationTestCompile", *integrationTestCompileDependencies)
    }

    /**
     * Set the current scope
     */
    fun scope(scope: String): Dependencies {
        this.currentScope = scope
        return this
    }


}