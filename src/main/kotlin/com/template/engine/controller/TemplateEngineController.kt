package com.template.engine.controller

import com.template.engine.model.TemplateViewModel
import tornadofx.Controller
import java.io.File
import java.time.LocalDateTime

class TemplateEngineController : Controller() {

    fun getTemplateNames() {

    }

    fun generateCode(model: TemplateViewModel) {
        var baseDirectory = File("./template/" + model.templateName.value)
        if(baseDirectory.isDirectory) {
            baseDirectory.walk().forEach { file ->
                if(file.name != baseDirectory.name) {
                    var basePath = model.projectDirectory.value
                    var classpath = ""
                    var moduleName = ""
                    var packageName = model.packageName.value.toLowerCase()
                    var newFile = ""
                    println("Working on file ${file.name}")
                    file.forEachLine {


                        // first step, replace all placeholders
                        var currentLine = it
                        val domainName = model.domainName.value
                        if(domainName.isNotBlank()) {
                            currentLine = replaceFilePrefix(currentLine, domainName)
                            currentLine = replacePackageName(currentLine, packageName)
                            currentLine = replaceFilePrefixUpper(currentLine, domainName)
                            currentLine = replaceFilePrefixLower(currentLine, domainName)
                            currentLine = replaceFilePrefixLowerCapital(currentLine, domainName)
                            currentLine = generateSerialVersionId(currentLine)
                        }
                        if(notHeaderLine(currentLine)) {
                            newFile += "${currentLine}\n"
                        }

                        // second step, parse the first line and create file to write
                        if(currentLine.startsWith("#file")) {
                            currentLine = currentLine.replace(".", "/")
                            currentLine = currentLine.replace("#file", "").trim()
                            classpath = currentLine
                        }

                        if(currentLine.startsWith("#module")) {
                            currentLine = currentLine.replace(".", "/")
                            currentLine = currentLine.replace("#module", "").trim()
                            moduleName = currentLine
                        }
                    }
                    // step three, write new file
                    File("${basePath}/${moduleName}/src/main/java/${classpath}.java").also {
                        it.parentFile.mkdirs()
                    }.writeText(newFile)
                    println("Class ${file.name} generated")
                }
            }
        }
    }

    private fun replaceFilePrefixUpper(currentLine: String, domainName: String): String {
        return currentLine.replace("{{FilePrefixUpper}}", domainName.toUpperCase())
    }

    private fun replaceFilePrefixLower(currentLine: String, domainName: String): String {
        return currentLine.replace("{{FilePrefixLower}}", domainName.toLowerCase())
    }

    private fun replaceFilePrefixLowerCapital(currentLine: String, domainName: String): String {

        return currentLine.replace("{{FilePrefixLowerCapital}}", domainName.decapitalize())
    }

    private fun notHeaderLine(line: String) : Boolean{
        return !(line.startsWith("#file") || line.startsWith("#module"))
    }

    private fun replaceFilePrefix(line: String, domainName: String) : String {
        val replaced = line.replace("{{FilePrefix}}", domainName)
        //println(replaced)
        return replaced
    }

    private fun generateSerialVersionId(currentLine: String): String {
        val currentDate = LocalDateTime.now()
        val year = currentDate.year
        val month = currentDate.monthValue
        val day = currentDate.dayOfMonth
        val hour = currentDate.hour
        var hourString: String
        if(hour < 10) {
            hourString = "0${hour}"
        } else {
            hourString = "${hour}"
        }
        val minute = currentDate.minute
        return currentLine.replace("{{SerialVersionId}}", "${year}${month}${day}${hourString}${minute}L")
    }

    private fun replacePackageName(line: String, packageName: String) : String {
        val replaced = line.replace("{{PackageName}}", packageName)
        //println(replaced)
        return replaced
    }
}