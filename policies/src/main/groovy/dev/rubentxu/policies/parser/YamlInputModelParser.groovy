package dev.rubentxu.policies.parser

import dev.rubentxu.StepsExecutor
import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.interfaces.IInputModelParser
import groovy.transform.Canonical
import groovy.util.logging.Log

import java.nio.file.Path

@Log
@Canonical
class YamlInputModelParser implements IInputModelParser {

    private StepsExecutor steps

    YamlInputModelParser(StepsExecutor steps) {
        this.steps = steps
    }

    @Override
    List<InputModel> parseFiles(List<Path> files) {
        log.info("Parsing input Model from readme files ${files.collect { it.name }.join(', ')}")

        List<InputModel> inputModelList = []
        for (final def it in files) {

            log.info("Parsing input Model from Yaml file ${it.name}")
            assert steps.fileExists(it.path): "File Yaml not found in path: ${it.path}"
            InputModel data = steps.readYaml(it, 'UTF-8') as InputModel
            inputModelList.add(data)
        }

        log.info("Tables parsed :: ${inputModelList.collect { it.name }.join(', ')}")
        return inputModelList
    }
}
