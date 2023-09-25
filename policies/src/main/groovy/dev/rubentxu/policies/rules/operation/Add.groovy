package dev.rubentxu.policies.rules.operation

import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.result.PolicyOutcome

//@CompileStatic
final class Add implements RuleOperation {
    def value
    String key

    @Override
    PolicyOutcome execute(InputModel inputModel) {
        if(key.contains('.')) {
            recursiveAdd(inputModel, key.split('\\.'), value)
        }
        inputModel.put(key, value)

    }

    private def recursiveAdd(Map<String, Object> inputModel, String[] keys, Object value) {
        if(keys.size() == 1) {
            inputModel.put(keys[0], value)
        } else {
            if(!inputModel.containsKey(keys[0])) {
                inputModel.put(keys[0], [:])
            }
            recursiveAdd(inputModel.get(keys[0]), keys[1..-1], value)
        }
    }
}
