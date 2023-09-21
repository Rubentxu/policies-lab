package dev.rubentxu.policies

import groovy.text.SimpleTemplateEngine
import groovy.transform.Canonical

@Canonical
class ReferencesResolver {
    SimpleTemplateEngine engine = new groovy.text.SimpleTemplateEngine()
    String expression


    String resolveExpression(InputModel inputModel) {
        String finalExpression = expression
        if (expression.contains('$')) {
            finalExpression = replaceValueReference(expression, inputModel)
        }
        return finalExpression
    }


    String resolveMapExpression(InputModel inputModel) {
        String finalExpression = '${' + expression + '}'
        return replaceValueReference(finalExpression, inputModel)
    }


    String replaceValueReference(String expression, InputModel inputModel) {
        Map binding = inputModel
        try {
            def expressionWithResolvedReferences = engine.createTemplate(expression).make(binding)
            return expressionWithResolvedReferences?.toString()
        } catch (Exception e) {
            if (e.message.contains('No such property')) {
                def matcher = e.message =~ /.*No such property: ([\w]+) for/
                if (matcher) {
                    def propertyName = matcher[0][1]
                    String resolvedExpression = expression.replace('$' + propertyName, 'null')
                    return replaceValueReference(resolvedExpression, inputModel)
                }
            }
        }
        return expression
    }

}
