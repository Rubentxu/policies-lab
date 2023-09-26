package dev.rubentxu.policies

import dev.rubentxu.executors.IStepsExecutor
import dev.rubentxu.executors.StepsExecutor
import dev.rubentxu.StepsExecutorMock
import dev.rubentxu.policies.input.InputModel
import dev.rubentxu.policies.input.parser.InputModelsParserFactory
import dev.rubentxu.policies.result.PolicyOutcome
import dev.rubentxu.policies.rules.PoliciesParserFactory
import spock.lang.Specification

import java.nio.file.Path

class PoliciesManagerSpec extends Specification {
    PoliciesManager policiesManager
    InputModelsParserFactory factory

    def setup() {
        IStepsExecutor steps = new StepsExecutorMock()
        this.factory = new InputModelsParserFactory(steps)
        def policiesParserFactory = new PoliciesParserFactory(steps: steps)
        this.policiesManager = new PoliciesManager(factory: this.factory, policiesParserFactory: policiesParserFactory, steps: steps)
        Locale.setDefault(new Locale("en", "US"))
    }

    def "ParseTopicPoliciesSpecs"() {

        when:
        List<Policy> policiesRules = policiesManager.parseTopicPoliciesSpecs(Path.of('policies/specs/new_policies.yaml'))
        policiesRules.each {    println(it.toString())}

        then:
        policiesRules.size() > 0
        policiesRules != null


    }

    def "Apply Policies To InputModel type Yaml"() {

        when:
        def results = policiesManager.applyPoliciesToInputModel(Path.of('policies/specs/new_policies.yaml'), ParserType.YAML, Path.of('policies/inputs'))
        results.each { println(it.toString())}

        then:
        results.size() > 0
        results != null
        results.every { it.errors.size() == 0 }
    }

    def "Apply Policies To InputModel type Json"() {

            when:
            def results = policiesManager.applyPoliciesToInputModel(Path.of('policies/specs/new_policies.yaml'), ParserType.JSON, Path.of('policies/inputs'))
            results.each { println(it.toString())}

            then:
            results.size() > 0
            results != null
            results.every { it.errors.size() == 0 }
    }


    def "Policies type CSV not input models found"() {

                when:
                def results = policiesManager.applyPoliciesToInputModel(Path.of('policies/specs/policies_personal_specs.yaml'), ParserType.CSV, Path.of('policies/specs'))
                results.each { println(it.toString())}

                then:
                def e = thrown(AssertionError)
                e.message == "No input models found in path: policies/specs. Expression: (inputModels.size() > 0)"
    }

    def "Policies type CSV not policies found"() {

        when:
        def results = policiesManager.applyPoliciesToInputModel(Path.of('policies/specs/policies_personal.yaml'), ParserType.CSV, Path.of('policies/inputs'))
        results.each { println(it.toString())}

        then:
        def e = thrown(AssertionError)
        e.message == "File Policies specs not found in path: policies/specs/policies_personal.yaml. Expression: steps.fileExists(path)"
    }

    def "Policies not found in file definition"() {

        when:
        def results = policiesManager.applyPoliciesToInputModel(Path.of('policies/specs/policies_personal_specs_error_schema.yaml'), ParserType.CSV, Path.of('policies/inputs'))
        results.each { println(it.toString())}

        then:
        def e = thrown(AssertionError)
        e.message == "Policies not found in file: policies/specs/policies_personal_specs_error_schema.yaml. Expression: data?.policies"
    }

    def "Apply Policies To InputModel type CSV"() {

        when:
        List<PolicyOutcome> results = policiesManager.applyPoliciesToInputModel(Path.of('policies/specs/policies_personal_specs.yaml'), ParserType.CSV, Path.of('policies/inputs'))
        results.each { println(it.toString())}

        then:
        results.size() > 0
        results != null

    }

//    def "Reference map value"() {
//        given:
//        ReferencesResolver resolver = new ReferencesResolver(expression: 'spec.metadata.name')
//        InputModel inputModel = [
//                spec: [
//                        metadata: [
//                                name: 'test'
//                        ]
//                ]
//        ] as InputModel
//
//        when:
//        def result = resolver.evaluateExpression(inputModel)
//
//        then:
//        result == 'test'
//    }
//
//    def "Reference intem value of list"() {
//        given:
//        ReferencesResolver resolver = new ReferencesResolver(expression: 'spec.values[0].name')
//        InputModel inputModel = [
//                spec: [
//                        metadata: [
//                                name: 'test'
//                        ],
//                        values: [
//                                [
//                                        name: 'test'
//                                ],
//                                [
//                                        name: 'test2'
//                                ]
//                        ]
//                ]
//        ] as InputModel
//
//        when:
//        def result = resolver.evaluateExpression(inputModel)
//
//        then:
//        result == 'test2'
//    }
//
//    def "Reference intem value of list spec.template.spec.containers[0].env[name=CONFIG_FILE].value"() {
//        given:
//        ReferencesResolver resolver = new ReferencesResolver(expression: 'spec.template.spec.containers[0].env.find{ it.name == "CONFIG_FILE2" }.value')
//        InputModel inputModel = [
//                spec: [
//                        metadata: [
//                                name: 'test'
//                        ],
//                        template: [
//                                spec: [
//                                        containers: [
//                                                [
//                                                        env: [
//                                                                [
//                                                                        name: 'CONFIG_FILE',
//                                                                        value: 'test'
//                                                                ],
//                                                                [
//                                                                        name: 'CONFIG_FILE2',
//                                                                        value: 'test2'
//                                                                ]
//                                                        ]
//                                                ]
//                                        ]
//                                ]
//                        ]
//                ]
//        ] as InputModel
//
//        when:
//        def result = resolver.evaluateExpression(inputModel)
//
//        then:
//        result == 'test2'
//    }
}
