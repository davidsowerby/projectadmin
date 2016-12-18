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


    val dummyFileContent = "Dummy file to prevent Git ignoring empty directory - delete when you have something else in this directory"
    lateinit var projectCreator: ProjectCreator
    val directories: MutableList<File> = mutableListOf()
    val files: MutableList<FileCreator> = mutableListOf()


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


    override fun configParam(configStep: ConfigStep) {
        if (configStep is SourceLanguage) {
            configSourceLanguage(configStep)
        } else if (configStep is TestSet) {
            configTestSet(configStep)
        }
    }

    private fun configSourceLanguage(sourceLanguage: SourceLanguage) {
        when (sourceLanguage.language) {
            Language.JAVA -> {
                val mainDir = addDirectoryWithPackage("src/main/java")
                addFile(File(mainDir, "dummyFileJava.txt"), dummyFileContent)
            }
            Language.KOTLIN -> {
                val mainDir = addDirectoryWithPackage("src/main/kotlin")
                addFile(File(mainDir, "dummyFileKotlin.txt"), dummyFileContent)
            }
            Language.GROOVY -> {
                val mainDir = addDirectoryWithPackage("src/main/groovy")
                addFile(File(mainDir, "dummyFileGroovy.txt"), dummyFileContent)
            }
        }
        val resourceDir = addDirectoryWithPackage("src/main/resources")
        addFile(File(resourceDir, "dummyResources.txt"), dummyFileContent)
    }

    private fun addFile(file: File, content: String) {
        files.add(FileCreator(file, content))
    }

    /**
     * Creates and returns a directory for the provided directory, extended by the basePackagePath
     *
     */
    private fun addDirectoryWithPackage(directoryName: String): File {
        val main = File(projectCreator.projectDir, directoryName)
        directories.add(main)
        if (basePackageAsPath() != "") {
            val packageDir = File(main, basePackageAsPath())
            directories.add(packageDir)
            return packageDir
        } else {
            return main
        }
    }

    private fun configTestSet(testSet: TestSet) {
        val languageDir: String = when (testSet.testFramework) {

            TestFramework.JUNIT -> "java"
            TestFramework.SPOCK -> "groovy"
        }
        val testDirPath = "src/${testSet.setName}/$languageDir"
        addDirectoryWithPackage(testDirPath)
        addDirectoryWithPackage("src/test/resources")
    }

    private fun basePackageAsPath(): String {
        return projectCreator.basePackage.replace(".", "/")
    }

    override fun projectCreator(creator: ProjectCreator) {
        projectCreator = creator
    }
}

class FileCreator(val file: File, val content: String) {
    fun create() {
        FileUtils.write(file, content)
    }
}