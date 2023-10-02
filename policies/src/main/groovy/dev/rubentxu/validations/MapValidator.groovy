package dev.rubentxu.validations

import dev.rubentxu.policies.InputModel


class MapValidator extends Validator<MapValidator, Map> {
    def resolvedValue

    private MapValidator(Map map) {
        super(map)
        resolvedValue = map
    }

    private MapValidator(Map map, String tag) {
        super(map, tag)
        resolvedValue = map
    }

    MapValidator(Validator validation) {
        super(validation.subjectUnderValidation)
        resolvedValue = validation.subjectUnderValidation
        this.validationResults = validation.validationResults
        this.tag = validation.tag
    }

    MapValidator notNull() {
        return super.notNull() as MapValidator
    }

    MapValidator getKey() {
        resolvedValue = getResolvedValue(tag)
        InputModel model = new InputModel()
        model.put("tag", tag)

        return test(renderMessage('getKey', model)) { resolvedValue != null }
    }

    MapValidator getKey(String key) {
        resolvedValue = getResolvedValue(key)
        InputModel model = new InputModel()
        model.put("tag", key)
        return test(renderMessage('getKey', model)) { resolvedValue != null }
    }

    def getResolvedValue(String key) {
        def result = findDeep(subjectUnderValidation, key)

        if (result instanceof String || result instanceof GString) {
            result = result.trim()
        }
        return result
    }

    @Override
    def getValue() {
        return resolvedValue
    }

    static MapValidator from(Map map) {
        return new MapValidator(map)
    }

    static MapValidator from(Map map, String tag) {
        return new MapValidator(map, tag)
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
