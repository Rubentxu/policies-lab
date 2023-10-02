package dev.rubentxu.validations

import dev.rubentxu.policies.InputModel


class NumberValidator extends Validator<NumberValidator, Number> {
    final static def REGEX_LESS_THAN = /<\s*(\d+\.?\d*)\s*$/
    final static def REGEX_MORE_THAN = />\s*(\d+\.?\d*)\s*$/
    final static def REGEX_BETWEEN = /(\d+\.?\d*)\.\.(\d+\.?\d*)\s*$/
    final static def REGEX_EQUAL = /==\s*(\d+\.?\d*)\s*$/
    final static def REGEX_NOT_EQUAL = /!=\s*(\d+\.?\d*)\s*$/
    final static def REGEX_LESS_OR_EQUAL = /<=\s*(\d+\.?\d*)\s*$/
    final static def REGEX_MORE_OR_EQUAL = />=\s*(\d+\.?\d*)\s*$/

    private NumberValidator(Number number) {
        super(number)
    }

    private NumberValidator(Number number, String tag) {
        super(number, tag)
    }

    NumberValidator(Validator validation) {
        super(validation.subjectUnderValidation, validation.tag)
        this.validationResults = validation.validationResults
        this.tag = validation.tag
    }

    NumberValidator lowerThan(Number max) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)
        model.put("max", max)

        return test(renderMessage('lowerThan', model)) { Number n -> n < max }
    }

    NumberValidator greaterThan(Number min) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)
        model.put("min", min)

        return test(renderMessage('greaterThan', model)) { Number n -> n > min }
    }

    NumberValidator between(Number min, Number max) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)
        model.put("min", min)
        model.put("max", max)

        return test(renderMessage('between', model)) { Number n -> n >= min && n <= max }
    }

    NumberValidator isPositive() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)

        return test(renderMessage('isPositive', model)) { Number n -> n > 0 }
    }

    NumberValidator isNegative() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)

        return test(renderMessage('isNegative', model)) { Number n -> n < 0 }
    }

    NumberValidator isZero() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)

        return test(renderMessage('isZero', model)) { Number n -> n == 0 }
    }

    NumberValidator isNotZero() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)

        return test(renderMessage('isNotZero', model)) { Number n -> n != 0 }
    }

    NumberValidator isEven() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)

        return test(renderMessage('isEven', model)) { Number n -> n % 2 == 0 }
    }

    NumberValidator isOdd() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)

        return test(renderMessage('isOdd', model)) { Number n -> n % 2 != 0 }
    }

    NumberValidator lessOrEqual(Number number) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)
        model.put("number", number)

        return test(renderMessage('lessOrEqual', model)) { Number n1 -> n1 <= n }
    }

    NumberValidator moreOrEqual(Number number) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)
        model.put("number", number)

        return test(renderMessage('moreOrEqual', model)) { Number n1 -> n1 >= n }
    }

    NumberValidator withExpression(String expression) {
        expression = expression.trim()
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)

        if (expression ==~ REGEX_LESS_THAN) {
            def matcher = expression =~ REGEX_LESS_THAN
            model.put("value", matcher[0][1])

            return withExpression(expression, renderMessage('lessThanNumber', model))
        }
        if (expression ==~ REGEX_MORE_THAN) {
            def matcher = expression =~ REGEX_MORE_THAN
            model.put("value", matcher[0][1])

            return withExpression(expression, renderMessage('moreThanNumber', model))
        }
        if (expression ==~ REGEX_BETWEEN) {
            def matcher = expression =~ REGEX_BETWEEN
            model.put("value", matcher[0][1])
            model.put("value2", matcher[0][2])

            return withRangeExpression(expression, renderMessage('betweenNumber', model))
        }
        if (expression ==~ REGEX_EQUAL) {
            def matcher = expression =~ REGEX_EQUAL
            model.put("value", matcher[0][1])

            return withExpression(expression, renderMessage('equalsNumber', model))
        }
        if (expression ==~ REGEX_NOT_EQUAL) {
            def matcher = expression =~ REGEX_NOT_EQUAL
            model.put("value", matcher[0][1])

            return withExpression(expression, renderMessage('notEqualsNumber', model))
        }
        if (expression ==~ REGEX_LESS_OR_EQUAL) {
            def matcher = expression =~ REGEX_LESS_OR_EQUAL
            model.put("value", matcher[0][1])

            return withExpression(expression, renderMessage('lessOrEqualNumber', model))
        }
        if (expression ==~ REGEX_MORE_OR_EQUAL) {
            def matcher = expression =~ REGEX_MORE_OR_EQUAL
            model.put("value", matcher[0][1])

            return withExpression(expression, renderMessage('moreOrEqualNumber', model))
        }
        if (expression ==~ REGEX_IS_NULL) {
            return isNull()
        }

        if (expression ==~ REGEX_IS_NOT_NULL) {
            return notNull()
        }
        return super.withExpression(expression)
    }

    private Number toNumber(String s) {
        if (s == "NULL") {
            return null
        }
        if (s.contains('.')) {
            return s.toBigDecimal()
        }
        return s.toBigInteger()
    }

    static NumberValidator from(Number number) {
        return new NumberValidator(number)
    }

    static NumberValidator from(Number number, String tag) {
        return new NumberValidator(number, tag)
    }

}
