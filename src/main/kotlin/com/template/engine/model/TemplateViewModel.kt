package com.template.engine.model

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import tornadofx.*

class TemplateViewModel : ItemViewModel<Template>() {

    val projectDirectory: StringProperty = bind { SimpleStringProperty(item?.projectDirectory)}

    val templateName: StringProperty = bind { SimpleStringProperty(item?.templateName)}

    val packageName: StringProperty = bind { SimpleStringProperty(item?.packageName)}

    val domainName: StringProperty = bind { SimpleStringProperty(item?.domainName)}

}