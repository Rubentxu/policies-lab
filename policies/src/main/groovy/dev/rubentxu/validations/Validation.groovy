package dev.rubentxu.validations

import org.codehaus.groovy.runtime.NullObject

class Validation<K extends Validation, T> {
    protected List<Boolean> validations
    protected List<String> onErrorMessages
    protected T sut
    protected String tag
    protected boolean enableNullCheck = true
    final static def REGEX_IS_NULL = /==\s*(NULL|null)\s*$/
    final static def REGEX_IS_NOT_NULL = /!=\s*(NULL|null)\s*$/


    protected Validation(T sut, boolean enableNullCheck = true) {
        this.validations = []
        this.onErrorMessages = []
        this.sut = sut
        this.tag = ''
        this.enableNullCheck = enableNullCheck
        if (this.enableNullCheck) {
            notNull()
        }

    }

    protected Validation(T sut, String tag, boolean enableNullCheck = true) {
        this.validations = []
        this.onErrorMessages = []
        this.sut = sut
        this.tag = tag
        this.enableNullCheck = enableNullCheck
        if (this.enableNullCheck) {
            notNull()
        }
    }


    protected K test(String onErrorMessage, Closure<Boolean> predicate) {
        try {
            if (enableNullCheck && (sut == null || sut instanceof NullObject)) {
                this.onErrorMessages.add(onErrorMessage)
                this.validations.add(false)
            } else if (predicate(sut)) {
                this.validations.add(true)
            } else {
                this.onErrorMessages.add(onErrorMessage)
                this.validations.add(false)
            }

        } catch (Exception ex) {
            validations.add(false)
            onErrorMessages.add("Syntax Error, invalid expression. $onErrorMessage")
        }
        return this as K
    }


    Boolean isValid() {
        return !validations.any { it == false }
    }


    protected ResultValidation getResult() {
        return new ResultValidation(isValid: isValid(), errors: onErrorMessages)
    }


    T throwIfInvalid(String customErrorMessage = '') {
        onErrorMessages = onErrorMessages.plus(0, customErrorMessage, this.tag)

        if (!isValid()) {
            def message = Logger.createBanner(onErrorMessages.unique())
            throw new IllegalArgumentException(message)
        }
        return sut
    }


    T defaultValueIfInvalid(T defaultValue) {
        if (isValid()) {
            return sut
        } else {
            return defaultValue
        }
    }


    def getValue() {
        return sut
    }


    protected String getTagMsg() {
        return tag ? "$tag with value " : ""
    }


    K notNull() {
        K result = test("${tagMsg}${sut} Must not be null") {
            T s -> s != null
        }
        return result
    }


    K isNull() {
        K result = test("${tagMsg}${sut} Must be null") { T s -> s instanceof NullObject || s == null }
        return result
    }


    K notEqual(T n) {
        return test("${tagMsg}${sut} Must be not equal to $n") { T n1 -> n1 != n }
    }


    K equal(T n) {
        return test("${tagMsg}${sut} Must be equal to $n") { T n1 -> n1 == n }
    }


    StringValidation isString() {
        K result = test("${tagMsg}${sut} Must be type String. Current is ${sut.getClass().name}") { T s -> s instanceof String }
        return new StringValidation(result)
    }


    NumberValidation isNumber() {
        K result = test("${tagMsg}${sut} Must be type Number. Current is ${sut.getClass().name}") { T s ->
            s instanceof Number || (s instanceof NullObject && !enableNullCheck)
        }
        return new NumberValidation(result)
    }


    CollectionValidation isList() {
        K result = test("Must be type List. Current is ${sut.getClass().name}") { T s -> s instanceof List }
        return new CollectionValidation(result)
    }


    CollectionValidation isCollection() {
        K result = test("Must be type List. Current is ${sut.getClass().name}") { T s -> s instanceof Collection }
        return new CollectionValidation(result)
    }


    MapValidation isMap() {
        K result = test("${tagMsg}${sut} Must be type Map. Current is ${sut.getClass().name}") { T s -> s instanceof Map }
        return new MapValidation(result)
    }


    <R> Validation<Validation, R> is(Class<R> clazz) {
        notNull()
        test("${tagMsg}${sut} Must be type $clazz. Current is ${sut?.getClass()?.name}") { T s ->
            s.class == clazz || clazz.isAssignableFrom(s.class)
        }
        // TODO: hay que probar esto. Lo cambio a this porque sino no propaga los errores
        //return new Validation<Validation, R>(sut, tag)
        return this
    }


    <R> Validation<Validation, R> is(String onErrorMessage, Closure<Boolean> predicate) {
        test(onErrorMessage, predicate)
        // TODO: hay que probar esto. Lo cambio a this porque sino no propaga los errores
        //return new Validation<Validation, R>(sut, tag)
        return this
    }


    static <T> Validation<Validation, T> from(T sut, boolean enableNullCheck = false) {
        return new Validation<Validation, T>(sut, enableNullCheck)
    }


    static <T> Validation<Validation, T> from(T sut, String tag) {
        return new Validation<Validation, T>(sut, tag)
    }


    static <T> Validation<Validation, T> from(T sut, String tag, boolean enableNullCheck) {
        return new Validation<Validation, T>(sut, tag, enableNullCheck)
    }


    <R> Validation<Validation, R> withExpression(String expression) {
        expression = expression?.trim()
        return withExpression(expression, "The expression did not evaluate to true ${tag ? "for $tag" : ""} with  ${sut} $expression")
    }


    <R> Validation<Validation, R> withExpression(String expression, String errorMsg) {
        return test(errorMsg) { n1 ->
            String finalExpresion =resolveNumberAndMagnitude(n1, expression)
            return Eval.me(finalExpresion)
        }
    }

    String resolveNumberAndMagnitude(value, String expression) {
//        def expression = "64Mi >= 128Mi"
        String tempExpression = "${value} ${expression}"
        def matcher = tempExpression =~ /(\d+)([A-Za-z]+)/
        if (matcher.find()) {
            def number1 = matcher.group(1).toInteger()
            def magnitude1 = matcher.group(2)
            matcher.find()
            def number2 = matcher.group(1).toInteger()
            def magnitude2 = matcher.group(2)
            def logicalExpression = "${number1} >= ${number2} && '${magnitude1}' == '${magnitude2}'"
            return logicalExpression
        }
        value = value instanceof String ? "'$value'" : value
        return "${value} ${expression}"
    }


    <R> Validation<Validation, R> withRangeExpression(String expression, String errorMsg) {
        return test(errorMsg) { n1 ->
            return Eval.me("(${expression}).contains(${n1})")
        }
    }
}
