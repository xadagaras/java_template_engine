package com.template.engine.controller

import com.template.engine.model.TemplateViewModel
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import tornadofx.Controller
import java.io.File
import java.io.OutputStreamWriter
import java.time.LocalDateTime


class TemplateEngineController : Controller() {

    fun generateCode(model: TemplateViewModel) {
        var templateDirectory = File(model.templateDirectory.value)

        val cfg = Configuration(Configuration.VERSION_2_3_23)
        cfg.setDirectoryForTemplateLoading(templateDirectory)
        cfg.defaultEncoding = "UTF-8"
        cfg.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
        cfg.logTemplateExceptions = false

        if(templateDirectory.isDirectory) {
            templateDirectory.walk().maxDepth(1).forEach { file ->
                if(file.name != templateDirectory.name) {
                    var packageName = model.packageName.value.toLowerCase()
                    println("Working on file ${file.name}")


                    val templateVars = mapOf("domainName" to model.domainName.value ,"packageName" to packageName, "serialVersionId" to generateSerialVersionId())
                    if(file.name.endsWith(".ftl")) {
                        val filename = getFilename(file, model)
                        val outputFile = File("${model.projectDirectory.value}/${filename}").also {
                            it.parentFile.mkdirs()
                        }
                        if(!outputFile.exists()) {

                            val template = cfg.getTemplate(file.name)
                            val out = OutputStreamWriter(outputFile.outputStream())
                            template.process(templateVars, out)
                            out.close()
                            println("${model.projectDirectory.value}/${filename} generated")
                        }
                    }
                }
            }
        }
    }

    private fun getFilename(file: File, model: TemplateViewModel): String {
        var filename = ""
        file.bufferedReader().use {
            val line = it.readLine()
            filename = line.replace("<#--", "")
            filename = replaceFilePrefix(filename, model.domainName.value)
            filename = replacePackageName(filename, model.packageName.value.replace(".", "/"))
            filename = filename.replace("-->", "")
        }
        return filename.trim()
    }

    private fun replaceFilePrefix(line: String, domainName: String) : String {
        val replaced = line.replace("{{DomainName}}", domainName)
        //println(replaced)
        return replaced
    }

    private fun generateSerialVersionId(): String {
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
        return "${year}${month}${day}${hourString}${minute}L"
    }

    private fun replacePackageName(line: String, packageName: String) : String {
        return line.replace("{{PackageName}}", packageName)
    }
}