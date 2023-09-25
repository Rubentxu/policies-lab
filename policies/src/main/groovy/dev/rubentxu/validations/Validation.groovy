package dev.rubentxu.validations

import dev.rubentxu.policies.ExpressionEvaluator
import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.result.ValidationOutcome
import groovy.util.logging.Log
import org.codehaus.groovy.runtime.NullObject


@Log
class Validation<K extends Validation, T> {
    protected List<Boolean> validations
    protected List<String> onErrorMessages
    protected T sut
    protected String tag
    protected boolean enableNullCheck = true
    final static def REGEX_IS_NULL = /==\s*(NULL|null)\s*$/
    final static def REGEX_IS_NOT_NULL = /!=\s*(NULL|null)\s*$/
    final ResourceBundle messages


    protected Validation(T sut, boolean enableNullCheck = true) {
        this.validations = []
        this.onErrorMessages = []
        this.sut = sut
        this.tag = ''
        this.enableNullCheck = enableNullCheck
        if (this.enableNullCheck) {
            notNull()
        }
        Locale locale = Locale.getDefault()
        messages = ResourceBundle.getBundle("i18n/messages", locale)

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
        Locale locale = Locale.getDefault()
        messages = ResourceBundle.getBundle("i18n/messages", locale)
    }

    protected String renderMessage(String key, InputModel model) {
        String message = messages.getString(key)
        return ExpressionEvaluator.renderTemplateString(model, message)
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
            InputModel model = new InputModel()
            model.put("onErrorMessage", onErrorMessage)
            onErrorMessages.add(renderMessage('exception', model))
        }
        return this as K
    }


    Boolean isValid() {
        return !validations.any { it == false }
    }


    protected ValidationOutcome getResult() {
        return new ValidationOutcome(isValid: isValid(), errors: onErrorMessages)
    }


    T throwIfInvalid(String customErrorMessage = '') {
        onErrorMessages = onErrorMessages.plus(0, customErrorMessage, this.tag)

        if (!isValid()) {
            def message= onErrorMessages.unique()
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
        InputModel model = new InputModel()
        model.put("tag", tag)
        return tag ? renderMessage('getTagMsg', model) : ""
    }


    K notNull() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        K result = test(renderMessage('notNull', model)) {
            T s -> s != null
        }
        return result
    }


    K isNull() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        K result = test(renderMessage('isNull', model)) { T s -> s instanceof NullObject || s == null }
        return result
    }


    K notEqual(T n) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("n", n)

        return test(renderMessage('notEqual', model)) { T n1 -> n1 != n }
    }


    K equal(T n) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("n", n)
        return test(renderMessage('equal', model)) { T n1 -> n1 == n }
    }


    StringValidation isString() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        K result = test(renderMessage('isString', model)) { T s -> s instanceof String }
        return new StringValidation(result)
    }


    NumberValidation isNumber() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        K result = test(renderMessage('isNumber', model)) { T s ->
            s instanceof Number || (s instanceof NullObject && !enableNullCheck)
        }
        return new NumberValidation(result)
    }


    CollectionValidation isList() {
        InputModel model = new InputModel()
        model.put("sut", sut)

        K result = test(renderMessage('isList', model)) { T s -> s instanceof List }
        return new CollectionValidation(result)
    }


    CollectionValidation isCollection() {
        InputModel model = new InputModel()
        model.put("sut", sut)

        K result = test(renderMessage('isCollection', model)) { T s -> s instanceof Collection }
        return new CollectionValidation(result)
    }


    MapValidation isMap() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        K result = test(renderMessage('isMap', model)) { T s -> s instanceof Map }
        return new MapValidation(result)
    }


    <R> Validation<Validation, R> is(Class<R> clazz) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        notNull()
        test(renderMessage('is', model)) { T s ->
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
        String replacedExpression = expression.replace('$.value', value.toString()).trim()
        InputModel model = new InputModel()
        model.put("tag", tag)
        model.put("replacedExpression", replacedExpression)

        return withExpression(replacedExpression, renderMessage('withExpression', model))
    }


    <R> Validation<Validation, R> withExpression(String expression, String errorMsg) {
        return test(errorMsg) { n1 ->
            String finalExpresion = resolveNumberAndMagnitude(n1, expression)
            return Eval.me(finalExpresion)
        }
    }

    String resolveNumberAndMagnitude(value, String expression) {
        return ExpressionEvaluator.sanitizeExpression(expression)

    }

    <R> Validation<Validation, R> withRangeExpression(String expression, String errorMsg) {
        return test(errorMsg) { n1 ->
            return Eval.me("(${expression}).contains(${n1})")
        }
    }
}
