package dev.rubentxu.validations

import dev.rubentxu.policies.ExpressionEvaluator


//@Category
class ValidatorCategory {

    static StringValidator validate(String self, String tag= '') {
        return StringValidator.from(self, tag)
    }

    static NumberValidator validate(Number self, String tag = '') {
        return NumberValidator.from(self, tag)
    }

    static CollectionValidator validate(List self, String tag = '') {
        return CollectionValidator.from(self, tag)
    }

    static MapValidator validate(Map self, String tag = '') {
        return MapValidator.from(self, tag)
    }

    static Validator validateAndGet(Map self, String tag) {
        def value = MapValidator.from(self, tag).getKey().getValue()

        if (value instanceof Map) {
            return MapValidator.from(value, tag)
        } else if (value instanceof List) {
            return CollectionValidator.from(value, tag)
        } else if (value instanceof String) {
            return StringValidator.from(value, tag)
        } else if (value instanceof Number) {
            return NumberValidator.from(value, tag)
        }
        return Validator.from(value, tag)
    }


    static <T> Validator<Validator, T> validate(T self) {
        return Validator.from(self)
    }


    static <T> Validator<Validator, T> validate(T self, String tag) {
        return (Validator<Validator, T>) Validator.from(self, tag)
    }


    static Validator validateReferenceResolver(Map self, String reference) {
        def value = ExpressionEvaluator.evaluateExpression(self, reference)
        if(value == "") value = null

        if(value instanceof Map) {
            return MapValidator.from(value, reference)
        } else if(value instanceof List) {
            return CollectionValidator.from(value, reference)
        } else if(value instanceof String) {
            return StringValidator.from(value, reference)
        } else if(value instanceof Number) {
            return NumberValidator.from(value, reference)
        }
        return Validator.from(value, reference)
    }

}
