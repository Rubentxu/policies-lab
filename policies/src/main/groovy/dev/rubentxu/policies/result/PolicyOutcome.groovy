package dev.rubentxu.policies.result

sealed interface PolicyOutcome permits ValidationOutcome, MutationOutcome {

    Map toMap()

    Map getMetadata()

    void  merge(PolicyOutcome policyOutcome)
}
