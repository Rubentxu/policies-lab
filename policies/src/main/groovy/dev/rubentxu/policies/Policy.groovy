package dev.rubentxu.policies

import dev.rubentxu.policies.result.PolicyOutcome
import dev.rubentxu.policies.rules.operation.RuleOperation
import groovy.transform.Canonical

@Canonical
class Policy {

    private String id
    private String effect
    private List<Condition> conditions
    private List<RuleOperation> rules


    boolean appliesTo(InputModel inputModel) {
        // Verificamos si la política se aplica al recurso
        if (conditions.isEmpty()) {
            return true
        }

        // Iteramos sobre las condiciones
        for (Condition condition in conditions) {

            // Verificamos si la condición se cumple
            Boolean resultCondition = condition.appliesTo(inputModel)
            if (!resultCondition) {
                return false
            }
        }

        // Todas las condiciones se cumplen
        return true
    }

    List<PolicyOutcome> resolve(InputModel inputModel) {
        // Iteramos sobre las reglas
        return rules.collect { rule ->
            rule.execute(inputModel)
        }

    }

    @Override
    String toString() {
        return "Policy{" +
                "id='" + id + '\'' +
                ", effect='" + effect + '\'' +
                ", conditions=" + conditions +
                ", rules=" + rules +
                '}'
    }
}
