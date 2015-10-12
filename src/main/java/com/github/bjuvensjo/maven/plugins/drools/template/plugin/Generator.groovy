package com.github.bjuvensjo.maven.plugins.drools.template.plugin

class Generator {
    File baseDir
    File sourceDir
    File outputDir
    String charset

    Generator(File baseDir, File sourceDir, File outputDir, String charset) {
        this.baseDir = baseDir
        this.sourceDir = sourceDir
        this.outputDir = outputDir
        this.charset = charset
    }

    List<File> generateDrls() {
        List<File> drls = []
        sourceDir.eachFileRecurse(groovy.io.FileType.FILES) { file ->
            if (file.name.endsWith(".drt")) {
                String drlContent = generateDrlContent(file);
                File drlFile = new File(outputDir, "${file.path.substring(sourceDir.path.length())[0..-5]}Generated.drl")
                writeDrl(drlFile, drlContent, charset)
                drls << drlFile
            }
        }
        drls
    }

    private String generateDrlContent(File drtFile) {
        String drlContent = ""
        String template
        String templateName
        drtFile.eachLine() { line ->
            if (line.trim().startsWith("template")) {
                template = ""
                templateName = line.replaceAll("template *", "").replaceAll("\"", "");
            } else if (line.trim().startsWith("end template")) {
                String parametersPath = "${drtFile.parent}/${templateName}.txt"
                drlContent += generateTemplate(template, new File(parametersPath)).trim()
                drlContent += "\n"
                template = null
                templateName = null
            } else if (template != null) {
                template += "$line\n"
            } else {
                drlContent += "$line\n"
            }
        }
        drlContent
    }

    private String generateTemplate(String templateText, File parameterFile) {
        String generatedText = ""
        String[] parameterNames
        parameterFile.eachLine() { line, lineNumber ->
            if (lineNumber == 1) {
                parameterNames = split(line)
            } else {
                String lineGeneratedText = templateText
                String[] parameterValues = split(line)
                parameterNames.eachWithIndex { name, index ->
                    lineGeneratedText = replace(lineGeneratedText, parameterNames[index], parameterValues[index])
                }
                lineGeneratedText = replace(lineGeneratedText, "row", "${lineNumber - 1}")
                generatedText += "$lineGeneratedText\n"
            }
        }
        generatedText
    }

    private String[] split(String line) {
        line.replaceFirst("\\| *", "").split(" *\\| *");
    }

    private String replace(String text, String name, String value) {
        String regExp = java.util.regex.Pattern.quote("@{$name}")
        text.replaceAll(regExp, value)
    }

    private File getDrlFile(File drtFile) {
        new File(outputDir, "${drtFile.path.substring(sourceDir.path.length())[0..-5]}Generated.drl")
    }

    private void writeDrl(File drlFile, String drlContent, String charset) {
        drlFile.parentFile.mkdirs()
        drlFile.setText(drlContent, charset)
    }
}
