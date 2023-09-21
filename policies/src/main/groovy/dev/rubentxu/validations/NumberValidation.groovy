package dev.rubentxu.validations


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
        return test("${tagMsg}${sut} Must be lower than $max") { Number n -> n < max }
    }


    NumberValidation greaterThan(Number min) {
        return test("${tagMsg}${sut} Must be greater than $min") { Number n -> n > min }
    }


    NumberValidation between(Number min, Number max) {
        return test("${tagMsg}${sut} Must be between $min and $max") { Number n -> n >= min && n <= max }
    }


    NumberValidation isPositive() {
        return test("${tagMsg}${sut} Must be positive") { Number n -> n > 0 }
    }


    NumberValidation isNegative() {
        return test("${tagMsg}${sut} Must be negative") { Number n -> n < 0 }
    }


    NumberValidation isZero() {
        return test("${tagMsg}${sut} Must be zero") { Number n -> n == 0 }
    }


    NumberValidation isNotZero() {
        return test("${tagMsg}${sut} Must be not zero") { Number n -> n != 0 }
    }


    NumberValidation isEven() {
        return test("${tagMsg}${sut} Must be even") { Number n -> n % 2 == 0 }
    }


    NumberValidation isOdd() {
        return test("${tagMsg}${sut} Must be odd") { Number n -> n % 2 != 0 }
    }


    NumberValidation lessOrEqual(Number n) {
        return test("${tagMsg}${sut} Must be less or equal to $n") { Number n1 -> n1 <= n }
    }


    NumberValidation moreOrEqual(Number n) {
        return test("${tagMsg}${sut} Must be more or equal to $n") { Number n1 -> n1 >= n }
    }


    NumberValidation withExpression(String expression) {
        expression = expression.trim()

        if (expression ==~ REGEX_LESS_THAN) {
            def matcher = expression =~ REGEX_LESS_THAN
            return withExpression(expression, "${tagMsg}${sut} Must be lower than ${matcher[0][1]}")
        }
        if (expression ==~ REGEX_MORE_THAN) {
            def matcher = expression =~ REGEX_MORE_THAN
            return withExpression(expression, "${tagMsg}${sut} Must be greater than ${matcher[0][1]}")
        }
        if (expression ==~ REGEX_BETWEEN) {
            def matcher = expression =~ REGEX_BETWEEN
            return withRangeExpression(expression, "${tagMsg}${sut} Must be between ${matcher[0][1]} and ${matcher[0][2]}")
        }
        if (expression ==~ REGEX_EQUAL) {
            def matcher = expression =~ REGEX_EQUAL
            return withExpression(expression, "${tagMsg}${sut} Must be equal to ${matcher[0][1]}")
        }
        if (expression ==~ REGEX_NOT_EQUAL) {
            def matcher = expression =~ REGEX_NOT_EQUAL
            return withExpression(expression, "${tagMsg}${sut} Must be not equal to ${matcher[0][1]}")
        }
        if (expression ==~ REGEX_LESS_OR_EQUAL) {
            def matcher = expression =~ REGEX_LESS_OR_EQUAL
            return withExpression(expression, "${tagMsg}${sut} Must be less or equal to ${matcher[0][1]}")
        }
        if (expression ==~ REGEX_MORE_OR_EQUAL) {
            def matcher = expression =~ REGEX_MORE_OR_EQUAL
            return withExpression(expression, "${tagMsg}${sut} Must be more or equal to ${matcher[0][1]}")
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
