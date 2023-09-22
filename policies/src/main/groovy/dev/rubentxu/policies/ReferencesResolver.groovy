package dev.rubentxu.policies

import groovy.text.SimpleTemplateEngine
import groovy.transform.Canonical

@Canonical
class ReferencesResolver {
    SimpleTemplateEngine engine = new SimpleTemplateEngine()
    String expression


    def resolve(InputModel inputModel) {
        if (expression.contains('$')) {
           return renderTemplateString(inputModel)
        } else {
            return evaluateExpression(inputModel)
        }
    }


    def evaluateExpression(InputModel inputModel) {
        try {
            Binding binding = new Binding(inputModel)
            GroovyShell sh = new GroovyShell(binding)
            def result = sh.evaluate(expression.trim())
            return result
        } catch (Exception e) {
            String noSuchProperty = noSuchPropertyMessage(e)
            throw new RuntimeException("Error evaluating expression: ${expression.trim()} ${noSuchProperty.trim()}".toString().trim())
        }
    }


    String renderTemplateString(InputModel inputModel) {
        try {
            Writable expressionWithResolvedReferences = engine.createTemplate(expression.trim()).make(inputModel)
            return expressionWithResolvedReferences?.toString()
        } catch (Exception e) {
            String noSuchProperty = noSuchPropertyMessage(e)
            throw new RuntimeException("Error rendering template string: '${expression.trim()}' ${noSuchProperty.trim()}".toString().trim())
        }

    }

    private String noSuchPropertyMessage(Exception e) {
        String noSuchProperty = ''
        def matcher = e.getMessage() =~ /No such property: \w+/

        if (matcher) {
            noSuchProperty = matcher[0]  // Imprimirá: No such property: address
        }
        return noSuchProperty
    }

}
