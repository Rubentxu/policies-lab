package dev.rubentxu.policies


import dev.rubentxu.validations.ResultValidation
import dev.rubentxu.validations.ValidationCategory
import groovy.transform.Canonical
import groovy.transform.CompileStatic


@Canonical
class PolicyRule {
    String code
    List<ReferencesResolver> validations
    List<ValidationExceptions> exceptions


    ResultValidation validate(InputModel input) {
        assert this.code: "code in PolicyRule is Required"

        use(ValidationCategory) {
            ResultValidation result = new ResultValidation(isValid: true, errors: [])
            List<ReferencesResolver> validationsToApply = this.validations

            if (this.exceptions) {
                List<ValidationExceptions> exceptionsToApply = this.exceptions.findAll {
//                    topicData.code ==~ ~it.topicName
                    code ==~ ~it.topicName
                }
                if (!exceptionsToApply.isEmpty()) {
                    validationsToApply = exceptionsToApply.collect({ it.validations }).flatten() as List<ReferencesResolver>
                }
            }

            validationsToApply.each { ReferencesResolver validation ->
                String expression = validation.resolveExpression(input)
                ResultValidation resultFromValidationTable = input.validateReferenceResolver(this.code, false)
                        .withExpression(expression)
                        .getResult()
                result.merge(resultFromValidationTable)
            }
            return result
        }
    }

}
