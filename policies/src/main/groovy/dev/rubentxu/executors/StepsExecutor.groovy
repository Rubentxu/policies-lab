package dev.rubentxu.executors

import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import dev.rubentxu.policies.input.InputModel
import groovy.json.JsonSlurper
import groovy.util.logging.Log
import groovy.yaml.YamlSlurper

import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.PathMatcher

@Log
class StepsExecutor implements  IStepsExecutor {

    @Override
    boolean fileExists(Path path) {
        return Files.exists(path)
    }

    @Override
    List<HashMap<String, Object>> readCSV(Path path, String encode) {
        List<InputModel> list = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(path.toString())).build()) {
            String[] line;
            String[] header = null;
            if ((line = reader.readNext()) != null) {
                header = line;
            }
            while ((line = reader.readNext()) != null) {
                InputModel obj = new InputModel();
                for (int i = 0; i < line.length; i++) {
                    obj.put(header[i], line[i]);
                }
                list.add(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    List<HashMap<String, Object>> readJSON(Path path, String encode) {
        String fileText = path.toFile().getText(encode)
        def jsonSlurper = new JsonSlurper()
        def jsonObject = jsonSlurper.parseText(fileText)

        // Podemos asumir que el JSON root es un objeto o un array en base al uso
        if (jsonObject instanceof Map) {
            return [jsonObject]
        } else if (jsonObject instanceof List) {
            return jsonObject
        } else {
            throw new RuntimeException("Unexpected root JSON type. Expected Map or List, but got ${jsonObject.getClass()}")
        }
    }

    @Override
    Map<String, Object> readYaml(Path path, String encode) {
        log.info("Reading YAML file: ${path}")
        String fileText = path.toFile().getText(encode)

        log.info("File text: ${fileText}")
//        def yaml = new Yaml()
//        yaml.load(fileText)
        def parsedYaml = new YamlSlurper().parseText(fileText)

        println(parsedYaml)

        return parsedYaml
    }

    @Override
    List<Path> findFiles(Path rootDir, String glob) {
        def fileTree = []
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + glob)
        Files.walk(rootDir).forEach { path ->
            if (Files.isRegularFile(path) && pathMatcher.matches(path)) {
                fileTree << path
            }
        }
        return fileTree
    }
}
