package uk.q3c.build.creator.gradle

import uk.q3c.build.creator.KotlinObjectFactory

/**
 * Created by David Sowerby on 06 Sep 2016
 */
class TaskTest extends BlockReaderSpecification {


    Task task

    def setup() {
        KotlinObjectFactory.fileBuffer().reset()
    }

    def "with all attributes in constructor without a body"() {

        when:
        task = new Task('newTask', 'Copy', 'otherTask', 'plugin').write()

        then:
        List<String> result = resultLines()
        result.get(0) == "task(name: 'newTask', type: Copy, dependsOn: otherTask, plugin: plugin) {"
        result.get(1) == "}"
        result.get(2) == ""
        result.size() == 3
    }


    def "with all attributes in constructor with a body"() {
        when:
        task = new Task('newTask', 'Copy', 'otherTask', 'plugin')
        task.line('line 1').write()


        then:
        List<String> result = resultLines()
        result.get(0) == "task(name: 'newTask', type: Copy, dependsOn: otherTask, plugin: plugin) {"
        result.get(1) == "    line 1"
        result.get(2) == "}"
        result.get(3) == ""
        result.size() == 4
    }


    def "with attributes set directly"() {

        when:
        task = new Task('newTask').type('Copy').dependsOn('otherTask').plugin('plugin').write()

        then:
        List<String> result = resultLines()
        result.get(0) == "task(name: 'newTask', type: Copy, dependsOn: otherTask, plugin: plugin) {"
        result.get(1) == "}"
        result.get(2) == ""
        result.size() == 3
    }


    def "without attributes, with body"() {
        given:
        task = new Task('newTask')

        when:
        task.line('line 1', 'line 2').write()

        then:
        List<String> result = resultLines()
        result.get(0) == "task(name: 'newTask') {"
        result.get(1) == "    line 1"
        result.get(2) == "    line 2"
        result.get(3) == "}"
        result.get(4) == ""
        result.size() == 5
    }


}
