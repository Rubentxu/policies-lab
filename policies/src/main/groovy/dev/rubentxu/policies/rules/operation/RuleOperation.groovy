package dev.rubentxu.policies.rules.operation

import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.result.PolicyOutcome

sealed interface RuleOperation permits Add, Modify, Remove, Predicate {
    PolicyOutcome execute(InputModel inputModel)
}
