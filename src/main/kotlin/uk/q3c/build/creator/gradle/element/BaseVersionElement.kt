package uk.q3c.build.creator.gradle.element


class BaseVersionElement(version: String) : BasicScriptElement(version) {
    override fun write() {
        fileBuffer.appendLine("ext.baseVersion = '$content'")
    }
}