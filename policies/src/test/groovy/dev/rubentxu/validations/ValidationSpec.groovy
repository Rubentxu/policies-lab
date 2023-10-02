package dev.rubentxu.validations


import spock.lang.Specification

class ValidationSpec extends Specification {

    def setup() {
        Locale.setDefault(new Locale("en", "US"))
    }

    def "test notNull() with non-null value"() {
        given:
        def sut = "hello"
        def validation = StringValidator.from(sut)

        when:
        validation.notNull()

        then:
        validation.isValid() == true
    }

    def "test notNull() with null value"() {
        given:
        def sut = null
        def validation = StringValidator.from(sut)

        when:
        validation.isString()

        then:
        validation.isValid() == false
        validation.validationResults.size() == 1

    }

    def "test test() with valid predicate"() {
        given:
        def sut = 5
        def validation = NumberValidator.from(sut)

        when:
        validation.test("sut must be greater than 3", { it > 3 })

        then:
        validation.isValid() == true
    }

    def "test test() with invalid predicate"() {
        given:
        def sut = 2
        def validation = NumberValidator.from(sut)

        when:
        validation.test("sut must be greater than 3", { it > 3 })

        then:
        validation.isValid() == false
        validation.validationResults.size() == 1
        validation.validationResults[0].errorMessage == "sut must be greater than 3"
    }

    def "test throwIfInvalid() with valid validation"() {
        given:
        def sut = "hello"
        def validation = StringValidator.from(sut)

        when:
        String result = validation.throwIfInvalid("Custom error message")



        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "No existen validaciones para el objeto. Custom error message"
    }

    def "test with invalid validation"() {
        when:
        def sut = null
        def validation = StringValidator.from(sut)

        then:
        validation.isValid() == false
    }

    def "test defaultValueIfInvalid() with valid validation"() {
        given:
        def sut = "hello"
        def validation = StringValidator.from(sut)

        when:
        def result = validation.defaultValueIfInvalid("default")

        then:
        result == "default"
    }

    def "test defaultValueIfInvalid() with invalid validation"() {
        given:
        def sut = null
        def validation = StringValidator.from(sut)

        when:
        def result = validation.defaultValueIfInvalid("default")

        then:
        result == "default"
    }
}