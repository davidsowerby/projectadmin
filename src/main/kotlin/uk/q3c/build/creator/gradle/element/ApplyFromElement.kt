package uk.q3c.build.creator.gradle.element


class ApplyFromElement(content: String) : BasicScriptElement(content), ScriptElement {

    override fun formatContent(): String {
        return "apply from: '$content'"
    }
}