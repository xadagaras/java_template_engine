# Java Template Engine

The Java Template Engine is a small GUI (graphical user interface) which uses
Freemarker Templates to generate custom code. The code to be generated can be of 
any type (programming language, something completely unrelated to programming etc.).


# Example template

Inside of a template you can write down the Code that you want to have. With Freemarker's variable support
you can probide placeholders inside of your template which should be replaced later.

Currently there are the following variables supported:

| Variable name | Description | Example(s) |
|---|---|---|
| domainName | This variable can be used i.e. for the Classname, for Variablenames etc. | public class ${domainName}Service { |
| packageName | This variable is used for package names. This could be used everywhere else, but that would not make much sense so please use it only inside of package  | ${packageName} |
| DomainName | This one is a not to be used as Freemarker variable, but used by the Java Template Engine for the file name. This Variable has to be wrapped in {{ }} | {{DomainName}} |
| PackageName | Same as with DomainName for the package name part inside of the url | src/main/java/com/example/demo/{{PackageName}}/{{DomainName}}Service.java |
| serialVersionId | Generates the serialVersionUID (might be only relevant for Java) | ${serialVersionId}|

The first line needs to start with a Freemarker commet (<#-- -->), and inside of this comment you have to provide
the relative path from the project root directory to the directory in which the class needs to be stored. With {{PackageName}} inside
the path you specify the package name (. will be replaced by /) and with {{DomainName}} you specify the Placeholder for the class name.

It is important that you specify the file ending, because the Java Template Engine does not know anything about the underlying template as of now.

```injectedfreemarker
<#-- src/main/java/com/example/demo/{{ClassPrefix}}Service.java-->
package com.example.demo.${packageName};

import org.slf4j.Logger;

@Slf4j
public class ${domainName}Service {
    
    private ${domainName}Dto ${domainName?decapitalize}Dto;

    public ${domainName}}Service(${domainName}Dto ${domainName?decapitalize}Dto) {
        // This is the ctor
        this.${domainName?decapitalize}Dto = ${domainName?decapitalize}Dto;
    }
}
```

If you use as Packagename my.packages and as Classprefix  Demo this would result in the following class generated:


```java
package com.example.demo.my.packages;

import org.slf4j.Logger;

@Slf4j
public class DemoService {
    
    private DemoDto demoDto;

    public DemoService(DemoDto demoDto) {
    // This is the ctor
        this.demoDto = demoDto;
    }
}
```
