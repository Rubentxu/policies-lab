package dev.rubentxu.policies

import spock.lang.Specification

class ReferencesResolverSpec extends Specification {

    def "resolveExpression should replace value references with actual values"() {
        given:
        def inputModel = new InputModel(person: [name: "John", age: 30])
        def referencesResolver = new ReferencesResolver(expression: 'Hello $person.name, you are ${person.age} years old')

        when:
        def resolvedExpression = referencesResolver.resolve(inputModel)

        then:
        resolvedExpression == "Hello John, you are 30 years old"
    }

    def "resolveExpression should return the original expression if no value references are present"() {
        given:
        def inputModel = new InputModel(name: "John", age: 30)
        def referencesResolver = new ReferencesResolver(expression: "Hello world")

        when:
        def resolvedExpression = referencesResolver.resolve(inputModel)

        then:
        def ex = thrown(RuntimeException)
        ex instanceof RuntimeException
        ex.message.trim() == "Error evaluating expression: Hello world"
    }

    def "resolveMapExpression should resolve value references in map"() {
        given:
        def inputModel = new InputModel(person: [name: "John", age: 30])
        def referencesResolver = new ReferencesResolver(expression: 'person.name')

        when:
        def resolvedExpression = referencesResolver.resolve(inputModel)

        then:
        resolvedExpression == "John"
    }

    def "resolveMapExpression should error in value references in map "() {
        given:
        def inputModel = new InputModel(person2: [ age: 30])
        def referencesResolver = new ReferencesResolver(expression: 'person.name')

        when:
        def resolvedExpression = referencesResolver.resolve(inputModel)

        then:
        def ex = thrown(RuntimeException)
        ex instanceof RuntimeException
        ex.message.trim() == "Error evaluating expression: person.name No such property: person"
    }

    def "resolveMapExpression should with sigle key resolve value"() {
        given:
        def inputModel = new InputModel(person: [ age: 30])
        def referencesResolver = new ReferencesResolver(expression: 'person')

        when:
        def resolvedExpression = referencesResolver.resolve(inputModel)

        then:
        resolvedExpression == [ age: 30]
    }

    def "resolveMapExpression should with complex key resolve value"() {
        given:
        def inputModel = new InputModel(person: [ age: 30, tags: [ [name: "tag1"], [name: "tag2"] ]])
        def referencesResolver = new ReferencesResolver(expression: 'person.tags[0]')

        when:
        def resolvedExpression = referencesResolver.resolve(inputModel)

        then:
        resolvedExpression == [name:'tag1']
    }

    def "resolveMapExpression with complex expresion should error null in value references in map "() {
        given:
        def inputModel = new InputModel(person: [ age: 30])
        def referencesResolver = new ReferencesResolver(expression: 'Hola $person.name como estas')

        when:
        def resolvedExpression = referencesResolver.resolve(inputModel)

        then:
        resolvedExpression == "Hola null como estas"
    }

    def "resolveMapExpression should return the original expression if no value references are present"() {
        given:
        def inputModel = new InputModel(name: "John", age: 30)
        def referencesResolver = new ReferencesResolver(expression: "Hello world")

        when:
        referencesResolver.resolve(inputModel)

        then:
        def ex = thrown(RuntimeException)
        ex instanceof RuntimeException
        ex.message == "Error evaluating expression: Hello world"
    }

    def "replaceValueReference should replace non-existent value references with null"() {
        given:
        def inputModel = new InputModel(name: "John", age: 30)
        def referencesResolver = new ReferencesResolver(expression: 'Hello $name, you are $age years old, your address is $address')

        when:
        referencesResolver.renderTemplateString(inputModel)

        then:
        def ex = thrown(RuntimeException)
        ex instanceof RuntimeException
        ex.message == "Error rendering template string: 'Hello \$name, you are \$age years old, your address is \$address' No such property: address"
    }

    def "replaceValueReference should return the original expression if all value references exist"() {
        given:
        def inputModel = new InputModel(name: "John", age: 30)
        def referencesResolver = new ReferencesResolver(expression: 'Hello $name, you are $age years old')

        when:
        def resolvedExpression = referencesResolver.renderTemplateString(inputModel)

        then:
        resolvedExpression == "Hello John, you are 30 years old"
    }

    def "replaceValueReference should return the original expression if no value references are present"() {
        given:
        def inputModel = new InputModel(name: "John", age: 30)
        def referencesResolver = new ReferencesResolver(expression: "Hello world")

        when:
        def resolvedExpression = referencesResolver.renderTemplateString(inputModel)

        then:
        resolvedExpression == "Hello world"
    }

}
