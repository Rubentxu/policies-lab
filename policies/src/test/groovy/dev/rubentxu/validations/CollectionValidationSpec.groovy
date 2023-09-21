package dev.rubentxu.validations

import spock.lang.Specification

class CollectionValidationSpec extends Specification {

    def "should validate that a collection is empty"() {
        given:
        def collection = []

        when:
        def result = CollectionValidation.from(collection).isEmpty().isValid()

        then:
        result == true
    }

    def "should validate that a collection is not empty"() {
        given:
        def collection = [1, 2, 3]

        when:
        def result = CollectionValidation.from(collection).notEmpty().isValid()

        then:
        result == true
    }

    def "should validate that a collection contains any element"() {
        given:
        def collection = [1, 2, 3]

        when:
        def result = CollectionValidation.from(collection).containsAny(2).isValid()

        then:
        result == true
    }

    def "should validate that a collection contains all elements"() {
        given:
        def collection = [1, 2, 3]

        when:
        def result = CollectionValidation.from(collection).containsAll(1).isValid()

        then:
        result == false
    }

    def "should validate that a collection has a size lower than a given number"() {
        given:
        def collection = [1, 2, 3]

        when:
        def result = CollectionValidation.from(collection).lowerThan(4).isValid()

        then:
        result == true
    }

    def "should validate that a collection has a size greater than a given number"() {
        given:
        def collection = [1, 2, 3]

        when:
        def result = CollectionValidation.from(collection).greaterThan(2).isValid()

        then:
        result == true
    }

    def "should validate that a collection has a size between two given numbers"() {
        given:
        def collection = [1, 2, 3]

        when:
        def result = CollectionValidation.from(collection).between(2, 4).isValid()

        then:
        result == true
    }
}
