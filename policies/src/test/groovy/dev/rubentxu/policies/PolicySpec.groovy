package dev.rubentxu.policies

import spock.lang.Specification

class PolicySpec extends Specification {

    def "test appliesTo method"() {
        given:
        Policy policy = new Policy(
                id: "policy1",
                effect: "allow",
                conditions: [
                        new Condition(
                                type: CONDITION_TYPE,
                                key: KEY,
                                value: VALUE
                        )
                ],
                rules: []
        )

        when:
        boolean result = policy.appliesTo(INPUT_MODEL)

        then:
        result == EXPECTED

        where:
        KEY                                                             | VALUE                 |     INPUT_MODEL                       |   CONDITION_TYPE              | EXPECTED
//        'kind'                                                          |  'Deployment'         |       new InputModel(getInputMap())   |  ConditionType.EQUALS         |  true
//        'metadata.name'                                                 |  'peets-deployment'   |       new InputModel(getInputMap())   |  ConditionType.EQUALS         |  true
//        'spec.selector.matchLabels.app'                                 |  'peets'              |       new InputModel(getInputMap())   |  ConditionType.EQUALS         |  true
        'spec.selector.matchLabels.app2'                                |  'peets2'             |       new InputModel(getInputMap())   |  ConditionType.EQUALS         |  false
        'spec.selector.matchLabels.app2'                                |  ''                   |       new InputModel(getInputMap())   |  ConditionType.EXISTS         |  false
        'spec.template.spec.containers[0].ports[0].containerPort'       |  ''                   |       new InputModel(getInputMap())   |  ConditionType.EXISTS         |  true
        'spec.template.spec.containers[0].ports[3].containerPort'       |  ''                   |       new InputModel(getInputMap())   |  ConditionType.EXISTS         |  false
        'spec.template.spec.containers[0].ports[0].containerPort'       |  '$.value > 500m'     |       new InputModel(getInputMap())   |  ConditionType.EXPRESSION     |  false
        'spec.template.spec.containers[0].ports[0].containerPort'       |  '$.value >= 8080'    |       new InputModel(getInputMap())   |  ConditionType.EXPRESSION     |  true
        'spec.template.spec.containers[0].resources.limits.cpu'         |  '$.value == 500'     |       new InputModel(getInputMap())   |  ConditionType.EXPRESSION     |  false
        'spec.template.spec.containers[0].resources.limits.cpu'         |  '$.value == 500m'    |       new InputModel(getInputMap())   |  ConditionType.EXPRESSION     |  true
    }

    Map getInputMap() {
        return [
                "apiVersion": "apps/v1",
                "kind"      : "Deployment",
                "metadata"  : [
                        "name": "peets-deployment"
                ],
                "spec"      : [
                        "replicas": 3,
                        "selector": [
                                "matchLabels": [
                                        "app": "peets"
                                ]
                        ],
                        "template": [
                                "metadata": [
                                        "labels": [
                                                "app": "peets"
                                        ]
                                ],
                                "spec"    : [
                                        "affinity"  : [
                                                "podAntiAffinity": [
                                                        "preferredDuringSchedulingIgnoredDuringExecution": [
                                                                [
                                                                        "podAffinityTerm": [
                                                                                "labelSelector": [
                                                                                        "matchExpressions": [
                                                                                                [
                                                                                                        "key"     : "app",
                                                                                                        "operator": "In",
                                                                                                        "values"  : [
                                                                                                                "peets"
                                                                                                        ]
                                                                                                ]
                                                                                        ]
                                                                                ],
                                                                                "topologyKey"  : "kubernetes.io/hostname"
                                                                        ],
                                                                        "weight"         : 100
                                                                ]
                                                        ]
                                                ]
                                        ],
                                        "containers": [
                                                [
                                                        "name"          : "peets",
                                                        "image"         : "mi-repositorio/peets:latest",
                                                        "ports"         : [
                                                                [
                                                                        "containerPort": 8080
                                                                ]
                                                        ],
                                                        "env"           : [
                                                                [
                                                                        "name" : "SPRING_PROFILES_ACTIVE",
                                                                        "value": "production"
                                                                ],
                                                                [
                                                                        "name" : "CONFIG_FILE",
                                                                        "value": "/etc/peets/config.yml"
                                                                ]
                                                        ],
                                                        "resources"     : [
                                                                "limits"  : [
                                                                        "cpu"   : "500m",
                                                                        "memory": "512Mi"
                                                                ],
                                                                "requests": [
                                                                        "cpu"   : "100m",
                                                                        "memory": "128Mi"
                                                                ]
                                                        ],
                                                        "volumeMounts"  : [
                                                                [
                                                                        "name"     : "peets-config",
                                                                        "mountPath": "/etc/peets",
                                                                        "readOnly" : true
                                                                ]
                                                        ],
                                                        "livenessProbe" : [
                                                                "httpGet"            : [
                                                                        "path": "/actuator/health",
                                                                        "port": 8080
                                                                ],
                                                                "initialDelaySeconds": 30,
                                                                "periodSeconds"      : 10,
                                                                "timeoutSeconds"     : 1
                                                        ],
                                                        "readinessProbe": [
                                                                "httpGet"            : [
                                                                        "path": "/actuator/health",
                                                                        "port": 8080
                                                                ],
                                                                "initialDelaySeconds": 5,
                                                                "periodSeconds"      : 5,
                                                                "timeoutSeconds"     : 1
                                                        ]
                                                ]
                                        ],
                                        "volumes"   : [
                                                [
                                                        "name"     : "peets-config",
                                                        "configMap": [
                                                                "name": "peets-configmap"
                                                        ]
                                                ]
                                        ]
                                ]
                        ]
                ]
        ]


    }


}