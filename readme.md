# Example template

You need a folder named template inside the root (the directory where the jar is from which you start this app)

Inside the template directory you can create a new folder, the name of the Folder will be the name of the template.

```
#file {{PackageName}}.{{FilePrefix}}Service

#module moduleA

package {{PackageName}};

import org.slf4j.Logger;

@Slf4j
public class {{FilePrefix}}Service {

    public {{FilePrefix}}Service() {
        // This is the default ctor
    }
}
```
