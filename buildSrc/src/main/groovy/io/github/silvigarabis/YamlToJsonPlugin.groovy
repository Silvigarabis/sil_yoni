package io.github.silvigarabis

import org.gradle.api.Plugin
import org.gradle.api.Project

class YamlToJsonPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        def task = project.tasks.register("convertYaml", YamlToJsonTask) {
            group = "other"
            description = "Process YAML files in main resources to JSON."

            inputFiles.from(
                    project.fileTree("src/main/resources") {
                        include("**/*.yml")
                        include("**/*.yaml")
                    }
            )

            outputDirectory.set(
                    project.layout.buildDirectory.dir("generated/yamlConverted")
            )
        }

        project.tasks.named("processResources") {

            dependsOn(task)

            exclude("**/*.yml")
            exclude("**/*.yaml")

            from(task)
        }

    }

}