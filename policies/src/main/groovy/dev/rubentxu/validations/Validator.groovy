package dev.rubentxu.validations

import dev.rubentxu.policies.ExpressionEvaluator
import dev.rubentxu.policies.InputModel
import dev.rubentxu.policies.result.ValidationOutcome
import groovy.util.logging.Log
import org.codehaus.groovy.runtime.NullObject

import java.text.MessageFormat


@Log
class Validator<K extends Validator, T> {
    protected List<ValidationResult> validationResults
    protected T subjectUnderValidation
    protected String tag
    final static def REGEX_IS_NULL = /==\s*(NULL|null)\s*$/
    final static def REGEX_IS_NOT_NULL = /!=\s*(NULL|null)\s*$/
    protected final ResourceBundle resourceBundle


    protected Validator(T sut) {
        this.validationResults = []
        this.subjectUnderValidation = sut
        this.tag = ''
        Locale locale = Locale.getDefault()
        resourceBundle = ResourceBundle.getBundle("i18n/messages", locale)
    }


    protected Validator(T sut, String tag) {
        this.validationResults = []
        this.subjectUnderValidation = sut
        this.tag = tag
        Locale locale = Locale.getDefault()
        resourceBundle = ResourceBundle.getBundle("i18n/messages", locale)

    }

    protected String renderMessage(String key, Object... params) {
        return MessageFormat.format(resourceBundle.getString(key), params)
    }

    protected K test(String errorMessage, Closure<Boolean> predicate) {
        try {
            if (subjectUnderValidation == null || subjectUnderValidation instanceof NullObject) {
                this.validationResults.add(new ValidationResult(isValid: false, errorMessage: errorMessage))
            } else if (predicate(subjectUnderValidation)) {
                this.validationResults.add(new ValidationResult(isValid: true, errorMessage: ''))
            } else {
                this.validationResults.add(new ValidationResult(isValid: false, errorMessage: errorMessage))
            }

        } catch (Exception ex) {
            InputModel model = new InputModel()
            model.put("errorMessage", errorMessage)


            validationResults.add(new ValidationResult(isValid: false, errorMessage: errorMessage))
            validationResults.add(new ValidationResult(isValid: false, errorMessage: resourceBundle.format('exception', [errorMessage])))
        }
        return this as K
    }

    Boolean isValid() {
        if(validationResults.isEmpty()) {
            validationResults.add(new ValidationResult(isValid: false, errorMessage: 'No existen validaciones para el objeto.'))
        }
        return validationResults.every { it.isValid }
    }

    protected ValidationOutcome getResult() {
        return new ValidationOutcome(isValid: isValid(), errors:  errors())
    }

    T throwIfInvalid(String customErrorMessage = '') {
        if (!isValid()) {
            List<String> errors = validationResults*.errorMessage.findAll{!it.isEmpty() }
            if(!customErrorMessage.isEmpty()) {
                errors.add(customErrorMessage)
            }

            String message= errors.join(' ')
            throw new IllegalArgumentException(message)
        }
        return subjectUnderValidation
    }

    T defaultValueIfInvalid(T defaultValue) {
        if (isValid()) {
            return subjectUnderValidation
        } else {
            return defaultValue
        }
    }

    def getValue() {
        return subjectUnderValidation
    }

    protected String getTagMsg() {
        return renderMessage('getTagMsg', tag?:'Variable')
    }

    K notNull() {
        K result = test(renderMessage('notNull', getTagMsg(), subjectUnderValidation)) {
            T s -> s != null
        }
        return result
    }

    K isNull() {
        K result = test(renderMessage('isNull', tagMsg, subjectUnderValidation)) { T s -> s instanceof NullObject || s == null }
        return result
    }

    K notEqual(T object) {
        return test(renderMessage('notEqual', tagMsg, subjectUnderValidation, object)) { T n1 -> n1 != n }
    }

    K equal(T object) {
        return test(renderMessage('equal', tagMsg, subjectUnderValidation, object)) { T n1 -> n1 == n }
    }

    StringValidator isString() {
        K result = test(renderMessage('isString', tagMsg, subjectUnderValidation)) { T s -> s instanceof String }
        return new StringValidator(result)
    }


    NumberValidator isNumber() {
        K result = test(renderMessage('isNumber', tagMsg, subjectUnderValidation)) { T s ->
            s instanceof Number || (s instanceof NullObject)
        }
        return new NumberValidator(result)
    }


    CollectionValidator isList() {
        K result = test(renderMessage('isList', subjectUnderValidation.getClass().name)) { T s -> s instanceof List }
        return new CollectionValidator(result)
    }


    CollectionValidator isCollection() {
        K result = test(renderMessage('isCollection', subjectUnderValidation.getClass().name)) { T s -> s instanceof Collection }
        return new CollectionValidator(result)
    }


    MapValidator isMap() {
        K result = test(renderMessage('isMap', tagMsg, subjectUnderValidation, subjectUnderValidation.getClass().name))
                { T s -> s instanceof Map }
        return new MapValidator(result)
    }


    <R> Validator<Validator, R> is(Class<R> clazz) {
        test(renderMessage('is', tagMsg, subjectUnderValidation, subjectUnderValidation.getClass().name)) { T s ->
            s.class == clazz || clazz.isAssignableFrom(s.class)
        }
        return this
    }


    <R> Validator<Validator, R> is(String onErrorMessage, Closure<Boolean> predicate) {
        test(onErrorMessage, predicate)
        return this
    }


    static <T> Validator<Validator, T> from(T sut) {
        return new Validator<Validator, T>(sut)
    }


    static <T> Validator<Validator, T> from(T sut, String tag) {
        return new Validator<Validator, T>(sut, tag)
    }


    <R> Validator<Validator, R> withExpression(String expression) {
        String replacedExpression = expression.replace('$.value', ExpressionEvaluator.sanitizeValue(value.toString())).trim()
        return withExpression(replacedExpression, renderMessage('withExpression', tag ? " for Key '$tag'" : "", replacedExpression))
    }


    <R> Validator<Validator, R> withExpression(String expression, String errorMsg) {
        return test(errorMsg) { n1 ->
            String finalExpresion = resolveNumberAndMagnitude(expression)
            return Eval.me(finalExpresion)
        }
    }

    String resolveNumberAndMagnitude(String expression) {
        return ExpressionEvaluator.sanitizeExpression(expression)

    }

    <R> Validator<Validator, R> withRangeExpression(String expression, String errorMsg) {
        return test(errorMsg) { n1 ->
            return Eval.me("(${expression}).contains(${n1})")
        }
    }

    List<String> errors() {
        validationResults.findAll {!it.isValid }.collect {it.errorMessage }
    }
}
