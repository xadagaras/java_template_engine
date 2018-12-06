package com.template.engine.view

import com.template.engine.controller.TemplateEngineController
import com.template.engine.model.TemplateViewModel
import javafx.collections.FXCollections
import javafx.scene.layout.Priority
import tornadofx.*
import java.io.File

class MainView : View("Java Template Engine") {

    val controller: TemplateEngineController by inject()
    val templateNames = FXCollections.observableArrayList<String>(getTemplateNames())

    val model = TemplateViewModel()

    override val root = vbox {
        vbox {
            label(title) {
                addClass(Styles.heading)
            }
            form {
                fieldset("Project") {
                    field("Project Folder") {

                        textfield().bind(model.projectDirectory)
                        button("open") {
                            action {
                                val directory = chooseDirectory("Select Target Directory")
                                model.projectDirectory.value = "${directory?.absolutePath}"

                            }
                        }

                    }
                    field("Template Name") {
                        combobox<String> {
                            items = templateNames
                            vgrow = Priority.ALWAYS
                            bind(model.templateName)

                        }
                    }
                }
                fieldset("Domain") {
                    field("Name") {
                        textfield().bind(model.domainName)
                    }
                    field("Package") {
                        textfield().bind(model.packageName)
                    }
                }
                button("Generate Code") {
                    action {
                        controller.generateCode(model)
                    }
                }
            }
        }

    }

    fun getTemplateNames() : List<String> {
        val templateNames: MutableList<String> = mutableListOf()
        File("./template").walk().forEach { file ->
            if(file.isDirectory && file.name != "template") {
                templateNames.add(file.name)
            }
        }
        return templateNames
    }
}