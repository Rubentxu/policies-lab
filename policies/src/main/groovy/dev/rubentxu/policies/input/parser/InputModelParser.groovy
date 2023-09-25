package dev.rubentxu.policies.input.parser

import dev.rubentxu.policies.input.InputModel

import java.nio.file.Path


interface InputModelParser {
    List<InputModel> parseFiles(List<Path> files)

}
