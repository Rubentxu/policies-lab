package dev.rubentxu.policies.rules

import dev.rubentxu.executors.StepsExecutor
import dev.rubentxu.policies.ParserType
import dev.rubentxu.policies.parser.YamlPolicyParser
import groovy.transform.Canonical
import groovy.util.logging.Log

@Log
@Canonical
class PoliciesParserFactory {

    private StepsExecutor steps

    PoliciesParser getParser(ParserType type) {
        switch (type) {
            case ParserType.YAML:
                return new YamlPolicyParser(steps)
            default:
                throw new IllegalArgumentException("Invalid type: $type")
        }
    }
}
