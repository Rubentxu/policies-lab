package dev.rubentxu.policies

import dev.rubentxu.executors.StepsExecutor
import dev.rubentxu.StepsExecutorMock
import dev.rubentxu.policies.interfaces.IPoliciesManager
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths


class PoliciesManagerSpec extends Specification {
    IPoliciesManager policiesManager
    InputModelsParserFactory factory

    def setup() {
        StepsExecutor steps = new StepsExecutorMock()
        this.factory = new InputModelsParserFactory(steps)
        this.policiesManager = new PoliciesManager(steps: steps, factory: this.factory)
    }

    def "ParseTopicPoliciesSpecs"() {

        when:
        List<PolicyRule> policiesRules = policiesManager.parseTopicPoliciesSpecs('policies/specs/policies_specs.yaml')
        policiesRules.each {    println(it.toString())}

        then:
        policiesRules.size() > 0
        policiesRules != null


    }

    def "Apply Policies To InputModel type Yaml"() {

        when:
        def results = policiesManager.applyPoliciesToInputModel(Path.of('policies/specs/policies_specs.yaml'), 'yaml', Path.of('policies/specs'))
        results.each { println(it.toString())}

        then:
        results.size() > 0
        results != null
        results.every { it.errors.size() == 0 }
    }

    def "Apply Policies To InputModel type Json"() {

            when:
            def results = policiesManager.applyPoliciesToInputModel(Path.of('policies/specs/policies_specs.yaml'), 'json', Path.of('policies/specs'))
            results.each { println(it.toString())}

            then:
            results.size() > 0
            results != null
            results.every { it.errors.size() == 0 }
    }


    def "Apply Policies To InputModel type CSV"() {

                when:
                def results = policiesManager.applyPoliciesToInputModel(Path.of('policies/specs/policies_personal_specs.yaml'), 'csv', Path.of('policies/specs'))
                results.each { println(it.toString())}

                then:
                results.size() > 0
                results != null
                results.every { it.errors.size() == 0 }
    }

    def "Reference map value"() {
        given:
        ReferencesResolver resolver = new ReferencesResolver(expression: 'spec.metadata.name')
        InputModel inputModel = [
                spec: [
                        metadata: [
                                name: 'test'
                        ]
                ]
        ] as InputModel

        when:
        def result = resolver.evaluateExpression(inputModel)

        then:
        result == 'test'
    }

    def "Reference intem value of list"() {
        given:
        ReferencesResolver resolver = new ReferencesResolver(expression: 'spec.values[0].name')
        InputModel inputModel = [
                spec: [
                        metadata: [
                                name: 'test'
                        ],
                        values: [
                                [
                                        name: 'test'
                                ],
                                [
                                        name: 'test2'
                                ]
                        ]
                ]
        ] as InputModel

        when:
        def result = resolver.evaluateExpression(inputModel)

        then:
        result == 'test2'
    }

    def "Reference intem value of list spec.template.spec.containers[0].env[name=CONFIG_FILE].value"() {
        given:
        ReferencesResolver resolver = new ReferencesResolver(expression: 'spec.template.spec.containers[0].env.find{ it.name == "CONFIG_FILE2" }.value')
        InputModel inputModel = [
                spec: [
                        metadata: [
                                name: 'test'
                        ],
                        template: [
                                spec: [
                                        containers: [
                                                [
                                                        env: [
                                                                [
                                                                        name: 'CONFIG_FILE',
                                                                        value: 'test'
                                                                ],
                                                                [
                                                                        name: 'CONFIG_FILE2',
                                                                        value: 'test2'
                                                                ]
                                                        ]
                                                ]
                                        ]
                                ]
                        ]
                ]
        ] as InputModel

        when:
        def result = resolver.evaluateExpression(inputModel)

        then:
        result == 'test2'
    }
}
