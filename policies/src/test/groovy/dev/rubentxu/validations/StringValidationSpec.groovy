package dev.rubentxu.validations

import dev.rubentxu.policies.result.ValidationOutcome
import spock.lang.Specification

class StringValidationSpec extends Specification {


    def 'Debe validar expresion logica tipo string'(String VALUE, String EXPRESION, boolean RESULTADO, String ERROR_MSG) {

        given:
        ValidationOutcome result = null


        when:

        use(ValidationCategory) {
            result = VALUE.validate('TestValue', false)
                    .isString()
                    .withExpression(EXPRESION)
                    .getResult()

        }
        then:
        result.isValid == RESULTADO
        result.errors[0] == ERROR_MSG

        where:

        where:
        VALUE            |  EXPRESION                        | RESULTADO     | ERROR_MSG
        'compact'        | '== "compact"'                    | true          | null
        'compact'        | '!= "compact"'                    | false         | 'TestValue with value compact Must be not equal to compact'
        'patata'         | '!= "compact"'                    | true          | null
        'patata'         | '!= compact'                      | true          | null
        'compact'        | '== compact'                      | true          | null
        'patata'         | '== compact'                      | false         | 'TestValue with value patata Must be equal to compact'
        'patata'         | '== "compact"'                    | false         | 'TestValue with value patata Must be equal to compact'
        'compact'        | 'IN ["compact", "delete"]'        | true          | null
        'delete'         | 'IN ["compact", "delete"]'        | true          | null
        'patata'         | 'IN ["compact", "delete"]'        | false         | 'TestValue with value patata Must contain in compact,delete'
        null             | 'IN ["compact", "delete"]'        | false         | 'TestValue with value null Must be type String.'



    }


    def 'Debe validar expresion logica tipo string template'(String VALUE, String EXPRESION, boolean RESULTADO, String ERROR_MSG) {

        given:
        ValidationOutcome result = null


        when:

        use(ValidationCategory) {
            result = TEMPLATE.validate('TestValue', false)
                    .isString()
                    .renderValue([val: VALUE])
                    .withExpression(EXPRESION)
                    .getResult()

        }
        then:
        result.isValid == RESULTADO
        result.errors[0] == ERROR_MSG

        where:

        where:
        TEMPLATE |    VALUE            |  EXPRESION                        | RESULTADO     | ERROR_MSG
        '${val}' |    'compact'        | '== "compact"'                    | true          | null
        '${val}' |    'compact'        | '!= "compact"'                    | false         | 'TestValue with value compact Must be not equal to compact'
        '${val}' |    'patata'         | '!= "compact"'                    | true          | null
        '${val}' |    'patata'         | '!= compact'                      | true          | null
        '${val}' |    'compact'        | '== compact'                      | true          | null
        '${val}' |    'patata'         | '== compact'                      | false         | 'TestValue with value patata Must be equal to compact'
        '${val}' |    'patata'         | '== "compact"'                    | false         | 'TestValue with value patata Must be equal to compact'
        '${val}' |    'compact'        | 'IN ["compact", "delete"]'        | true          | null
        '${val}' |    'delete'         | 'IN ["compact", "delete"]'        | true          | null
        '${val}' |    'patata'         | 'IN ["compact", "delete"]'        | false         | 'TestValue with value patata Must contain in compact,delete'




    }

}
