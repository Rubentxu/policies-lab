package dev.rubentxu.policies.input.parser

import dev.rubentxu.executors.IStepsExecutor
import dev.rubentxu.executors.StepsExecutor
import dev.rubentxu.policies.input.InputModel
import groovy.transform.Canonical
import groovy.util.logging.Log

import java.nio.file.Path

@Log
@Canonical
class CSVInputModelParser implements InputModelParser {

    IStepsExecutor steps

    @Override
    List<InputModel> parseFiles(List<Path> files) {
        log.info("Parsing input Model from CSV files ${files.collect { it.toString() }.join(', ')}")
        List<InputModel> inputModelList = []
        for (final def it in files) {
            log.info("Parsing input Model from CSV file ${it.toString()}")
            assert steps.fileExists(it): "File CSV not found in path: ${it.toString()}"
            List<InputModel> data = steps.readCSV(it, 'UTF-8') as List<InputModel>
            inputModelList.addAll(data)
        }

        log.info("Tables parsed :: ${inputModelList.collect { it.name }.join(', ')}")
        return inputModelList
    }
}
