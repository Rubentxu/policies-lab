package dev.rubentxu.policies

import dev.rubentxu.executors.IStepsExecutor
import dev.rubentxu.executors.StepsExecutor
import dev.rubentxu.policies.input.parser.InputModelParser
import dev.rubentxu.policies.input.parser.InputModelsParserFactory
import dev.rubentxu.policies.result.PolicyOutcome
import dev.rubentxu.policies.rules.PoliciesParserFactory
import dev.rubentxu.policies.rules.PoliciesParser
import groovy.transform.Canonical
import groovy.util.logging.Log

import java.nio.file.Path


@Log
@Canonical
class PoliciesManager  {

    InputModelsParserFactory factory
    PoliciesParserFactory policiesParserFactory
    IStepsExecutor steps


    List<Policy> parseTopicPoliciesSpecs(Path path) {
        log.info("Parsing topic policies specs from path: ${path}")
        PoliciesParser parser =  policiesParserFactory.getParser(ParserType.YAML)
        return parser.parseFile(path)

    }


    List<PolicyOutcome> applyPoliciesToInputModel(Path policiesFilePath, ParserType typeInputModel, Path pathInputModel) {
        InputModelParser parser = factory.getParser(typeInputModel)
        List<InputModel> inputModels = parser.parseFiles(steps.findFiles(pathInputModel,"**/${pathInputModel}/*.${typeInputModel}"))

        List<Policy> policyRules = parseTopicPoliciesSpecs(policiesFilePath)

        List<PolicyOutcome> resultValidations = []

        for (final Policy policyRule in policyRules) {
            for(final InputModel input in inputModels) {
                List<PolicyOutcome> result = policyRule.resolve(input)
                resultValidations.addAll(result)
            }
        }
        return resultValidations
    }

}
