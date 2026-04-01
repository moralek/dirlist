# DirList

Aplicación web Java (Servlets) empaquetada con Maven como WAR para listar y operar archivos del sistema desde una interfaz web.

## Tipo de proyecto
- Maven + Java 8
- Empaquetado `war`
- Servlet API 3.1 (`javax.servlet-api` con scope `provided`)

## Compatibilidad Java
- El proyecto está fijado a Java 8.
- En [pom.xml](/workspace/dirlist/pom.xml) el compilador Maven usa `source` `1.8` y `target` `1.8`.
- No debe asumirse compatibilidad ni migración automática a Java 11, 17 o superior sin cambios explícitos en build, pruebas y despliegue.

## Estructura
- `pom.xml`: configuración de compilación y empaquetado.
- `src/main/java/com/dlt/`: servlets y utilidades.
- `src/main/webapp/WEB-INF/web.xml`: mapeo de rutas.
- `build.bat`: script de build para Windows.
- `build.sh`: script de build para Linux/Unix.

## Endpoints
- `/login` → `lg`: acceso al sistema.
- `/list` → `dl`: listado de directorios y archivos.
- `/fd` → `fd`: descarga de archivos.
- `/fz` → `fz`: compresión ZIP y descarga.
- `/up` → `up`: subida de archivos.
- `/del` → `del`: eliminación de archivos.
- `/si` → `si`: información de sistema/JVM.
- `/hrso` → `hrso`: hora/zona horaria del SO.

## Flujo principal
1. El usuario entra a `/list` o `/login`.
2. Si no hay sesión autenticada, el filtro `af` redirige a `/login`.
3. El usuario inicia sesión.
4. Se muestra el contenido del `path` actual con filtro y ordenamiento.
5. Por cada archivo/carpeta se habilitan acciones de descargar/zip/eliminar.
6. Si hay permisos de escritura, se permite subir archivo.

## Autenticación
- El acceso al sistema requiere inicio de sesión previo en `/login`.

## Clases principales
- `lg`: servlet de acceso.
- `af`: filtro de autenticación.
- `ut`: utilidades generales.
- `dl`: listado principal.

## Build
```bash
mvn clean package
```

Requisito:
- Ejecutar el build con un entorno que tenga Java 8 y Maven disponibles.

En Windows:
```bat
build.bat
```

En Linux/Unix:
```bash
chmod +x build.sh
./build.sh
```

## Observaciones
- No hay tests automáticos en `src/test`.
- La generación de HTML se hace directamente con `PrintWriter` en los servlets.


## Ejemplos de uso (rápidos)

### 1) Navegar carpetas desde el navegador
1. Abre la aplicación en tu servidor, por ejemplo: `http://localhost:8080/cgu90dlst/list`.
2. Si no tienes sesión, serás redirigido a `http://localhost:8080/cgu90dlst/login`.
3. Inicia sesión con las credenciales configuradas para el entorno.
4. Haz clic en las carpetas para entrar.
5. Usa la barra **Buscar...** para filtrar por prefijo de nombre.

### 2) Descargar un archivo
- Desde la tabla, haz clic en el nombre del archivo.
- O directo por URL:
  `http://localhost:8080/cgu90dlst/fd?file=/ruta/completa/archivo.txt`

### 3) Comprimir y bajar una carpeta
- Haz clic en el ícono de archivo comprimido (ZIP) en la fila.
- O por URL:
  `http://localhost:8080/cgu90dlst/fz?path=/ruta/completa/carpeta`

### 4) Subir un archivo a la carpeta actual
1. Ve a la carpeta destino en `/list`.
2. Usa el selector de archivo y pulsa **Subir**.
3. Si el nombre ya existe, la app lo indicará y no sobreescribirá.

### 5) Eliminar un archivo
1. En **OS Info**, activa “mostrar eliminar”.
2. Haz clic en la “X” del archivo.
3. Confirma en el diálogo.

### 6) Ver información del sistema
- En la esquina superior derecha, pulsa **OS Info** para consultar versión Java, servidor, sistema operativo y zona horaria.

## Casos típicos (menos técnicos)
- **Compartir evidencias**: empaqueta una carpeta de logs con ZIP y descárgala en un clic.
- **Limpieza rápida**: filtra por prefijo y elimina archivos viejos visibles en el listado.
- **Carga de soporte**: sube un archivo de configuración a una ruta concreta sin usar consola.
