package dev.rubentxu.validations

import dev.rubentxu.StepsExecutorMock
import dev.rubentxu.executors.IStepsExecutor
import dev.rubentxu.policies.PoliciesManager
import dev.rubentxu.policies.input.parser.InputModelsParserFactory
import dev.rubentxu.policies.rules.PoliciesParserFactory
import spock.lang.Specification

class ValidationSpec extends Specification {

    def setup() {
        Locale.setDefault(new Locale("en", "US"))
    }

    def "test notNull() with non-null value"() {
        given:
        def sut = "hello"
        def validation = StringValidation.from(sut)

        when:
        validation.notNull()

        then:
        validation.isValid() == true
    }

    def "test notNull() with null value"() {
        given:
        def sut = null
        def validation = StringValidation.from(sut)

        when:
        validation.isString()

        then:
        validation.isValid() == false
        validation.onErrorMessages.size() == 2
        validation.onErrorMessages[0] == "Variable with value null Must not be null"
        validation.onErrorMessages[1] == "Variable with value null Must be type String."
    }

    def "test test() with valid predicate"() {
        given:
        def sut = 5
        def validation = NumberValidation.from(sut)

        when:
        validation.test("sut must be greater than 3", { it > 3 })

        then:
        validation.isValid() == true
    }

    def "test test() with invalid predicate"() {
        given:
        def sut = 2
        def validation = NumberValidation.from(sut)

        when:
        validation.test("sut must be greater than 3", { it > 3 })

        then:
        validation.isValid() == false
        validation.onErrorMessages.size() == 1
        validation.onErrorMessages[0] == "sut must be greater than 3"
    }

    def "test throwIfInvalid() with valid validation"() {
        given:
        def sut = "hello"
        def validation = StringValidation.from(sut)

        when:
        def result = validation.throwIfInvalid()

        then:
        result == sut
    }

    def "test with invalid validation"() {
        when:
        def sut = null
        def validation = StringValidation.from(sut)

        then:
        validation.isValid() == false
    }

    def "test defaultValueIfInvalid() with valid validation"() {
        given:
        def sut = "hello"
        def validation = StringValidation.from(sut)

        when:
        def result = validation.defaultValueIfInvalid("default")

        then:
        result == sut
    }

    def "test defaultValueIfInvalid() with invalid validation"() {
        given:
        def sut = null
        def validation = StringValidation.from(sut)

        when:
        def result = validation.defaultValueIfInvalid("default")

        then:
        result == "default"
    }
}