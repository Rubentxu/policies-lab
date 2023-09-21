package dev.rubentxu.policies.parser

import dev.rubentxu.StepsExecutor
import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.interfaces.IInputModelParser
import groovy.transform.Canonical
import groovy.util.logging.Log

import java.nio.file.Path

@Canonical
@Log
class JsonInputModelParser implements IInputModelParser {

    private StepsExecutor steps


    @Override
    List<InputModel> parseFiles(List<Path> files) {
        log.info("Parsing input Model from Json files ${files.collect { it.name }.join(', ')}")
        List<InputModel> inputModelList = []
        for (final def it in files) {

            log.info("Parsing input Model from Json file ${it.name}")
            assert steps.fileExists(it): "File Json not found in path: ${it}"
            InputModel data = steps.readJSON(it, 'UTF-8') as InputModel
            inputModelList.add(data)
        }

        log.info("Tables parsed :: ${inputModelList.collect { it.name }.join(', ')}")
        return inputModelList
    }
}
