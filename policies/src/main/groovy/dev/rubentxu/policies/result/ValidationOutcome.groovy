package dev.rubentxu.policies.result


class ValidationOutcome implements PolicyOutcome {
    String key
    Boolean isValid
    List<String> errors
    Map metadata = [:]


    @Override
    void merge(PolicyOutcome policyOutcome) {
        if (policyOutcome instanceof ValidationOutcome) {
            merge((ValidationOutcome) policyOutcome)
        }
    }

    void merge(ValidationOutcome outcome) {
        isValid = isValid && outcome.isValid
        errors.addAll(outcome.errors)
        if (outcome?.metadata) {
            metadata.putAll(outcome.metadata)
        }
    }

    @Override
    String toString() {
        return "ResultValidation: isValid: ${isValid}, errors: ${errors.join(', ')}"
    }

    @Override
    Map toMap() {
        return [
                key     : key,
                isValid : isValid,
                errors  : errors,
                metadata: metadata
        ]
    }

}
