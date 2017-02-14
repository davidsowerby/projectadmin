package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.*
import uk.q3c.build.creator.Language.*
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
    lateinit var outputDir: File


    override fun execute() {
        buildscript.writeBlockToBuffer()
        plugins.writeBlockToBuffer()
        testSets.writeBlockToBuffer()
        repositories.writeBlockToBuffer()
        dependencies.writeBlockToBuffer()


        for (element in elements) {
            element.write()
        }

        fileBuffer.writeToFile(File(outputDir, filename))
        fileBuffer.reset()
    }


    override fun configParam(configStep: ConfigStep) {
        when (configStep) {
            is SourceLanguage -> configSourceLanguage(configStep)
            is TestSet -> configTestSet(configStep)
            is BaseVersion -> configBaseVersion(configStep)
        }
    }

    private fun configBaseVersion(configStep: BaseVersion) {
        elements.add(BaseVersionElement(configStep.baseVersion))
    }

    private fun javaSource(language: SourceLanguage): GradleGroovyBuilder {
        plugins {
            +"java"
        }
        elements.add(BasicScriptElement("sourceCompatibility = '${language.languageVersion()}'"))
        return this
    }

    fun buildscript(init: Buildscript.() -> Unit): Buildscript {
        buildscript.init()
        return buildscript
    }

    fun dependencies(scope: String, init: Dependencies.() -> Unit): Dependencies {
        dependencies.scope(scope)
        dependencies.init()
        return dependencies
    }


    fun task(name: String, type: String = "", dependsOn: String = "", plugin: String = ""): Task {
        val task: Task = Task(name = name, type = type, dependsOn = dependsOn, plugin = plugin)
        elements.add(task)
        return task
    }


    fun wrapper(gradleVersion: String): GradleGroovyBuilder {
        task(name = "wrapper", type = "Wrapper", plugin = "", dependsOn = "") {
            +"gradleVersion = '$gradleVersion'"
        }
        return this
    }

    fun kotlinSource(language: SourceLanguage) {
        buildscript {
            elements.add(0, BasicScriptElement("ext.kotlin_version = '${language.languageVersion()}'"))
            repositories {
                +jcenter
                +mavenCentral
            }
            dependencies("classpath") {
                +"org.jetbrains.kotlin:kotlin-gradle-plugin:\$kotlin_version"
            }
        }
        applyPlugin("kotlin")
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

    fun task(name: String, type: String, dependsOn: String = "", plugin: String = "", init: Task.() -> Unit): Task {
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

    fun repositories(vararg repos: String): Repositories {
        repositories.repositories(*repos)
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

    fun line(line: String): GradleGroovyBuilder {
        elements.add(BasicScriptElement(line))
        return this
    }


    private fun configSourceLanguage(sourceLanguage: SourceLanguage) {

        when (sourceLanguage.language) {
            JAVA -> javaSource(sourceLanguage)
            KOTLIN -> kotlinSource(sourceLanguage)
            GROOVY -> gradleSource()
        }
    }

    private fun gradleSource() {
        plugins {
            +"groovy"
        }
    }

    private fun configTestSet(testSet: TestSet) {
        defaultRepositories()
        testSets {
            +testSet.setName
        }
        plugins {
            +"org.unbroken-dome.test-sets version 1.2.0"
        }
        val dependencyScope: String = testSet.setName + "Compile"

        dependencies(dependencyScope) {
            when (testSet.testFramework) {
                TestFramework.JUNIT -> +dependencyStr("junit:junit", testSet.frameworkVersion())
                TestFramework.SPOCK -> {
                    +dependencyStr("org.spockframework:spock-core", testSet.frameworkVersion())
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

        task(name = "sourcesJar", type = "Jar", dependsOn = "classes") {
            +"classifier = 'sources'"
            +"from sourceSets.main.allSource"
        }
        task(name = "javadocJar", type = "Jar", dependsOn = "javadoc") {
            +"classifier = 'javadoc'"
            +"from javadoc.destinationDir"
        }
        config(name = "artifacts") {
            +"archives sourcesJar"
            +"archives javadocJar"
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
                throw UnsupportedOperationException(groupModule + " is not a recognised artifact for retrieving the latest version")
            }
        }

    }

    override fun projectCreator(creator: ProjectCreator) {
        outputDir = creator.projectDir
    }

    private fun defaultRepositories() {
        repositories {
            +jcenter
            +mavenCentral
        }
    }
}