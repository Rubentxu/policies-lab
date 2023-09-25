package dev.rubentxu.policies

import spock.lang.Specification

class ExpressionEvaluatorSpec extends Specification {

    def "evaluate simple expression"() {
        given:
        def inputModel = new InputModel(name: "John", age: 30)

        when:
        def result = ExpressionEvaluator.evaluate(inputModel, "age > 18")

        then:
        result == true
    }

    def "evaluate complex expression"() {
        given:
        def inputModel = new InputModel(name: "John", age: 30, address: [city: "New York", country: "USA"])

        when:
        def result = ExpressionEvaluator.evaluate(inputModel, "age > 18 && address.city == 'New York'")

        then:
        result == true
    }

    def "evaluate expression with template string"() {
        given:
        def inputModel = new InputModel(name: "John", age: 30)

        when:
        def result = ExpressionEvaluator.evaluate(inputModel, 'Hello, ${name}! You are ${age} years old.')

        then:
        result == "Hello, John! You are 30 years old."
    }

    def "evaluate expression with invalid syntax"() {
        given:
        def inputModel = new InputModel(name: "John", age: 30)

        when:
        ExpressionEvaluator.evaluate(inputModel, "age >")

        then:
        thrown(RuntimeException)
    }

    def "resolve comparison expression"() {
        expect:
        ExpressionEvaluator.resolveComparisonExpression("10cm > 5cm") == "(10 > 5 && 'cm' == 'cm')"
    }

    def "split expression into subexpressions"() {
        expect:
        ExpressionEvaluator.splitIntoSubExpressions("age > 18 && address.city == 'New York'") == ["age > 18", "address.city == 'New York'"]
    }

    def "find logical operators in expression"() {
        expect:
        ExpressionEvaluator.findLogicalOperators("age > 18 && address.city == 'New York'") == ["&&"]
    }

    def "combine subexpressions and operators into a list"() {
        expect:
        ExpressionEvaluator.combineSubExpressionsAndOperators(["age > 18", "address.city == 'New York'"], ["&&"]) == ["age > 18", "&&", "address.city == 'New York'"]
    }

    def "resolve full expression"() {
        when:
        String expression = ExpressionEvaluator.sanitizeExpression("age > 18 && city == 'New York'")

        then:
        expression == "(age > 18) && (city == 'New York')"
    }

    def "resolve full expression with units"() {
        when:
        String expression = ExpressionEvaluator.sanitizeExpression("500m > 503m  || 300kg <  200kg &&  222 == 200")

        then:
        expression == "(500 > 503 && 'm' == 'm') || (300 < 200 && 'kg' == 'kg') && (222 == 200 && '' == '')"
    }

    def "resolve full expression with units 2"() {
        when:
        String expression = ExpressionEvaluator.sanitizeExpression("8080 > 500m")

        then:
        expression == "(8080 > 500 && '' == 'm')"
    }

    def "resolve full expression with units 3"() {
        when:
        String expression = ExpressionEvaluator.sanitizeExpression(EXPRESSION)

        then:
        expression == EXPECTED

        where:
        EXPRESSION                                          | EXPECTED
        "8080 > 500m"                                       | "(8080 > 500 && '' == 'm')"
        "8080m > 500"                                       | "(8080 > 500 && 'm' == '')"
        "8080m > 500m"                                      | "(8080 > 500 && 'm' == 'm')"
        "8080m > 500m && 8080m > 500m"                      | "(8080 > 500 && 'm' == 'm') && (8080 > 500 && 'm' == 'm')"
        "8080m > 500m || 8080m > 500m"                      | "(8080 > 500 && 'm' == 'm') || (8080 > 500 && 'm' == 'm')"
        "8080m > 500m || 8080m > 500m && 8080m > 500m"      | "(8080 > 500 && 'm' == 'm') || (8080 > 500 && 'm' == 'm') && (8080 > 500 && 'm' == 'm')"
        "8080m > 500m && 8080m > 500 || 8080m > 500m"       | "(8080 > 500 && 'm' == 'm') && (8080 > 500 && 'm' == '') || (8080 > 500 && 'm' == 'm')"
        "Deployment == Deployment"                          | "('Deployment' == 'Deployment')"
        "Deployment == Deployment && Deployment == lolailo" | "('Deployment' == 'Deployment') && ('Deployment' == 'lolailo')"


    }
}
