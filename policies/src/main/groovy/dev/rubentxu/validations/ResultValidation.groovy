package dev.rubentxu.validations


class ResultValidation {
    Boolean isValid
    List<String> errors
    Map metadata = [:]


    def merge(ResultValidation resultValidation) {
        isValid = isValid && resultValidation.isValid
        errors.addAll(resultValidation.errors)
        if (resultValidation?.metadata) {
            metadata.putAll(resultValidation.metadata)
        }
    }


    @Override
    String toString() {
        return "ResultValidation: isValid: ${isValid}, errors: ${errors.join(', ')}"
    }

    Map toMap() {
        return [
                isValid : isValid,
                errors  : errors,
                metadata: metadata
        ]
    }
}
