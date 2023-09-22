package dev.rubentxu.policies.parser

import dev.rubentxu.executors.StepsExecutor
import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.interfaces.IInputModelParser
import groovy.transform.Canonical
import groovy.util.logging.Log

import java.nio.file.Path

@Log
@Canonical
class CSVInputModelParser implements IInputModelParser {

    private StepsExecutor steps

    CSVInputModelParser(StepsExecutor steps) {
        this.steps = steps
    }

    @Override
    List<InputModel> parseFiles(List<Path> files) {
        log.info("Parsing input Model from CSV files ${files.collect { it.name }.join(', ')}")
        List<InputModel> inputModelList = []
        for (final def it in files) {
            log.info("Parsing input Model from CSV file ${it.name}")
            assert steps.fileExists(it): "File CSV not found in path: ${it.path}"
            List<InputModel> data = steps.readCSV(it) as List<InputModel>
            inputModelList.addAll(data)
        }

        log.info("Tables parsed :: ${inputModelList.collect { it.name }.join(', ')}")
        return inputModelList
    }
}
