package dev.rubentxu.policies

import groovy.transform.Canonical

@Canonical
class ValidationExceptions {
    String topicName
    List<ReferencesResolver> validations

    @Override
    String toString() {
        return "ValidationExceptions: topicName: ${topicName}, validations: ${validations}"
    }
}
