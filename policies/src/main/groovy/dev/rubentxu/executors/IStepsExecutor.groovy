package dev.rubentxu.executors

import java.nio.file.Path

interface IStepsExecutor {

    List<HashMap<String, Object>> readCSV(Path path)

    List<HashMap<String, Object>> readJSON(Path path, String encode)

    Map<String, Object> readYaml(Path path, String encode)

    List<Path> findFiles(Path rootDir, String glob)

    boolean fileExists(Path path)
}