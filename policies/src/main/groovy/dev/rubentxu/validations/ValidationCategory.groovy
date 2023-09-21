package dev.rubentxu.validations


//@Category
class ValidationCategory {

    static StringValidation validate(String self, boolean enableNullCheck = true) {
        return StringValidation.from(self, enableNullCheck)

    }


    static StringValidation validate(String self, String tag, boolean enableNullCheck = true) {
        return StringValidation.from(self, tag, enableNullCheck)

    }


    static NumberValidation validate(Number self, boolean enableNullCheck = true) {
        return NumberValidation.from(self, enableNullCheck)
    }


    static NumberValidation validate(Number self, String tag, boolean enableNullCheck = true) {
        return NumberValidation.from(self, tag, enableNullCheck)
    }


    static CollectionValidation validate(List self, boolean enableNullCheck = true) {
        return CollectionValidation.from(self, enableNullCheck)
    }


    static CollectionValidation validate(List self, String tag, boolean enableNullCheck = true) {
        return CollectionValidation.from(self, tag, enableNullCheck)
    }


    static MapValidation validate(Map self, boolean enableNullCheck = true) {
        return MapValidation.from(self, enableNullCheck)
    }


    static MapValidation validate(Map self, String tag, boolean enableNullCheck = true) {
        return MapValidation.from(self, tag, enableNullCheck)
    }


    static Validation validateAndGet(Map self, String tag) {
        return validateAndGet(self, tag, true)
    }


    static Validation validateAndGet(Map self, String tag, boolean enableNullCheck) {
        def value = MapValidation.from(self, tag, enableNullCheck).getKey().getValue()

        if (value instanceof Map) {
            return MapValidation.from(value, tag, enableNullCheck)
        } else if (value instanceof List) {
            return CollectionValidation.from(value, tag, enableNullCheck)
        } else if (value instanceof String) {
            return StringValidation.from(value, tag, enableNullCheck)
        } else if (value instanceof Number) {
            return NumberValidation.from(value, tag, enableNullCheck)
        }
        return Validation.from(value, tag, enableNullCheck)


    }


    static <T> Validation<Validation, T> validate(T self, boolean enableNullCheck = true) {
        return Validation.from(self, enableNullCheck)
    }


    static <T> Validation<Validation, T> validate(T self, String tag, boolean enableNullCheck = true) {
        return (Validation<Validation, T>) Validation.from(self, tag, enableNullCheck)
    }


    static def validateNull(Object self, String tag, boolean enableNullCheck = true) {
        return Validation.from(self, tag, enableNullCheck)
    }

}
