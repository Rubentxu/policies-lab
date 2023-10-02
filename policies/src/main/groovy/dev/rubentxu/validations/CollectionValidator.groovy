package dev.rubentxu.validations

import dev.rubentxu.policies.InputModel


class CollectionValidator extends Validator<CollectionValidator, Collection> {

    private CollectionValidator(Collection sut) {
        super(sut)
    }

    private CollectionValidator(Collection sut, String tag) {
        super(sut, tag)
    }

    CollectionValidator(Validator validation) {
        super(validation.subjectUnderValidation, validation.tag)
        this.validationResults = validation.validationResults
        this.tag = validation.tag
    }

    CollectionValidator lowerThan(Number max) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)
        model.put("max", max)

        return test(renderMessage('lowerThanCollection', model)) { Collection c -> c.size() < max }
    }

    CollectionValidator greaterThan(Number min) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)
        model.put("min", min)

        return test(renderMessage('greaterThanCollection', model)) { Collection c -> c.size() > min }
    }

    CollectionValidator between(Number min, Number max) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)
        model.put("min", min)
        model.put("max", max)

        return test(renderMessage('betweenCollection', model)) { Collection c -> c.size() >= min && c.size() <= max }
    }

    CollectionValidator isEmpty() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)

        return test(renderMessage('isEmpty', model)) { Collection n -> n.isEmpty() }
    }

    CollectionValidator notEmpty() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)

        return test(renderMessage('notEmptyCollection', model)) { Collection n -> !n.isEmpty() }
    }

    CollectionValidator containsAny(Object element) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)
        model.put("element", element)

        return test(renderMessage('containsAny', model)) { Collection n -> n.any { it == element } }
    }

    CollectionValidator containsAll(Object element) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", subjectUnderValidation)
        model.put("element", element)

        return test(renderMessage('containsAll', model)) { Collection n -> n.every { it == element } }
    }

    static CollectionValidator from(Collection sut) {
        return new CollectionValidator(sut)
    }

    static CollectionValidator from(Collection sut, String tag) {
        return new CollectionValidator(sut, tag)
    }

}
