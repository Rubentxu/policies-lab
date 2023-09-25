package dev.rubentxu.policies.result

import dev.rubentxu.policies.InputModel
import groovy.transform.Canonical

@Canonical
class MutationOutcome implements PolicyOutcome {
    String key
    List<String> errors
    InputModel oldInputModel
    InputModel newInputModel
    Map metadata = [:]
    List<String> differences



    @Override
    void merge(PolicyOutcome policyOutcome) {
        if (policyOutcome instanceof MutationOutcome) {
            merge((MutationOutcome) policyOutcome)
        }
    }

    void merge(MutationOutcome outcome) {

    }


    @Override
    Map toMap() {
        return [
                key     : key,
                differences : differences,
                errors  : errors,
                metadata: metadata
        ]
    }


}
