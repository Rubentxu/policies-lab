package dev.rubentxu.policies.rules.operation

import dev.rubentxu.policies.ExpressionEvaluator
import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.result.PolicyOutcome
import dev.rubentxu.policies.result.ValidationOutcome
import dev.rubentxu.validations.ValidatorCategory
import groovy.transform.Canonical

@Canonical
final class Predicate implements RuleOperation {
    String key
    List<String> expressions
    String effect

    @Override
    PolicyOutcome execute(InputModel inputModel) {
        PolicyOutcome policyOutcome = new ValidationOutcome(
                key: key,
                isValid: effect == 'Allow',
                errors: [],
        )

        expressions.any { String expression ->
            try {
                use(ValidatorCategory) {
                    ValidationOutcome validation = inputModel.validateReferenceResolver(key)
                            .withExpression(expression)
                            .getResult()

                    policyOutcome.merge(validation)
                }
            } catch (Exception e) {
                policyOutcome.isValid = false
                 policyOutcome.errors.add(e.message)
            }
        }
        return policyOutcome

    }
}
