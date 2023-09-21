package dev.rubentxu.validations

import spock.lang.Shared
import spock.lang.Specification


class NumberValidationSpec extends Specification {

    @Shared
    Number valueNull = null

    def 'Debe validar expresion logica'(Number VALUE, String EXPRESION, boolean RESULTADO, String ERROR_MSG) {
        given:
        ResultValidation result = null

        when:
        use(ValidationCategory) {
            result = VALUE.validate('TestValue', false)
                    .isNumber()
                    .withExpression(EXPRESION)
                    .getResult()

        }
        then:
        result.isValid == RESULTADO
        result.errors[0] == ERROR_MSG

        where:

        where:
        VALUE       |  EXPRESION        | RESULTADO     | ERROR_MSG
        1000        | '<2000'           | true          | null
        1000        | '<999.99'         | false         | 'TestValue with value 1000 Must be lower than 999.99'
        1000        | '< 2000'         | true          | null
        1000        | '< 2000   '       | true          | null
        1000        | '  < 2000   '     | true          | null
        1000        | '< 2000 aas   '   | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for TestValue with  1000 < 2000 aas'
        1000        | ' a < 2000'       | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for TestValue with  1000 a < 2000'
        1000        | '< s2000'         | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for TestValue with  1000 < s2000'
        1000        | '> 500'           | true          | null
        1000        | '> 1500'          | false         | 'TestValue with value 1000 Must be greater than 1500'
        1000.99     | '< 2000.99'       | true          | null
        1000.09     | '> 500.99 s'      | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for TestValue with  1000.09 > 500.99 s'
        1000.09     | '>= 500.99'       | true          | null
        1000.09     | '>= 1000.09'      | true          | null
        1000.09     | '>= 1000.19'      | false         | 'TestValue with value 1000.09 Must be more or equal to 1000.19'
        1000.09     | '<= 1500.99'      | true          | null
        1000.09     | '<= 1000.09'      | true          | null
        1000.09     | '<= 1000.001'     | false         | 'TestValue with value 1000.09 Must be less or equal to 1000.001'
        1000        | '==1000'          | true          | null
        1000.09     | '==1000.01'       | false         | 'TestValue with value 1000.09 Must be equal to 1000.01'
        1000.09     | '!=1000.01'       | true          | null
        1000.09     | ' != 1000.01 '    | true          | null
        1000.09     | '!=1000.09'       | false         | 'TestValue with value 1000.09 Must be not equal to 1000.09'
        1000.09     | '!= 1000.09'      | false         | 'TestValue with value 1000.09 Must be not equal to 1000.09'
        1000.09     | '!= 1000.09 '     | false         | 'TestValue with value 1000.09 Must be not equal to 1000.09'
        1000.09     | '!= 1000.09  '    | false         | 'TestValue with value 1000.09 Must be not equal to 1000.09'
        1000.09     | '!= 1000.09  a'   | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for TestValue with  1000.09 != 1000.09  a'
        1000.09     | '!= 1000.09  a '  | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for TestValue with  1000.09 != 1000.09  a'
        1000.09     | '!= 1000.09  a  ' | false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for TestValue with  1000.09 != 1000.09  a'
        1000.09     | '!= 1000.09  a  s'| false         | 'Syntax Error, invalid expression. The expression did not evaluate to true for TestValue with  1000.09 != 1000.09  a  s'
        1000        | '100..1001'       | true          | null
        1000.09     | '100.09..1001'    | true          | null
        1000.09     | '100.09..1000.09' | true          | null
        1000.09     | '100.09..1000.08' | false         | 'TestValue with value 1000.09 Must be between 100.09 and 1000.08'
        100.08      | '100.09..1000.08' | false         | 'TestValue with value 100.08 Must be between 100.09 and 1000.08'
        1000.09     | '100.09..1000.10' | true          | null
        1000.09     | '100.09..1000.10' | true          | null
        1000.09     | '100.09..1000.10' | true          | null
        1000.09     | '100.09..1000.10' | true          | null
        1000.09     | '!=NULL'          | true          | null
        1000.09     | '==NULL'          | false         | 'TestValue with value 1000.09 Must be null'
        1000.09     | '!=null'          | true          | null
        1000.09     | '==null'          | false         | 'TestValue with value 1000.09 Must be null'
        10000       | '< 10*100*4-24+18000/2'  | true         | null

    }

    def 'Debe validar expresion logica para expresiones con nulos'(String EXPRESION, boolean RESULTADO, String ERROR_MSG) {

        given:
        NumberValidation result = null
        Number value = null

        when:

        use(ValidationCategory) {
            result = value.validate('TestValue', false)
                    .isNumber()
                    .withExpression(EXPRESION)

        }
        then:
        result.isValid() == RESULTADO
        result.onErrorMessages[0] == ERROR_MSG

        where:

        EXPRESION          | RESULTADO     | ERROR_MSG
         '!=NULL'          | false         | 'TestValue with value null Must not be null'
         '==NULL'          | true          | null

    }

}
