package dev.rubentxu.validations

import dev.rubentxu.policies.result.ValidationOutcome
import spock.lang.Specification

class MapValidationSpec extends Specification {

    def "notNull should throw an exception when the map is null"() {
        given:
        def sut = null
        def validation = MapValidation.from(sut)

        when:
        def result =  validation.notNull()

        then:
        result.isValid() == false
    }

    def "notNull should return the same instance of MapValidation when the map is not null"() {
        given:
        def sut = [:]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.notNull()

        then:
        result == validation
    }

    def "getKey should throw an exception when the key is not present in the map"() {
        given:
        def sut = [:]
        def validation = MapValidation.from(sut)

        when:
        ValidationOutcome result =  validation.getKey("key") .getResult()

        then:
        result.errors[0] == "There is no configuration defined for key 'key'"
    }

    def "getKey should return the same instance of MapValidation when the key is present in the map"() {
        given:
        def sut = [key: "value"]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key")

        then:
        result == validation
    }

    def "getValue should return the resolved value of the map"() {
        given:
        def sut = [key: "value"]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == "value"
    }

    def "getValue should return null when the map is null"() {
        given:
        def sut = null
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getValue()

        then:
        result == null
    }

    def "getValue should return null when the key is not present in the map"() {
        given:
        def sut = [:]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == null
    }

    def "getValue should return the trimmed value when the resolved value is a string"() {
        given:
        def sut = [key: " value "]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == "value"
    }

    def "getValue should return the trimmed value when the resolved value is a GString"() {
        given:
        def sut = [key: " ${'value'} "]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == "value"
    }

    def "getValue should return the resolved value when the resolved value is not a string or a GString"() {
        given:
        def sut = [key: 1]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == 1
    }

    def "from should return a new instance of MapValidation"() {
        given:
        def sut = [:]

        when:
        def result = MapValidation.from(sut)

        then:
        result instanceof MapValidation
    }

    def "from should set the sut property to the provided map"() {
        given:
        def sut = [:]

        when:
        def result = MapValidation.from(sut)

        then:
        result.sut == sut
    }

    def "from should set the tag property to null by default"() {
        given:
        Map sut = [:]

        when:
        def result = MapValidation.from(sut)

        then:
        result.tag == ''
    }

    def "from should set the tag property to the provided tag"() {
        given:
        def sut = [:]
        def tag = "tag"

        when:
        def result = MapValidation.from(sut, tag)

        then:
        result.tag == tag
    }

    def "from should set the enableNullCheck property to true by default"() {
        given:
        def sut = [:]

        when:
        def result = MapValidation.from(sut)

        then:
        result.enableNullCheck == true
    }

    def "from should set the enableNullCheck property to the provided value"() {
        given:
        def sut = [:]
        def enableNullCheck = false

        when:
        def result = MapValidation.from(sut, enableNullCheck)

        then:
        result.enableNullCheck == enableNullCheck
    }

    def "getKey should set the resolvedValue property to the value of the key in the map"() {
        given:
        def sut = [key: "value"]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key")

        then:
        result.resolvedValue == "value"
    }

    def "getKey should set the resolvedValue property to null when the key is not present in the map"() {
        given:
        def sut = [:]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key")

        then:
        result.resolvedValue == null
    }

    def "getKey should return the same instance of MapValidation"() {
        given:
        def sut = [key: "value"]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key")

        then:
        result == validation
    }

    def "getKey should return a new instance of MapValidation when the key is not present in the map"() {
        given:
        def sut = [:]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getResult()

        then:
        result.errors[0] == "There is no configuration defined for key 'key'"
    }

    def "getKey should return a new instance of MapValidation when the key is present in the map but the resolved value is null"() {
        given:
        def sut = [key: null]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == null
    }

    def "getKey should return a new instance of MapValidation when the key is present in the map but the resolved value is an empty string"() {
        given:
        def sut = [key: ""]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == ''
    }

    def "getKey should return a new instance of MapValidation when the key is present in the map but the resolved value is a empty string"() {
        given:
        def sut = [key: " "]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == ""
    }

    def "getKey should return a new instance of MapValidation when the key is present in the map but the resolved value is an empty list"() {
        given:
        def sut = [key: []]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == []
    }

    def "getKey should return a new instance of MapValidation when the key is present in the map but the resolved value is an empty map"() {
        given:
        def sut = [key: [:]]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == [:]
    }


    def "getKey should return a new instance of MapValidation when the key is present in the map but the resolved value is a zero number"() {
        given:
        def sut = [key: 0]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == 0
    }

    def "getKey should return a new instance of MapValidation when the key is present in the map but the resolved value is a false boolean"() {
        given:
        def sut = [key: false]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == false
    }

    def "getKey should return a new instance of MapValidation when the key is present in the map but the resolved value is a null boolean"() {
        given:
        def sut = [key: null as Boolean]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == null
    }

    def "getKey should return a new instance of MapValidation when the key is present in the map but the resolved value is a null number"() {
        given:
        def sut = [key: null as Integer]
        def validation = MapValidation.from(sut)

        when:
        def result = validation.getKey("key").getValue()

        then:
        result == null
    }



}
