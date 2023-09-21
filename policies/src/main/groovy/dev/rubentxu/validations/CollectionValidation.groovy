package dev.rubentxu.validations


class CollectionValidation extends Validation<CollectionValidation, Collection> {


    private CollectionValidation(Collection sut, boolean enableNullCheck = true) {
        super(sut, enableNullCheck)
    }

    private CollectionValidation(Collection sut, String tag, boolean enableNullCheck = true) {
        super(sut, tag, enableNullCheck)
    }

    CollectionValidation(Validation validation) {
        super(validation.sut, validation.tag, validation.enableNullCheck)
        this.onErrorMessages = validation.onErrorMessages
        this.validations = validation.validations
        this.tag = validation.tag
    }

    CollectionValidation lowerThan(Number max) {
        return test("${tagMsg}${sut.size()} size Must be lower than $max") { Collection c -> c.size() < max }
    }


    CollectionValidation greaterThan(Number min) {
        return test("${tagMsg}${sut.size()} size Must be greater than $min") { Collection c -> c.size() > min }
    }


    CollectionValidation between(Number min, Number max) {
        return test("${tagMsg}${sut.size()} Must be between $min and $max") { Collection c -> c.size() >= min && c.size() <= max }
    }


    CollectionValidation isEmpty() {
        return test("${tagMsg}${sut.size()} size Must be empty") { Collection n -> n.isEmpty() }
    }


    CollectionValidation notEmpty() {
        return test("${tagMsg}${sut.size()} size Must be not empty") { Collection n -> !n.isEmpty() }
    }


    CollectionValidation containsAny(Object element) {
        return test("${tagMsg}${sut.size()} size Must contain any of $element") { Collection n -> n.any { it == element } }
    }


    CollectionValidation containsAll(Object element) {
        return test("${tagMsg}${sut.size()} size Must contain all of $element") { Collection n -> n.every { it == element } }
    }


    static CollectionValidation from(Collection sut, boolean checkNull = true) {
        return new CollectionValidation(sut, checkNull)
    }


    static CollectionValidation from(Collection sut, String tag, boolean checkNull = true) {
        return new CollectionValidation(sut, tag, checkNull)
    }


}
