package dev.rubentxu.validations

import dev.rubentxu.policies.result.ValidationOutcome
import spock.lang.Shared
import spock.lang.Specification


class NumberValidatorSpec extends Specification {

    def setup() {
        Locale.setDefault(new Locale("en", "US"))
    }


    @Shared
    Number valueNull = null

    def 'Debe validar expresion logica'(Number VALUE, String EXPRESION, boolean RESULTADO, String ERROR_MSG) {
        given:
        ValidationOutcome result = null

        when:
        use(ValidatorCategory) {
            result = VALUE.validate('TestValue')
                    .isNumber()
                    .withExpression(EXPRESION)
                    .getResult()

        }
        then:
        result.isValid == RESULTADO
        result.errors[0] == ERROR_MSG

        where:

        where:
        VALUE       |  EXPRESION                | RESULTADO     | ERROR_MSG
//        1000        | '$.value <2000'           | true          | null
//        1000        | '$.value<999.99'          | false         | 'The expression did not evaluate to true for Key \'TestValue\' with expression \'1000<999.99\''
//        1000        | '$.value < 2000'          | true          | null
//        1000        | '$.value< 2000   '        | true          | null
//        1000        | '$.value   < 2000   '     | true          | null
//        1000        | '$.value < 2000 aas   '   | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for Key \'TestValue\' with expression \'1000 < 2000 aas\''
//        1000        | '$.value  a < 2000'       | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for Key \'TestValue\' with expression \'1000  a < 2000\''
//        1000        | '$.value < s2000'         | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for Key \'TestValue\' with expression \'1000 < s2000\''
//        1000        | '$.value > 500'           | true          | null
//        1000        | '$.value > 1500'          | false         | 'The expression did not evaluate to true for Key \'TestValue\' with expression \'1000 > 1500\''
//        1000.99     | '$.value < 2000.99'       | true          | null
//        1000.09     | '$.value > 500.99 s'      | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 > 500.99 s\''
//        1000.09     | '$.value >= 500.99'       | true          | null
//        1000.09     | '$.value >= 1000.09'      | true          | null
//        1000.09     | '$.value >= 1000.19'      | false         | 'The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 >= 1000.19\''
//        1000.09     | '$.value <= 1500.99'      | true          | null
//        1000.09     | '$.value <= 1000.09'      | true          | null
//        1000.09     | '$.value <= 1000.001'     | false         | 'The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 <= 1000.001\''
//        1000        | '$.value ==1000'          | true          | null
//        1000.09     | '$.value ==1000.01'       | false         | 'The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 ==1000.01\''
//        1000.09     | '$.value !=1000.01'       | true          | null
//        1000.09     | '$.value  != 1000.01 '    | true          | null
//        1000.09     | '$.value !=1000.09'       | false         | 'The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 !=1000.09\''
//        1000.09     | '$.value != 1000.09'      | false         | 'The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 != 1000.09\''
//        1000.09     | '$.value != 1000.09 '     | false         | 'The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 != 1000.09\''
//        1000.09     | '$.value != 1000.09  '    | false         | 'The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 != 1000.09\''
//        1000.09     | '$.value != 1000.09  a'   | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 != 1000.09  a\''
//        1000.09     | '$.value != 1000.09  a '  | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 != 1000.09  a\''
//        1000.09     | '$.value != 1000.09  a  ' | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 != 1000.09  a\''
//        1000.09     | '$.value != 1000.09  a  s'| false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for Key \'TestValue\' with expression \'1000.09 != 1000.09  a  s\''
//        1000        | '100..1001'               | true          | null
//        1000.09     | '100.09..1001'            | true          | null
//        1000.09     | '100.09..1000.09'         | true          | null
//        1000.09     | '100.09..1000.08'         | false         | 'TestValue with value 1000.09 Must be between 100.09 and 1000.08'
//        100.08      | '100.09..1000.08'         | false         | 'TestValue with value 100.08 Must be between 100.09 and 1000.08'
//        1000.09     | '100.09..1000.10'         | true          | null
//        1000.09     | '100.09..1000.10'         | true          | null
//        1000.09     | '100.09..1000.10'         | true          | null
//        1000.09     | '100.09..1000.10'         | true          | null
//        1000.09     | '$.value !=NULL'          | true          | null
//        1000.09     | '$.value ==NULL'          | false         | 'TestValue with value 1000.09 Must be null'
//        1000.09     | '$.value !=null'          | true          | null
//        1000.09     | '$.value ==null'          | false         | 'TestValue with value 1000.09 Must be null'
        10000       | '$.value < 10*100*4-24+18000'     | true         | null

    }

    def 'Debe validar expresion logica para expresiones con nulos'(String EXPRESION, boolean RESULTADO, String ERROR_MSG) {

        given:
        NumberValidator result = null
        Number value = null

        when:

        use(ValidatorCategory) {
            result = value.validate('TestValue')
                    .isNumber()
                    .withExpression(EXPRESION)

        }
        then:
        result.isValid() == RESULTADO
        result.errors()[0] == ERROR_MSG
        result.errors()[1] == ERROR_MSG2

        where:

        EXPRESION          | RESULTADO     | ERROR_MSG                                          | ERROR_MSG2
         '!=NULL'          | false         | 'TestValue with value null Must be type Number.'   | 'TestValue with value null Must not be null'
         '== null'         | false          | 'TestValue with value null Must be type Number.'   | 'TestValue with value null Must be null'

    }

}
