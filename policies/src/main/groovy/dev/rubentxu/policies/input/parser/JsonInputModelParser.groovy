package dev.rubentxu.policies.input.parser

import dev.rubentxu.executors.IStepsExecutor
import dev.rubentxu.executors.StepsExecutor
import dev.rubentxu.policies.input.InputModel
import groovy.transform.Canonical
import groovy.util.logging.Log

import java.nio.file.Path

@Canonical
@Log
class JsonInputModelParser implements InputModelParser {

    private IStepsExecutor steps

    @Override
    List<InputModel> parseFiles(List<Path> files) {
        log.info("Parsing input Model from Json files ${files.collect { it.toString() }.join(', ')}")
        List<InputModel> inputModelList = []
        for (final def it in files) {

            log.info("Parsing input Model from Json file ${it.toString()}")
            assert steps.fileExists(it): "File Json not found in path: ${it}"
            InputModel data = steps.readJSON(it, 'UTF-8') as InputModel
            inputModelList.add(data)
        }

        log.info("Tables parsed :: ${inputModelList.collect { it.name }.join(', ')}")
        return inputModelList
    }
}
