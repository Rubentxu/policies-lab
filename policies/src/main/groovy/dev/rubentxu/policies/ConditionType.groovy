package dev.rubentxu.policies

enum ConditionType {
    EQUALS,
    EXISTS,
    EXPRESSION

    @Override
    String toString() {
        return name().toLowerCase()
    }
}