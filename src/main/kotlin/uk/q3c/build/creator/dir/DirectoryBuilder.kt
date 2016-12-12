package uk.q3c.build.creator.dir

import org.apache.commons.io.FileUtils
import uk.q3c.build.creator.*
import java.io.File

/**
 * Creates directories and files as required by configuration calls to [configParam] methods
 *
 * Created by David Sowerby on 11 Oct 2016
 */
class DirectoryBuilder : Builder {
    override fun writeToFile(outputFile: File) {
        throw UnsupportedOperationException("DirectoryBuilder has nothing to write to file")
    }


    private lateinit var projectCreator: ProjectCreator
    val directories: MutableList<File> = mutableListOf()
    val files: MutableList<FileCreator> = mutableListOf()
    val dummyJavaCount: Int = 0


    override fun execute() {
        for (dir in directories) {
            if (!dir.exists()) {
                FileUtils.forceMkdir(dir)
            }
        }

        for (fc in files) {
            fc.create()
        }
    }

    override fun mavenPublishing() {
        // do nothing
    }


    override fun setProjectCreator(creator: ProjectCreator) {
        this.projectCreator = creator

    }


    override fun configParam(sourceLanguage: SourceLanguage) {
        when (sourceLanguage.language) {
            Language.JAVA -> addDirectories("src/main/java", mainResourcesDir())
            Language.KOTLIN -> addDirectories("src/main/kotlin", mainResourcesDir())
            Language.GROOVY -> addDirectories("src/main/groovy", mainResourcesDir())
        }
    }

    private fun addDirectories(mainDir: String, resources: File) {
        val main = File(projectCreator.projectDir, mainDir)
        directories.add(main)
        directories.add(resources)
        if (basePackageAsPath() != "") {
            directories.add(File(main, basePackageAsPath()))
            directories.add(File(resources, basePackageAsPath()))
        }
    }

    fun mainResourcesDir(): File {
        return File(projectCreator.projectDir, "src/main/resources")
    }

    fun testResourcesDir(): File {
        return File(projectCreator.projectDir, "src/test/resources")
    }

    override fun configParam(testSet: TestSet) {
        val languageDir: String = when (testSet.testFramework) {

            TestFramework.JUNIT -> "java"
            TestFramework.SPOCK -> "groovy"
        }
        val testDirPath = "src/${testSet.setName}/$languageDir"
        addDirectories(testDirPath, testResourcesDir())
    }

    private fun basePackageAsPath(): String {
        return projectCreator.basePackage.replace(".", "/")
    }

}

class FileCreator(val file: File, val content: String) {
    fun create() {
        FileUtils.write(file, content)
    }
}