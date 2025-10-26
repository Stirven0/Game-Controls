# Módulo Crash Handler

## Para migrar a otro proyecto:

1. Copiar la carpeta `crash-handler` al directorio raíz del nuevo proyecto
2. Agregar `include(":crash-handler")` en `settings.gradle.kts` de la rais del proyecto
3. Agregar `implementation(project(":crash-handler"))` en `app/build.gradle.kts`
4. Sincronizar Gradle

## Uso:

En tu Application class:
```java
import com.aa.crash_handler.CrashHandler;
import android.app.Application;
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.registerGlobal(this);
    }
}

en el AndroidManifest agrega el sigiente atributo:
<application
    android:name=".MyApp">
</application>