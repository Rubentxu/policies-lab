package dev.rubentxu.policies


import dev.rubentxu.policies.rules.operation.RuleOperation
import groovy.transform.Canonical

@Canonical
class Rule {
    private RuleType type
    private String error
    private RuleOperation operation

    boolean appliesTo(InputModel inputModel) {
        operation.execute(inputModel)
    }


}
