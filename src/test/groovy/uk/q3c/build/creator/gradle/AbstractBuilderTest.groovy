package uk.q3c.build.creator.gradle

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import uk.q3c.build.creator.Builder
import uk.q3c.build.creator.KotlinObjectFactory
import uk.q3c.build.creator.ProjectCreator
import uk.q3c.build.creator.gradle.buffer.FileBuffer

/**
 * Created by David Sowerby on 11 Dec 2016
 */
class AbstractBuilderTest extends Specification {

    @Rule
    TemporaryFolder temporaryFolder
    File temp
    Builder builder
    ProjectCreator projectCreator = Mock(ProjectCreator)
    FileBuffer fileBuffer

    def setup() {
        temp = temporaryFolder.getRoot()
        projectCreator.projectDir >> temp
        createBuilder()
        builder.setProjectCreator(projectCreator)
        fileBuffer = KotlinObjectFactory.fileBuffer()
        fileBuffer.reset()
    }

    def createBuilder() {
        builder = new GradleGroovyBuilder()
    }
}
