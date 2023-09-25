package dev.rubentxu.policies.rules

import dev.rubentxu.policies.Policy
import dev.rubentxu.policies.Rule

import java.nio.file.Path

interface PoliciesParser {
    List<Policy> parseFile(Path path)
}
