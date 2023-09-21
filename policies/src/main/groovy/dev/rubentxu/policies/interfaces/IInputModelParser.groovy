package dev.rubentxu.policies.interfaces

import dev.rubentxu.policies.InputModel

import java.nio.file.Path


interface IInputModelParser {
    List<InputModel> parseFiles(List<Path> files)
    InputModel parse
}
