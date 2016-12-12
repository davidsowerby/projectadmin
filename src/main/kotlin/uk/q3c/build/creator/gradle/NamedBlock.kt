package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.gradle.buffer.DefaultFileBuffer
import uk.q3c.build.creator.gradle.buffer.FileBuffer
import uk.q3c.build.creator.gradle.element.BasicScriptElement
import uk.q3c.build.creator.gradle.element.ScriptElement


abstract class NamedBlock : ScriptElement {
    val fileBuffer: FileBuffer = DefaultFileBuffer
    val elements: MutableList<ScriptElement> = mutableListOf()
    var writeWhenEmpty = false

    open fun openBlock() {
        fileBuffer.appendLine(blockName(), " {")
        fileBuffer.incrementIndent()
    }

    fun closeBlock() {
        fileBuffer.decrementIndent()
        fileBuffer.appendLine("}")
    }

    /**
     * Write the content of this block together with the block opening and closing.  Note that nothing at all is written if the block is empty
     */
    open fun writeBlockToBuffer() {
        if (writeWhenEmpty || (!this.isEmpty())) {
            fileBuffer.blankLine()
            openBlock()
            writeContent()
            closeBlock()
        }
    }

    open fun isEmpty(): Boolean {
        return elements.isEmpty()
    }

    open fun writeContent() {
        for (element in elements.distinct()) {
            element.write()
        }
    }


    override fun write() {
        writeBlockToBuffer()
    }

    abstract fun blockName(): String


    open operator fun String.unaryPlus() {
        elements.add(BasicScriptElement(this))
    }

    fun contains(element: ScriptElement): Boolean {
        return elements.contains(element)
    }
}



