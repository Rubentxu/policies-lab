package dev.rubentxu.policies.input.parser

import dev.rubentxu.executors.StepsExecutor
import dev.rubentxu.policies.input.InputModel
import groovy.transform.Canonical
import groovy.util.logging.Log

import java.nio.file.Path

@Log
@Canonical
class YamlInputModelParser implements InputModelParser {

    private StepsExecutor steps

    YamlInputModelParser(StepsExecutor steps) {
        this.steps = steps
    }

    @Override
    List<InputModel> parseFiles(List<Path> files) {
        log.info("Parsing input Model from readme files ${files.collect { it.toFile().name }.join(', ')}")

        List<InputModel> inputModelList = []
        for (final def it in files) {

            log.info("Parsing input Model from Yaml file ${it.toFile().name}")
            assert steps.fileExists(it): "File Yaml not found in path: ${it.toString()}"
            InputModel data = steps.readYaml(it, 'UTF-8') as InputModel
            inputModelList.add(data)
        }

        log.info("Tables parsed :: ${inputModelList.collect { it.name }.join(', ')}")
        return inputModelList
    }
}
