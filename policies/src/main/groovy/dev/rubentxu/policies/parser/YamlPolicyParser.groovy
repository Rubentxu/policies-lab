package dev.rubentxu.policies.parser

import dev.rubentxu.executors.StepsExecutor
import dev.rubentxu.policies.ConditionType
import dev.rubentxu.policies.Policy
import dev.rubentxu.policies.RuleType
import dev.rubentxu.policies.rules.PoliciesParser
import dev.rubentxu.policies.rules.operation.Modify
import dev.rubentxu.policies.rules.operation.Predicate
import dev.rubentxu.policies.rules.operation.RuleOperation
import dev.rubentxu.policies.Condition
import groovy.transform.Canonical
import groovy.util.logging.Log

import java.nio.file.Path


@Log
@Canonical
class YamlPolicyParser implements PoliciesParser {

    StepsExecutor steps
    Map<Path, List<Policy>> policiesByPath = [:]

    @Override
    List<Policy> parseFile(Path path) {
        log.info("Parsing topic policies specs from path: ${path}")
        assert steps.fileExists(path): "File Policies specs not found in path: ${path}"
        if (!policiesByPath.containsKey(path)) {
            List<Policy> policies = []
            Map data = steps.readYaml(path, 'UTF-8')

            assert data?.policies: "Policies not found in file: ${path}"

            data?.policies.collect { policyMap ->
                def policy = new Policy(
                        id: policyMap.id,
                        effect: policyMap.effect,
                        conditions: policyMap?.conditions.collect { conditionMap ->
                            new Condition(
                                    key: conditionMap.key,
                                    value: conditionMap.value,
                                    type: conditionMap.type.toString().toUpperCase() as ConditionType
                            )
                        },
                        rules: policyMap?.rules.collect { ruleMap ->
                            resolveOperation(policyMap.effect, ruleMap)
                        }
                )
                if(policiesByPath.containsKey(path)) {
                    policiesByPath.get(path).add(policy)
                } else {
                    policiesByPath.put(path, [policy])
                }

            }

        }
        log.info("Parsed topic policies specs")
        return policiesByPath.get(path)

    }


   private RuleOperation resolveOperation(String effect, Map rule) {
        RuleType type = rule.type as RuleType
        switch (type) {
            case RuleType.VALIDATION:
                return new Predicate(
                        key: rule.key,
                        expressions: rule.expressions,
                        effect: effect
                )
            case RuleType.MODIFICATION:
                return new Modify(
                        key: rule.key,
                        value: rule.value,

                )
            default:
                throw new IllegalArgumentException("Invalid rule type: ${type}")
        }
    }
}
