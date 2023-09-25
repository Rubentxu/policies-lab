package dev.rubentxu.policies.rules.operation

import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.result.MutationOutcome
import dev.rubentxu.policies.result.PolicyOutcome

final class Modify implements RuleOperation {
    def value
    String key

    @Override
    PolicyOutcome execute(InputModel inputModel) {
        InputModel newInputModel = inputModel.clone()
        if(key.contains('.')) {
            recursiveModify(newInputModel, key.split('\\.'), value)
        }
        newInputModel.put(key, value)

        return new MutationOutcome(
            key: key,
            oldInputModel: inputModel,
            newInputModel: newInputModel,
            errors: [],
        )
    }

    private void recursiveModify(Map<String, Object> inputModel, String[] keys, Object value) {
        if(keys.size() == 1) {
            inputModel.put(keys[0], value)
        } else {
            if(inputModel.containsKey(keys[0])) {
                recursiveModify(inputModel.get(keys[0]), keys[1..-1], value)
            }
        }
    }
}
