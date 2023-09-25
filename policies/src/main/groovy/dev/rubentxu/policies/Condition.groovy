package dev.rubentxu.policies

import dev.rubentxu.policies.result.ValidationOutcome
import dev.rubentxu.policies.rules.operation.Predicate
import groovy.transform.Canonical

@Canonical
class Condition {

    private String key
    private String value
    private ConditionType type

    boolean appliesTo(InputModel inputModel) {
        // Verificamos el tipo de condición
        switch (type) {
            case ConditionType.EQUALS:
                return predicateResolve(inputModel, '$.value == ' + value, 'Allow')
            case ConditionType.EXISTS:
                return predicateResolve(inputModel, '$.value != null', 'Allow')
            case ConditionType.EXPRESSION:
                // Evaluamos la expresión lógica
                return predicateResolve(inputModel, value, 'Allow')
            default:
                throw new IllegalArgumentException("Tipo de condición no soportado")
        }
    }

    private boolean predicateResolve(InputModel inputModel, String expression, String effect) {
        Predicate predicate = new Predicate(key: key, expressions: [expression], effect: effect)
        ValidationOutcome validationOutcome = predicate.execute(inputModel)
        return validationOutcome.isValid
    }

    private boolean evalExpression(InputModel inputModel) {
        // Evaluamos la expresión lógica
        return evalExpression(inputModel, value)
    }

    private boolean evalExpression(InputModel inputModel, String expression) {
        // Parseamos la expresión lógica
        Binding binding = new Binding(inputModel)
        GroovyShell sh = new GroovyShell(binding)
        def result = sh.evaluate(expression.trim())
        if(result instanceof Boolean){
            return result
        }else{
            throw new IllegalArgumentException("La expresión lógica debe devolver un valor booleano")
        }

    }
}
