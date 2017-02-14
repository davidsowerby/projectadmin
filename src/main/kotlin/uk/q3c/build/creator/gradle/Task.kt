package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.gradle.element.BasicScriptElement

/**
 * Created by David Sowerby on 02 Oct 2016
 */
class Task(name: String, var type: String = "", var dependsOn: String = "", var plugin: String = "") : VariableNamedBlock(name) {

    //This is here only to give Java users a constructor with name only
    constructor(name: String) : this(name, type = "", dependsOn = "", plugin = "")


    init {
        writeWhenEmpty = true
    }

    override fun openBlock() {
        fileBuffer.append("task $name(")
        var precedingArgument: Boolean = false
//        precedingArgument = addArgument("name", "'$name'", precedingArgument)
        precedingArgument = addArgument("type", type, precedingArgument)
        precedingArgument = addArgument("dependsOn", dependsOn, precedingArgument)
        addArgument("plugin", plugin, precedingArgument)
        fileBuffer.appendLine(") {")
        fileBuffer.incrementIndent()
    }

    private fun addArgument(argumentName: String, argumentValue: String, precedingArgument: Boolean): Boolean {
        if (argumentValue.isNotEmpty()) {
            if (precedingArgument) {
                fileBuffer.append(", ")
            }
            fileBuffer.append(argumentName, ": ", argumentValue)
            return true
        }
        return false
    }

    fun type(type: String): Task {
        this.type = type
        return this
    }

    fun dependsOn(dependsOn: String): Task {
        this.dependsOn = dependsOn
        return this
    }

    fun plugin(plugin: String): Task {
        this.plugin = plugin
        return this
    }

    fun line(vararg lines: String): Task {
        for (line in lines) {
            elements.add(BasicScriptElement(line))
        }
        return this
    }

}


