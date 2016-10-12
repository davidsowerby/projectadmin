package uk.q3c.build.creator.gradle.element


class ApplyPluginElement(content: String) : BasicScriptElement(content), ScriptElement {

    override fun formatContent(): String {
        return "apply plugin: '$content'"
    }
}