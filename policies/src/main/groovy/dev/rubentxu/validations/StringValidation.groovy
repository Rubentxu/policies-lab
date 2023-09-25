package dev.rubentxu.validations

import dev.rubentxu.policies.InputModel
import groovy.text.SimpleTemplateEngine

//@CompileStatic
class StringValidation extends Validation<StringValidation, String> {
    def EMAIL_REGEX = /^(([^<>()[\]\.,;:\s@\"]]+(\.[^<>()[\]\.,;:\s@\"]]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    def HTTP_PROTOCOL_REGEX = /^(?:http[s]?:\/\/.)(?:www\.)?[-a-zA-Z0-9@%._\+~#=]{2,256}\.[a-z]{2,6}\b(?:[-a-zA-Z0-9@:%_\+.~#?&\/\/=]*)/
    final static def REGEX_IN_ARRAY = /^\s*IN\s*\[(.*)\]/
    final static def REGEX_EQUAL = /==\s*"?([\sa-zA-ZÀ-ÿñÑ0-9_\.-]+)"?\s*$/
    final static def REGEX_NOT_EQUAL = /!=\s*"?([a-zA-ZÀ-ÿñÑ0-9_\.-]+)"?\s*$/

    private StringValidation(String sut, boolean enableNullCheck = true) {
        super(sut, enableNullCheck)
    }

    private StringValidation(String sut, String tag, boolean enableNullCheck = true) {
        super(sut, tag, enableNullCheck)
    }

    StringValidation(Validation validation) {
        super(validation.sut, validation.tag, validation.enableNullCheck)
        this.onErrorMessages = validation.onErrorMessages
        this.validations = validation.validations
        this.tag = validation.tag
    }


    StringValidation moreThan(int size) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("size", size)

        return test(renderMessage('moreThan', model)) { String s -> s.length() >= size }
    }


    StringValidation lessThan(int size) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("size", size)

        return test(renderMessage('lessThanString', model)) { String s -> s.length() <= size }
    }


    StringValidation between(int minSize, int maxSize) {
        moreThan(minSize)
        return lessThan(maxSize)
    }


    StringValidation contains(String subString) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("subString", subString)

        return test(renderMessage('contains', model)) { String s -> s.contains(c) }
    }


    StringValidation isEmail() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('isEmail', model)) { String s -> s ==~ EMAIL_REGEX }
    }


    StringValidation matchRegex(regex) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("regex", regex)

        return test(renderMessage('matchRegex', model)) { String s -> s ==~ regex }
    }


    StringValidation isHttpProtocol() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('isHttpProtocol', model)) { String s -> s ==~ HTTP_PROTOCOL_REGEX }
    }


    StringValidation containsIn(String[] array) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("array", array)

        return test(renderMessage('containsIn', model)) { String s -> array.contains(s) }
    }


    StringValidation containsIn(List<String> array) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("array", array)

        return test(renderMessage('containsIn', model)) { String s -> array.contains(s) }
    }


    StringValidation notEmpty() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('notEmpty', model)) { s -> s != null && s != '' }
    }


    static StringValidation from(String sut, boolean checkNull = true) {
        return new StringValidation(sut, checkNull)
    }


    static StringValidation from(String sut, String tag, boolean checkNull = true) {
        return new StringValidation(sut, tag, checkNull)
    }


    StringValidation withExpression(String expression) {
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

        if (expression ==~ REGEX_IS_NULL) {
            return isNull()
        }

        if (expression ==~ REGEX_IS_NOT_NULL) {
            return notNull()
        }
        return super.withExpression(expression) as StringValidation

    }


    <R> Validation<Validation, R> renderValue(Map binding) {
        try {
            sut = new SimpleTemplateEngine()
                    .createTemplate(sut).make(binding).toString()
        } catch (ex) {
        }
        return this
    }


    StringValidation inArray(String arrayExpression) {
        def array = arrayExpression
                .replaceAll('"', "")
                .replaceAll("'", "")
                .tokenize(',')
                .collect { it.trim() }
        return containsIn(array)
    }
}
