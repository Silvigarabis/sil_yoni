package io.github.silvigarabis

import groovy.json.JsonOutput
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import org.yaml.snakeyaml.Yaml

import java.nio.file.Path

@CacheableTask
abstract class YamlToJsonTask extends DefaultTask {

    @Incremental
    @InputFiles
    @PathSensitive(PathSensitivity.RELATIVE)
    abstract ConfigurableFileCollection getInputFiles()

    @OutputDirectory
    abstract DirectoryProperty getOutputDirectory()

    @TaskAction
    void convert(InputChanges changes) {
        Yaml yaml = new Yaml()

        changes.getFileChanges(inputFiles).each { change ->

            if (change.file.directory) {
                return
            }

            def inputPath = Path.of(change.normalizedPath)

            def outParentPath = inputPath.getParent()
            def outName = inputPath.getFileName()
                    .toString().replaceAll("\\.ya?ml\$", ".json")

            def outPath = outParentPath == null ? Path.of(outName) : outParentPath.resolve(outName)

            def output = outputDirectory.file(outPath.toString())
            File outputFile = output.get().asFile

            switch (change.changeType) {
                case ChangeType.ADDED:
                case ChangeType.MODIFIED:
                    println "[CONVERT] ${inputPath} -> ${outPath}"
                    convertFile(yaml, change.file, outputFile)
                    break

                case ChangeType.REMOVED:
                    println "[DELETE] ${inputPath} -> ${outPath}"
//                    if (outputFile.exists()){
//                        outputFile.delete()
//                    }
                    break
            }
        }
    }

    static void convertFile(Yaml yaml, File input, File output) {
        def data = yaml.load(input.getText("UTF-8"))

        if (data instanceof Map) {
            data = data.findAll {
                !it.key.toString().startsWith("x-")
            }
        }

        output.parentFile.mkdirs()
        output.text = JsonOutput.toJson(data)
    }
}