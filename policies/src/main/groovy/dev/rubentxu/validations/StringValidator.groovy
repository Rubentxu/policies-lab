package dev.rubentxu.validations

import groovy.text.SimpleTemplateEngine
import org.codehaus.groovy.runtime.NullObject

//@CompileStatic
class StringValidator extends Validator<StringValidator, String> {
    def EMAIL_REGEX = /^(([^<>()[\]\.,;:\s@\"]]+(\.[^<>()[\]\.,;:\s@\"]]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    def HTTP_PROTOCOL_REGEX = /^(?:http[s]?:\/\/.)(?:www\.)?[-a-zA-Z0-9@%._\+~#=]{2,256}\.[a-z]{2,6}\b(?:[-a-zA-Z0-9@:%_\+.~#?&\/\/=]*)/
    final static def REGEX_IN_ARRAY = /^\s*IN\s*\[(.*)\]/
    final static def REGEX_EQUAL = /==\s*"?([\sa-zA-ZÀ-ÿñÑ0-9_\.-]+)"?\s*$/
    final static def REGEX_NOT_EQUAL = /!=\s*"?([a-zA-ZÀ-ÿñÑ0-9_\.-]+)"?\s*$/

    private StringValidator(String text, String tag = '' ) {
        super(text, tag)
    }

    private StringValidator(NullObject text, String tag = '' ) {
        super(text, tag)
    }

    StringValidator(Validator validation) {
        super(validation.subjectUnderValidation, validation.tag)
        this.validationResults = validation.validationResults
        this.tag = validation.tag
    }

    StringValidator moreThan(int size) {
        return test(renderMessage('moreThan', tagMsg, subjectUnderValidation, size)) { String s -> s.length() >= size }
    }

    StringValidator lessThan(int size) {
        return test(renderMessage('lessThanString', tagMsg, subjectUnderValidation, size)) { String s -> s.length() <= size }
    }

    StringValidator between(int minSize, int maxSize) {
        moreThan(minSize)
        return lessThan(maxSize)
    }

    StringValidator contains(String subString) {
        return test(renderMessage('contains', tagMsg, subjectUnderValidation, subString)) { String s -> s.contains(c) }
    }

    StringValidator isEmail() {
        return test(renderMessage('isEmail', tagMsg, subjectUnderValidation)) { String s -> s ==~ EMAIL_REGEX }
    }

    StringValidator matchRegex(regex) {
        return test(renderMessage('matchRegex', tagMsg, subjectUnderValidation, regex)) { String s -> s ==~ regex }
    }

    StringValidator isHttpProtocol() {
        return test(renderMessage('isHttpProtocol', tagMsg, subjectUnderValidation)) { String s -> s ==~ HTTP_PROTOCOL_REGEX }
    }

    StringValidator containsIn(String[] array) {
        return test(renderMessage('containsIn', tagMsg, subjectUnderValidation, array.join(','))) { String s -> array.contains(s) }
    }

    StringValidator containsIn(List<String> array) {
        return test(renderMessage('containsIn', tagMsg, subjectUnderValidation, array.join(','))) { String s -> array.contains(s) }
    }

    StringValidator notEmpty() {
        return test(renderMessage('notEmpty', tagMsg, subjectUnderValidation)) { s -> s != null && s != '' }
    }

    static StringValidator from(String text) {
        return new StringValidator(text)
    }

    static StringValidator from(String text, String tag) {
        return new StringValidator(text, tag)
    }

    static StringValidator from(Object sut, String tag, List<ValidationResult> validationResults, String errorMessage, Closure<Boolean> predicate) {
        return new StringValidator(sut, tag).test(errorMessage, predicate)
    }

    StringValidator withExpression(String expression) {
        if (expression ==~ REGEX_IN_ARRAY) {
            def matcher = expression =~ REGEX_IN_ARRAY
            return inArray(matcher[0][1])
        }
        if (expression ==~ REGEX_EQUAL) {
            def matcher = expression =~ REGEX_EQUAL
            return equal(matcher[0][1])
        }
        if (expression ==~ REGEX_NOT_EQUAL) {
            def matcher = expression =~ REGEX_NOT_EQUAL
            return notEqual(matcher[0][1])
        }

        if (expression ==~ this.REGEX_IS_NULL) {
            return isNull()
        }

        if (expression ==~ this.REGEX_IS_NOT_NULL) {
            return notNull()
        }
        return super.withExpression(expression) as StringValidator

    }

    <R> Validator<Validator, R> renderValue(Map binding) {
        try {
            subjectUnderValidation = new SimpleTemplateEngine()
                    .createTemplate(subjectUnderValidation).make(binding).toString()
        } catch (ex) {
        }
        return this
    }

    StringValidator inArray(String arrayExpression) {
        def array = arrayExpression
                .replaceAll('"', "")
                .replaceAll("'", "")
                .tokenize(',')
                .collect { it.trim() }
        return containsIn(array)
    }
}
