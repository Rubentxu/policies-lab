package dev.rubentxu.validations

import dev.rubentxu.policies.InputModel


class NumberValidation extends Validation<NumberValidation, Number> {
    final static def REGEX_LESS_THAN = /<\s*(\d+\.?\d*)\s*$/
    final static def REGEX_MORE_THAN = />\s*(\d+\.?\d*)\s*$/
    final static def REGEX_BETWEEN = /(\d+\.?\d*)\.\.(\d+\.?\d*)\s*$/
    final static def REGEX_EQUAL = /==\s*(\d+\.?\d*)\s*$/
    final static def REGEX_NOT_EQUAL = /!=\s*(\d+\.?\d*)\s*$/
    final static def REGEX_LESS_OR_EQUAL = /<=\s*(\d+\.?\d*)\s*$/
    final static def REGEX_MORE_OR_EQUAL = />=\s*(\d+\.?\d*)\s*$/


    private NumberValidation(Number sut, boolean enableNullCheck = true) {
        super(sut, enableNullCheck)
    }

    private NumberValidation(Number sut, String tag, boolean enableNullCheck = true) {
        super(sut, tag, enableNullCheck)
    }

    NumberValidation(Validation validation) {
        super(validation.sut, validation.tag, validation.enableNullCheck)
        this.onErrorMessages = validation.onErrorMessages
        this.validations = validation.validations
        this.tag = validation.tag
    }


    NumberValidation lowerThan(Number max) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("max", max)

        return test(renderMessage('lowerThan', model)) { Number n -> n < max }
    }


    NumberValidation greaterThan(Number min) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("min", min)

        return test(renderMessage('greaterThan', model)) { Number n -> n > min }
    }


    NumberValidation between(Number min, Number max) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("min", min)
        model.put("max", max)

        return test(renderMessage('between', model)) { Number n -> n >= min && n <= max }
    }


    NumberValidation isPositive() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('isPositive', model)) { Number n -> n > 0 }
    }


    NumberValidation isNegative() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('isNegative', model)) { Number n -> n < 0 }
    }


    NumberValidation isZero() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('isZero', model)) { Number n -> n == 0 }
    }


    NumberValidation isNotZero() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('isNotZero', model)) { Number n -> n != 0 }
    }


    NumberValidation isEven() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('isEven', model)) { Number n -> n % 2 == 0 }
    }


    NumberValidation isOdd() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('isOdd', model)) { Number n -> n % 2 != 0 }
    }


    NumberValidation lessOrEqual(Number number) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("number", number)

        return test(renderMessage('lessOrEqual', model)) { Number n1 -> n1 <= n }
    }


    NumberValidation moreOrEqual(Number number) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("number", number)

        return test(renderMessage('moreOrEqual', model)) { Number n1 -> n1 >= n }
    }


    NumberValidation withExpression(String expression) {
        expression = expression.trim()
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

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


    static NumberValidation from(Number sut, boolean checkNull = true) {
        return new NumberValidation(sut, checkNull)
    }


    static NumberValidation from(Number sut, String tag, boolean checkNull = true) {
        return new NumberValidation(sut, tag, checkNull)
    }


}
