package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.*
import uk.q3c.build.creator.gradle.buffer.DefaultFileBuffer
import uk.q3c.build.creator.gradle.buffer.FileBuffer
import uk.q3c.build.creator.gradle.element.*
import java.io.File

/**
 * An implementation of [Builder] to produce a groovy build.gradle file
 *
 * Created by David Sowerby on 10 Oct 2016
 */
class GradleGroovyBuilder : Builder {

    private lateinit var projectCreator: ProjectCreator
    private var filename: String = "build.gradle"

    val mavenLocal = "mavenLocal()"
    val jcenter = "jcenter()"
    val mavenCentral = "mavenCentral()"

    val maven = "maven"
    val mavenPublishing = "maven-publish"

    val elements: MutableList<ScriptElement> = mutableListOf()
    val fileBuffer: FileBuffer = DefaultFileBuffer
    val buildscript = Buildscript()
    val plugins = Plugins()
    val repositories = Repositories()
    val dependencies = Dependencies()
    val testSets = TestSets()


    override fun write() {
        buildscript.writeBlockToBuffer()
        plugins.writeBlockToBuffer()
        testSets.writeBlockToBuffer()
        repositories.writeBlockToBuffer()
        dependencies.writeBlockToBuffer()


        for (element in elements) {
            element.write()
        }
        fileBuffer.writeToFile(outputFile())
        fileBuffer.reset()
    }


    private fun outputFile(): File {
        return File(projectCreator.projectDir, filename)
    }


    fun javaSource(sourceLevel: String) {
        plugins {
            +"java"
        }
        elements.add(BasicScriptElement("sourceCompatibility = '$sourceLevel'"))
    }

    fun buildscript(init: Buildscript.() -> Unit): Buildscript {
        buildscript.init()
        return buildscript
    }

    fun dependencies(scope: String, init: Dependencies.() -> Unit): Dependencies {
        dependencies.setCurrentScope(scope)
        dependencies.init()
        return dependencies
    }


    fun task(name: String, type: String, dependsOn: String, plugin: String): GradleGroovyBuilder {
        elements.add(Task(name = name, type = type, dependsOn = dependsOn, plugin = plugin))
        return this
    }

    fun wrapper(gradleVersion: String): GradleGroovyBuilder {
        task(name = "wrapper", type = "Wrapper", plugin = "", dependsOn = "") {
            +"gradleVersion = '$gradleVersion'"
        }
        return this
    }

    fun kotlinSource(version: String) {
        buildscript {
            +"ext.kotlin_version = '$version'"
            repositories {
                +jcenter
                +mavenCentral
            }
            dependencies("classpath") {
                +"org.jetbrains.kotlin:kotlin-gradle-plugin:\$kotlin_version"
            }
        }
        defaultRepositories()
        dependencies("compile") {
            +"org.jetbrains.kotlin:kotlin-stdlib:\$kotlin_version"
            +"org.jetbrains.kotlin:kotlin-reflect:\$kotlin_version"
        }
    }

    fun plugins(init: Plugins.() -> Unit): Plugins {
        plugins.init()
        return plugins
    }

    fun repositories(init: Repositories.() -> Unit): Repositories {
        repositories.init()
        return repositories
    }

    fun task(name: String, type: String, dependsOn: String, plugin: String, init: Task.() -> Unit): Task {
        val task = Task(name, type, dependsOn, plugin)
        task.init()
        elements.add(task)
        return task
    }


    fun config(name: String, init: Config.() -> Unit): Config {
        val cfg: Config = Config(name)
        cfg.init()
        elements.add(cfg)
        return cfg
    }

    fun dependencies(): Dependencies {
        return dependencies
    }

    fun config(name: String): Config {
        val cfg: Config = Config(name)
        elements.add(cfg)
        return cfg
    }

    fun testSets(init: TestSets.() -> Unit): TestSets {
        testSets.init()
        return testSets
    }

    fun applyPlugin(name: String): GradleGroovyBuilder {
        elements.add(ApplyPluginElement(name))
        return this
    }

    fun applyFrom(name: String): GradleGroovyBuilder {
        elements.add(ApplyFromElement(name))
        return this
    }

    fun baseVersion(version: String): GradleGroovyBuilder {
        elements.add(BaseVersionElement(version))
        return this
    }

    fun buildscript(): Buildscript {
        return buildscript
    }

    fun repositories(): Repositories {
        return repositories
    }

    fun plugins(vararg pluginIds: String): GradleGroovyBuilder {
        for (pluginId in pluginIds) {
            plugins {
                +pluginId
            }
        }
        return this
    }


    override fun setProjectCreator(creator: ProjectCreator) {
        this.projectCreator = creator
    }

    override fun set(step: SourceLanguage) {
        when (step.language) {
            Language.JAVA -> javaSource(step.version)
            Language.KOTLIN -> kotlinSource(step.version)
            Language.GROOVY -> TODO()
        }
    }

    override fun set(step: TestSet) {
        defaultRepositories()
        testSets {
            +step.setName
        }
        plugins {
            +"org.unbroken-dome.test-sets version 1.2.0"
        }
        val dependencyScope: String = step.setName + "Compile"

        dependencies(dependencyScope) {
            when (step.testFramework) {
                TestFramework.JUNIT -> +dependencyStr("junit:junit", step.version)
                TestFramework.SPOCK -> {
                    +dependencyStr("org.spockframework:spock-core", step.version)
                    +dependencyStr("cglib:cglib-nodep", "")
                    +dependencyStr("org.objenesis:objenesis", "")
                }
            }
        }
    }

    override fun mavenPublishing() {
        plugins {
            +maven
            +mavenPublishing
        }
        config("publishing") {
            config("publications") {
                config("mavenStuff(MavenPublication)") {
                    +"from components.java"
                    config("artifact sourcesJar") {
                        +"classifier 'sources'"
                    }
                    config("artifact javadocJar") {
                        +"classifier 'javadoc'"
                    }
                }
            }

        }
    }


    private fun dependencyStr(groupModule: String, version: String): String {
        if (version == "") {
            return "$groupModule:${latestVersion(groupModule)}"
        } else {
            return "$groupModule:$version"
        }
    }

    /**
     * This should be replaced by a bintray / mvn lookup
     */
    private fun latestVersion(groupModule: String): String {
        return when (groupModule) {
            "junit:junit" -> "4:12"
            "org.spockframework:spock-core" -> "1.0-groovy-2.4"
            "cglib:cglib-nodep" -> "3.2.0"
            "org.objenesis:objenesis" -> "2.2"
            else -> {
                throw UnsupportedOperationException(groupModule + " is not a recognised artifact")
            }
        }

    }

    private fun dep(groupModule: String, version: String): String {
        return "$groupModule:$version"
    }

    private fun defaultRepositories() {
        repositories {
            +jcenter
            +mavenCentral
        }
    }
}