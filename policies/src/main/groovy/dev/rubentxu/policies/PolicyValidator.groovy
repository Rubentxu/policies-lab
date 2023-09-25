package dev.rubentxu.policies

import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.Policy
import dev.rubentxu.policies.result.PolicyOutcome
import groovy.util.logging.Log

@Log
class PolicyValidator  {

    List<PolicyOutcome> validate(InputModel inputModel, List<Policy> policies) {
        log.info("Validating manifests...")

        // Iteramos sobre las políticas
        for (Policy policy in policies) {

            // Verificamos si la política se aplica al manifiesto
            if (!policy.appliesTo(inputModel)) {
                continue
            }

            // Verificamos si la política se cumple
            List<PolicyOutcome> resultValidation = policy.resolve(inputModel)
            return resultValidation

        }
    }

}