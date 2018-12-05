package com.template.engine.controller

import com.template.engine.model.TemplateViewModel
import tornadofx.*
import java.io.File
import java.io.FileOutputStream

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

                        currentLine = replaceFilePrefix(currentLine, model.domainName.value)
                        currentLine = replacePackageName(currentLine, packageName)
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

    private fun notHeaderLine(line: String) : Boolean{
        return !(line.startsWith("#file") || line.startsWith("#module"))
    }

    private fun replaceFilePrefix(line: String, domainName: String) : String {
        val replaced = line.replace("{{FilePrefix}}", domainName)
        //println(replaced)
        return replaced
    }

    private fun replacePackageName(line: String, packageName: String) : String {
        val replaced = line.replace("{{PackageName}}", packageName)
        //println(replaced)
        return replaced
    }
}