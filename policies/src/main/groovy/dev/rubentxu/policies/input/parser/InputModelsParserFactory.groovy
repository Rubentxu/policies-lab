package dev.rubentxu.policies.input.parser

import dev.rubentxu.executors.IStepsExecutor
import dev.rubentxu.executors.StepsExecutor
import dev.rubentxu.policies.ParserType
import groovy.transform.Canonical
import groovy.util.logging.Log



@Log
@Canonical
class InputModelsParserFactory {

    IStepsExecutor steps

    InputModelParser getParser(ParserType type) {
        switch (type) {
            case ParserType.JSON:
                return new JsonInputModelParser(steps: steps)
            case ParserType.YAML:
                return new YamlInputModelParser(steps: steps)
            case ParserType.CSV:
                return new CSVInputModelParser(steps: steps)
            default:
                throw new IllegalArgumentException("Invalid type: $type")
        }
    }
}
