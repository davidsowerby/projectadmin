package uk.q3c.build.creator.gradle

open class VariableNamedBlock(val name: String) : NamedBlock() {


    override fun blockName(): String {
        return name
    }
}