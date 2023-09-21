package dev.rubentxu.policies.interfaces

import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.PolicyRule
import dev.rubentxu.validations.ResultValidation

import java.nio.file.Path


interface IPoliciesManager {

    List<PolicyRule> parseTopicPoliciesSpecs(Path path)

    List<ResultValidation> applyPoliciesToInputModel(Path policiesFilePath, String typeInputModel, Path pathInputModel)

    List<ResultValidation> applyPoliciesToInputModel(Path policiesFilePath, InputModel inputModel)
}
