package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.gradle.element.BasicScriptElement


/**
 * Created by David Sowerby on 30 Sep 2016
 */
open class Config(name: String) : VariableNamedBlock(name) {

    init {
        writeWhenEmpty = true
    }

    fun config(name: String, init: Config.() -> Unit): Config {
        val cfg: Config = Config(name)
        cfg.init()
        elements.add(cfg)
        return cfg
    }

    fun line(vararg lines: String) {
        for (line in lines) {
            elements.add(BasicScriptElement(line))
        }
    }

}