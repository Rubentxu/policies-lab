package dev.rubentxu.validations

import dev.rubentxu.policies.InputModel


class MapValidation extends Validation<MapValidation, Map> {
    def resolvedValue

    private MapValidation(Map sut, boolean enableNullCheck = true) {
        super(sut,enableNullCheck)
        resolvedValue = sut
    }

    private MapValidation(Map sut, String tag, boolean enableNullCheck = true) {
        super(sut, tag, enableNullCheck)
        resolvedValue = sut
    }

    MapValidation(Validation validation) {
        super(validation.sut)
        resolvedValue = validation.sut
        this.onErrorMessages = validation.onErrorMessages
        this.validations = validation.validations
        this.tag = validation.tag
    }


    MapValidation notNull() {
        return super.notNull() as MapValidation
    }


    MapValidation getKey() {
        resolvedValue = getResolvedValue(tag)
        InputModel model = new InputModel()
        model.put("tag", tag)

        return test(renderMessage('getKey', model)) { resolvedValue != null }
    }


    MapValidation getKey(String key) {
        resolvedValue = getResolvedValue(key)
        InputModel model = new InputModel()
        model.put("tag", key)
        return test(renderMessage('getKey', model)) { resolvedValue != null }
    }


    def getResolvedValue(String key) {
        def result = findDeep(sut, key)

        if (result instanceof String || result instanceof GString) {
            result = result.trim()
        }
        return result
    }


    @Override
    def getValue() {
        return resolvedValue
    }


    static MapValidation from(Map sut, boolean checkNull = true) {
        return new MapValidation(sut, checkNull)
    }


    static MapValidation from(Map sut, String tag, boolean checkNull = true) {
        return new MapValidation(sut, tag, checkNull)
    }


    private def findDeep(Map m, String path) {
        if (m == null || !path) return null
        if (m.containsKey(path)) return m[path]

        def slice = path.split('\\.')
        def key = slice[0]
        if (m.containsKey(key) && slice.size() == 1) {
            return m[key]
        } else if (m.containsKey(key)) {
            if (m[key] instanceof Map) {
                return findDeep(m[key], slice[1..-1].join('.'))
            }
        }
    }

}
