package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.gradle.element.PluginElement

class Plugins : NamedBlock() {

    override fun blockName(): String {
        return "plugins"
    }

    override operator fun String.unaryPlus() {
        elements.add(PluginElement(this))
    }

    fun idea(): Plugins {
        +"idea"
        return this
    }

    fun eclipse(): Plugins {
        +"eclipse-wtp"
        return this
    }

    fun plugins(vararg pluginIds: String): Plugins {
        for (plugin in pluginIds) {
            +plugin
        }
        return this
    }

    fun java(): Plugins {
        +"java"
        return this
    }

    fun groovy(): Plugins {
        +"groovy"
        return this
    }

    fun maven(): Plugins {
        +"maven"
        return this
    }

    fun mavenPublish(): Plugins {
        +"maven-publish"
        return this
    }


}