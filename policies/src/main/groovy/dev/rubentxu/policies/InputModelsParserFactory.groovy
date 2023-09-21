package dev.rubentxu.policies

import dev.rubentxu.StepsExecutor
import dev.rubentxu.policies.interfaces.IInputModelParser
import dev.rubentxu.policies.parser.CSVInputModelParser
import dev.rubentxu.policies.parser.JsonInputModelParser
import dev.rubentxu.policies.parser.YamlInputModelParser
import groovy.util.logging.Log



@Log
class InputModelsParserFactory {

    StepsExecutor steps

    InputModelsParserFactory(StepsExecutor steps) {
        this.steps = steps
    }

    IInputModelParser getParser(String type) {
        switch (type) {
            case "json":
                return new JsonInputModelParser(steps)
            case "yaml":
                return new YamlInputModelParser(steps)
            case "csv":
                return new CSVInputModelParser(steps)
            default:
                throw new IllegalArgumentException("Invalid type: $type")
        }
    }
}
