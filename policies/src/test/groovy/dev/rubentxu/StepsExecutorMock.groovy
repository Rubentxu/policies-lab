package dev.rubentxu

import dev.rubentxu.executors.IStepsExecutor
import dev.rubentxu.executors.StepsExecutor
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap

//@CompileStatic
class StepsExecutorMock extends StepsExecutor implements IStepsExecutor {

    Map<String, Object> dynamicProps
    Map<String, List<Map>> callTraces

    StepsExecutorMock() {
        dynamicProps = [
                env               : [:],
                params            : [:],
                currentBuild      : [:],
                configFileProvider: this.&defaultMethodClosure,
                ansiColor         : this.&defaultMethodClosure,
                container         : this.&defaultMethodClosure,
                dir               : this.&dirMock,
                readYaml          : this.&readYamlMock,
                readJSON          : this.&readJsonMock,
                readFile          : this.&readFileMock,
                fileExists        : this.&fileExistsMock,
                withCredentials   : this.&defaultMethodClosure,
                node              : this.&defaultMethodClosure,
                stage             : this.&defaultMethodClosure,
                configFile        : {},
                writeFile         : {},
                writeJSON         : this.&writeJSONMock,
                writeYaml         : this.&writeYAMLMock,
                writeMavenPom     : {},
                usernamePassword  : {},
                publishHTML       : {},
                parallel          : this.&parallelMock,
                sleep             : {},
                string            : {},
                file              : {},
                sh                : { '' },
                echo              : { System.out.println(it.toString()) },
                error             : { String message -> throw new Exception(message) },
        ]
        callTraces = new ConcurrentHashMap()
    }

    List findCallTracesByMethod(String methodName) {
        return callTraces[methodName] ?: []
    }

    def defaultMethodClosure(_, closure) {
        closure.delegate = this
        return closure()
    }



    def dirMock(String path, Closure block) {
        def currentPwd = dynamicProps.env?.PWD
        def currentDir = currentPwd ?: '.'
        dynamicProps.env['PWD'] = Paths.get(currentDir).toAbsolutePath().resolve(path).toString()
        def result = block()
        if (currentPwd) {
            dynamicProps.env.PWD = currentPwd
        } else {
            (dynamicProps.env as Map).remove('PWD')
        }
        return result
    }

    def parallelMock(Map args) {
        args.findAll { k, v -> k != 'failFast' }.each { k, v -> v() }
    }

//    def readYamlMock(Map args, baseDir = '') {
//        String content = args.text ?: this.getResourceContent(Paths.get(baseDir as String, args.file as String) as String)
//        if(content.contains('---')) {
//            def result =  new Yaml().loadAll(content).toList()
//            result.size() == 1 ? result[0] : result
//        } else {
//            return new Yaml().load(content)
//        }
//    }



    def readCSVMock(Map args, baseDir = '') {
        String content = args.text ?: this.getResourceContent(Paths.get(baseDir as String, args.file as String) as String)
        re

    }

    def readJsonMock(Map args, baseDir = '') {
        String content = args.text ?: this.getResourceContent(Paths.get(baseDir as String, args.file as String) as String)
        return new JsonSlurper().parseText(content ?: '')
    }

    def findFilesMock(Map args, baseDir = 'topics') {
        Path parentPath = Paths.get(getClass().getClassLoader().getResource(baseDir as String).toURI()).resolve('../').normalize()

        List<File> files = new ArrayList<>()
        parentPath.toFile().eachFileRecurse(FileType.FILES) {
            def relativePath = parentPath.relativize(it.toPath())
            if (matchesGlobExpression(args.glob as String, relativePath)) {
                files.add(relativePath.toFile())
            }
        }
        return files
    }

    private Boolean matchesGlobExpression(String globPattern, Path path) {
        return FileSystems
                .getDefault()
                .getPathMatcher("glob:${globPattern}")
                .matches(path)
    }

    def readFileMock(Map args, String baseDir = '') {
        return this.getResourceContent(Paths.get(baseDir, args.file as String) as String)
    }

    def writeJSONMock(Map args) {
        if (args.returnText) {
            JsonBuilder builder = new JsonBuilder()
            builder(args.json)
            builder.toString()
        }
    }

//    def writeYAMLMock(Map args) {
//        if (args.returnText) {
//            return new Yaml().load(args.data as String ?: args.datas as String)
//        }
//    }

    def fileExistsMock(Map args, String baseDir = '') {
        return getClass().getClassLoader().getResource(Paths.get(baseDir, args.file as String) as String) != null ? Boolean.TRUE : Boolean.FALSE
    }



    Object sleep(long arg) {
        // To simulate steps.sleep(time) in a pipeline
        methodMissing('sleep', [arg])
    }

    def getAllDynamicProps() {
        return dynamicProps
    }

    @Override
    def getProperty(String propName) {
        return dynamicProps[propName]
    }

    @Override
    void setProperty(String propName, val) {
        dynamicProps[propName] = val
    }

    def methodMissing(String methodName, args) {
        if (!this.callTraces.containsKey(methodName)) {
            this.callTraces[methodName] = []
        }
        this.callTraces[methodName] << [args: args, invocationNumber: this.callTraces[methodName].size() + 1 as Integer]

        def prop = dynamicProps[methodName]
        if (prop instanceof Closure) {
            return prop(*args)
        }
        throw new Exception("\u001B[1;31m************ Method Missing with name $methodName and args $args **************\u001B[0m")
    }

    String getResourceContent(String file) {
        return getClass().getClassLoader().getResource(file.replaceAll("\\\\", "/"))?.text
    }

    @Override
    List<HashMap<String, Object>> readCSV(Path path) {
        URL contentFile = getClass().getClassLoader().getResource(path.toString())
        if(!contentFile) {
            return super.readCSV(path, encode)
        }
        return super.readCSV(Paths.get(contentFile.toURI()))

    }

    @Override
    List<HashMap<String, Object>> readJSON(Path path, String encode) {
        URL contentFile = getClass().getClassLoader().getResource(path.toString())
        if(!contentFile) {
            return super.readJSON(path, encode)
        }
        return super.readJSON(Paths.get(contentFile.toURI()), encode)
    }

    @Override
    Map<String, Object> readYaml(Path path, String encode) {
        URL contentFile = getClass().getClassLoader().getResource(path.toString())
        if(!contentFile) {
            return super.readYaml(path, encode)
        }
        return super.readYaml(Paths.get(contentFile.toURI()), encode)
    }

    @Override
    List<Path> findFiles(Path rootDir, String glob)  {
        URL contentFile = getClass().getClassLoader().getResource(rootDir.toString())
        return super.findFiles(Paths.get(contentFile.toURI()),glob)
    }

    @Override
    boolean fileExists(Path path) {
        URL contentFile = getClass().getClassLoader().getResource(path.toString())
        if(!contentFile) {
            return super.fileExists(path)
        }
        return super.fileExists(Paths.get(contentFile.toURI()))
    }
}
