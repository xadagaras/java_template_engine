package com.template.engine.view

import com.template.engine.controller.TemplateEngineController
import com.template.engine.model.TemplateViewModel
import tornadofx.*

class MainView : View("Java Template Engine") {

    val controller: TemplateEngineController by inject()



    val model = TemplateViewModel()

    override val root = vbox {
        vbox {
            label(title) {
                addClass(Styles.heading)
            }
            form {
                fieldset("Project") {
                    field("Project Folder") {

                        textfield() {
                            tooltip("Select the target directory where the code files should be saved.")
                        }.bind(model.projectDirectory)
                        button("Choose Project Folder") {
                            tooltip("Select the target directory where the code files should be saved.")
                            action {
                                val directory = chooseDirectory("Select Target Directory")
                                model.projectDirectory.value = "${directory?.absolutePath}"

                            }
                        }

                    }
                    field("Template Directory") {
                        textfield(){
                            tooltip("Select the directory where the template file(s) are located.")
                        }.bind(model.templateDirectory)
                        button("Choose Template Folder") {
                            tooltip("Select the directory where the template file(s) are located.")
                            action {
                                val directory = chooseDirectory("Select Target Directory")
                                model.templateDirectory.value = "${directory?.absolutePath}"
                            }
                        }
                    }
                }
                fieldset("Domain") {
                    field("Name") {
                        textfield(){
                            tooltip("Specify the prefix that should be used for the classes. I.e. if you want a class to be named PetStore and Store" +
                                    "is defined fixed inside of the template, you need to specify Pet in this field.")
                        }.bind(model.domainName)
                    }
                    field("Package") {
                        textfield(){
                            tooltip("Specify the relative package name that should be used by the generated class. The length " +
                                    "of the package name is defined by the templates itself.")
                        }.bind(model.packageName)
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
}