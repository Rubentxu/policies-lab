package dev.rubentxu.policies

import dev.rubentxu.StepsExecutor
import dev.rubentxu.policies.interfaces.IInputModelParser
import dev.rubentxu.policies.interfaces.IPoliciesManager
import dev.rubentxu.validations.ResultValidation
import groovy.transform.Canonical
import groovy.util.logging.Log
import java.nio.file.Path


@Canonical
@Log
class PoliciesManager implements IPoliciesManager {

    StepsExecutor steps
    Map<String, List<PolicyRule>> rulesByPath
    InputModelsParserFactory factory

    @Override
    List<PolicyRule> parseTopicPoliciesSpecs(Path path) {
        log.info("Parsing topic policies specs from path: ${path}")
        assert steps.fileExists(path): "File Policies specs not found in path: ${path}"
        if (!rulesByPath.containsKey(path)) {
            List<PolicyRule> rules = []
            Map data = steps.readYaml(path, 'UTF-8')
            data?.policies_specs.each { key, value ->
                if (value.validations || value.exceptions) {
                    rules.add(
                            new PolicyRule(
                                    code: value.key,
                                    validations: value.validations.collect { new ReferencesResolver(expression: it) },
                                    exceptions: value.exceptions.collect {
                                        new ValidationExceptions(topicName: it.topicName,
                                                validations: it.validations.collect { new ReferencesResolver(expression: it) }
                                        )
                                    },
                            )
                    )
                }
            }
            rulesByPath.put(path, rules)
        }
        log.info("Parsed topic policies specs")
        return rulesByPath.get(path)

    }

    @Override
    List<ResultValidation> applyPoliciesToInputModel(Path policiesFilePath, String typeInputModel, Path pathInputModel) {
        IInputModelParser parser = factory.getParser(typeInputModel)
        List<InputModel> inputModels = parser.parseFiles(steps.findFiles(pathInputModel))

        List<PolicyRule> policyRules = parseTopicPoliciesSpecs(policiesFilePath)

        List<ResultValidation> resultValidations = []

        for (final PolicyRule policyRule in policyRules) {
            for (final InputModel input in inputModels) {
                ResultValidation result = policyRule.validate(input)
                resultValidations.add(result)
            }
        }
        return resultValidations
    }

    @Override
    List<ResultValidation> applyPoliciesToInputModel(Path policiesFilePath, InputModel inputModel) {
        List<ResultValidation> resultValidations = []
        List<PolicyRule> policyRules = parseTopicPoliciesSpecs(policiesFilePath)
        for (final PolicyRule policyRule in policyRules) {
            ResultValidation result = policyRule.validate(inputModel)
            resultValidations.add(result)
        }
        return resultValidations

    }

}
