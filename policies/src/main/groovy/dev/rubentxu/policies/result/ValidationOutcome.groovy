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
        this.key = outcome.key
        this.isValid = isValid && outcome.isValid
        this.errors.addAll(outcome.errors)
        if (outcome?.metadata) {
            this.metadata.putAll(outcome.metadata)
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
