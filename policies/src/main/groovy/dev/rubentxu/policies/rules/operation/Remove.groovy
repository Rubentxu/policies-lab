package dev.rubentxu.policies.rules.operation

import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.result.PolicyOutcome

final class Remove implements RuleOperation {
    String key

    @Override
    PolicyOutcome execute(InputModel inputModel) {
        if(key.contains('.')) {
            recursiveRemove(inputModel, key.split('\\.'))
        }
        inputModel.remove(key)
    }

    private void recursiveRemove(Map<String, Object> inputModel, String[] keys) {
        if(keys.size() == 1) {
            inputModel.remove(keys[0])
        } else {
            if(inputModel.containsKey(keys[0])) {
                recursiveRemove(inputModel.get(keys[0]), keys[1..-1])
            }
        }
    }
}