package dev.rubentxu.policies


import groovy.text.SimpleTemplateEngine

class ExpressionEvaluator {
    static SimpleTemplateEngine engine = new SimpleTemplateEngine()

    static def evaluate(InputModel inputModel, String expression) {
        if (expression.contains('$')) {
            return renderTemplateString(inputModel, expression)
        } else {
            return evaluateExpression(inputModel, expression)
        }
    }

    static def evaluateExpression(InputModel inputModel, String expression) {
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

    static String renderTemplateString(InputModel inputModel, String expression) {
        try {
            Writable expressionWithResolvedReferences = engine.createTemplate(expression.trim()).make(inputModel)
            return expressionWithResolvedReferences?.toString()
        } catch (Exception e) {
            String noSuchProperty = noSuchPropertyMessage(e)
            throw new RuntimeException("Error rendering template string: '${expression.trim()}' ${noSuchProperty.trim()}".toString().trim())
        }

    }

    static private String noSuchPropertyMessage(Exception e) {
        String noSuchProperty = ''
        def matcher = e.getMessage() =~ /No such property: \w+/

        if (matcher) {
            noSuchProperty = matcher[0]  // Imprimirá: No such property: address
        } else {
            def matcher2 = e.getMessage() =~ /Cannot get property '\w+'/
            noSuchProperty = matcher2[0]
        }
        return noSuchProperty
    }

    // Función para resolver una subexpresión
    static String resolveComparisonExpression(String subExpression) {
        def pattern = ~/(\d+)([a-zA-Z]*)\s*([<>=]+)\s*(\d+)([a-zA-Z]*)/
        def matcher = pattern.matcher(subExpression)

        // Si la subexpresión coincide con el patrón, crea una nueva expresión con la comparación de valores y magnitudes
        if (matcher.find()) {
            subExpression = subExpression.replace("${matcher.group(1)}${matcher.group(2)}", matcher.group(1))
            subExpression = subExpression.replace("${matcher.group(4)}${matcher.group(5)}", matcher.group(4))

            return "(${subExpression} && '${matcher.group(2)}' == '${matcher.group(5)}')"
        }

        // Si la subexpresión no coincide con el patrón, devuelve la subexpresión original entre paréntesis
        return "(${subExpression})"
    }

// Función para dividir la expresión en subexpresiones y operadores lógicos
    static List splitIntoSubExpressions(String longExpression) {
        return longExpression.split(/\s*(&&|\|\|)\s*/)
    }

// Función para encontrar los operadores lógicos en la expresión
    static List findLogicalOperators(String longExpression) {
        return longExpression.findAll(/&&|\|\|/)
    }

// Función para combinar subexpresiones y operadores lógicos en una lista
    static List combineSubExpressionsAndOperators(List<String> splitExpression, List<String> logicalOperators) {
        List expressionWithOperators = []
        for (int i = 0; i < splitExpression.size(); i++) {
            expressionWithOperators.add(splitExpression[i])
            if (i < logicalOperators.size()) {
                expressionWithOperators.add(logicalOperators[i])
            }
        }
        return expressionWithOperators
    }

// Función para resolver una expresión completa
    static String sanitizeExpression(String expression) {

        // Dividir la expresión, encontrar operadores lógicos, y combinarlos en una lista
        List splitExp = splitIntoSubExpressions(expression)
        List logicalOps = findLogicalOperators(expression)
        List expressionWithOperators = combineSubExpressionsAndOperators(splitExp, logicalOps)

        // Resolver cada subexpresión y unirlas con los operadores lógicos
        return expressionWithOperators.collect {
            it in ["&&", "||"] ? it : resolveComparisonExpression(it)
        }.collect {
            it in ["&&", "||"] ? it : resolveWords(it)
        }.join(' ')
    }


    static String resolveWords(String subExpression) {
        def pattern = ~/([a-zA-Z]*)\s*([<>=]+)\s*([a-zA-Z]*)/
        def matcher = pattern.matcher(subExpression)

        // Si la subexpresión coincide con el patrón, crea una nueva expresión con la comparación de valores y magnitudes
        if (matcher.find()) {
            if(matcher.group(1)){
                subExpression =  subExpression.replace(matcher.group(1), "'${matcher.group(1)}'")
            }
            if(matcher.group(3) && matcher.group(3) != matcher.group(1)){
                subExpression = subExpression.replace(matcher.group(3), "'${matcher.group(3)}'")
            }

        }

        // Si la subexpresión no coincide con el patrón, devuelve la subexpresión original entre paréntesis
        return "${subExpression}"
    }
}
