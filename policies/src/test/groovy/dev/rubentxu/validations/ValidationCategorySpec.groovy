package dev.rubentxu.validations

import spock.lang.Specification

class ValidationCategorySpec extends Specification {

    def "validate a non-null string"() {
        given:
        def string = "hello world"

        when:
        def result = ValidationCategory.validate(string)

        then:
        result instanceof StringValidation
        result.getValue() == string
    }

    def "validate a null string"() {
        given:
        def string = null

        when:
        def result = ValidationCategory.validate(string)

        then:
        result.getValue() == null
    }

    def "validate a non-null string with tag"() {
        given:
        def string = "hello world"
        def tag = "myTag"

        when:
        def result = ValidationCategory.validate(string, tag)

        then:
        result instanceof StringValidation
        result.getValue() == string

    }

    def "validate a non-null number"() {
        given:
        def number = 42

        when:
        def result = ValidationCategory.validate(number)

        then:
        result instanceof NumberValidation
        result.getValue() == number
    }

    def "validate a null number"() {
        given:
        def number = null

        when:
        def result = ValidationCategory.validate(number)

        then:
        result.getValue() == null
    }

    def "validate a non-null number with tag"() {
        given:
        def number = 42
        def tag = "myTag"

        when:
        def result = ValidationCategory.validate(number, tag)

        then:
        result instanceof NumberValidation
        result.getValue() == number
    }

    def "validate a non-null list"() {
        given:
        def list = [1, 2, 3]

        when:
        def result = ValidationCategory.validate(list)

        then:
        result instanceof CollectionValidation
        result.getValue() == list
    }

    def "validate a null list"() {
        given:
        def list = null

        when:
        def result = ValidationCategory.validate(list)

        then:
        result.getValue() == null
    }

    def "validate a non-null list with tag"() {
        given:
        def list = [1, 2, 3]
        def tag = "myTag"

        when:
        def result = ValidationCategory.validate(list, tag)

        then:
        result instanceof CollectionValidation
        result.getValue() == list

    }

    def "validate a non-null map"() {
        given:
        def map = [name: "John", age: 30]

        when:
        def result = ValidationCategory.validate(map)

        then:
        result instanceof MapValidation
        result.getValue() == map
    }

    def "validate a null map"() {
        given:
        def map = null

        when:
        def result = ValidationCategory.validate(map)

        then:
        result.getValue() == null
    }

    def "validate a non-null map with tag"() {
        given:
        def map = [name: "John", age: 30]
        def tag = "myTag"

        when:
        def result = ValidationCategory.validate(map, tag)

        then:
        result instanceof MapValidation
        result.getValue() == map

    }

    def "validate and get a non-null map with tag"() {
        given:
        def map = [person: [name: "John", age: 30]]
        def tag = "person"

        when:
        def result = ValidationCategory.validateAndGet(map, tag)

        then:
        result instanceof MapValidation
        result.getValue() == [name: "John", age: 30]
    }

    def "validate and get a non-null list with tag"() {
        given:
        def map = [people: [[name: "John", age: 30], [name: "Jane", age: 25]]]
        def tag = "people"

        when:
        def result = ValidationCategory.validateAndGet(map, tag)

        then:
        result instanceof CollectionValidation
        result.getValue() == [[name: "John", age: 30], [name: "Jane", age: 25]]
    }

    def "validate and get a non-null string with tag"() {
        given:
        def map = [greeting: "hello world"]
        def tag = "greeting"

        when:
        def result = ValidationCategory.validateAndGet(map, tag)

        then:
        result instanceof StringValidation
        result.getValue() == "hello world"
    }

    def "validate and get a non-null number with tag"() {
        given:
        def map = [age: 30]
        def tag = "age"

        when:
        def result = ValidationCategory.validateAndGet(map, tag)

        then:
        result instanceof NumberValidation
        result.getValue() == 30
    }

    def "validate a non-null object"() {
        given:
        def object = new Object()

        when:
        def result = ValidationCategory.validate(object)

        then:
        result instanceof Validation
        result.getValue() == object
    }

    def "validate a null object"() {
        given:
        def object = null

        when:
        def result = ValidationCategory.validate(object)

        then:
        result instanceof Validation
        result.getValue() == null
    }

    def "validate a non-null object with tag"() {
        given:
        def object = new Object()
        def tag = "myTag"

        when:
        def result = ValidationCategory.validate(object, tag)

        then:
        result instanceof Validation
        result.getValue() == object

    }

    def "validate a null object with tag"() {
        given:
        def object = null
        def tag = "myTag"

        when:
        def result = ValidationCategory.validate(object, tag)

        then:
        result instanceof Validation
        result.getValue() == null

    }

    def "validate a null object with tag and null check disabled"() {
        given:
        def object = null
        def tag = "myTag"

        when:
        def result = ValidationCategory.validateNull(object, tag, false)

        then:
        result instanceof Validation
        result.getValue() == null

    }

}