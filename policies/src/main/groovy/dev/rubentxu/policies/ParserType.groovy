package dev.rubentxu.policies

enum ParserType {
    YAML,
    JSON,
    XML,
    CSV,
    GROOVY

    @Override
    String toString() {
        return name().toLowerCase()
    }
}