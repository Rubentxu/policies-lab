package dev.rubentxu.validations

import groovy.transform.Immutable

@Immutable
class ValidationResult {
    boolean isValid
    String errorMessage

}
