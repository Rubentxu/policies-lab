package dev.rubentxu.validations

import spock.lang.Specification

class ResultValidationSpec extends Specification {

    def "merge should combine the results of two ResultValidation objects"() {
        given:
        def resultValidation1 = new ResultValidation(isValid: true, errors: ["error1"], metadata: [key1: "value1"])
        def resultValidation2 = new ResultValidation(isValid: false, errors: ["error2"], metadata: [key2: "value2"])

        when:
        resultValidation1.merge(resultValidation2)

        then:
        resultValidation1.isValid == false
        resultValidation1.errors == ["error1", "error2"]
        resultValidation1.metadata == [key1: "value1", key2: "value2"]
    }

    def "toString should return a string representation of the ResultValidation object"() {
        given:
        def resultValidation = new ResultValidation(isValid: true, errors: ["error1", "error2"], metadata: [key1: "value1", key2: "value2"])

        when:
        def result = resultValidation.toString()

        then:
        result == "ResultValidation: isValid: true, errors: error1, error2"
    }

    def "toMap should return a map representation of the ResultValidation object"() {
        given:
        def resultValidation = new ResultValidation(isValid: true, errors: ["error1", "error2"], metadata: [key1: "value1", key2: "value2"])

        when:
        def result = resultValidation.toMap()

        then:
        result == [isValid: true, errors: ["error1", "error2"], metadata: [key1: "value1", key2: "value2"]]
    }
}
