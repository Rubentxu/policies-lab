package dev.rubentxu.validations

import dev.rubentxu.policies.InputModel


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
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("max", max)

        return test(renderMessage('lowerThanCollection', model)) { Collection c -> c.size() < max }
    }


    CollectionValidation greaterThan(Number min) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("min", min)

        return test(renderMessage('greaterThanCollection', model)) { Collection c -> c.size() > min }
    }


    CollectionValidation between(Number min, Number max) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("min", min)
        model.put("max", max)

        return test(renderMessage('betweenCollection', model)) { Collection c -> c.size() >= min && c.size() <= max }
    }


    CollectionValidation isEmpty() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('isEmpty', model)) { Collection n -> n.isEmpty() }
    }


    CollectionValidation notEmpty() {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)

        return test(renderMessage('notEmptyCollection', model)) { Collection n -> !n.isEmpty() }
    }


    CollectionValidation containsAny(Object element) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("element", element)

        return test(renderMessage('containsAny', model)) { Collection n -> n.any { it == element } }
    }


    CollectionValidation containsAll(Object element) {
        InputModel model = new InputModel()
        model.put("tagMsg", tagMsg)
        model.put("sut", sut)
        model.put("element", element)

        return test(renderMessage('containsAll', model)) { Collection n -> n.every { it == element } }
    }


    static CollectionValidation from(Collection sut, boolean checkNull = true) {
        return new CollectionValidation(sut, checkNull)
    }


    static CollectionValidation from(Collection sut, String tag, boolean checkNull = true) {
        return new CollectionValidation(sut, tag, checkNull)
    }


}
