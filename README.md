# Web Server

Este proyecto implementa un servidor web simple que admite múltiples solicitudes consecutivas sin concurrencia. Está diseñado para leer archivos de disco locales y devolverlos cuando se lo solicitan, incluidas páginas HTML, archivos JavaScript, CSS e imágenes. Además, se crea una aplicación web para probar el servidor, que presenta comunicación asincrónica con servicios REST en el backend.
## Empezando

Estas instrucciones lo guiarán para obtener una copia del proyecto ejecutándose en su máquina local para fines de desarrollo y prueba.
### Prerequisitos

Para ejecutar este proyecto, debe tener Java instalado en su sistema. Siga los pasos a continuación para instalar Java y Maven (que se utiliza para administrar dependencias).
1. **Instalar Java:**

    Descargue e instale Java JDK (versión 11 o superior). Puede seguir las instrucciones en el [sitio web oficial de Java](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

2. **Instalar Maven:**

   - Maven se utiliza para gestionar dependencias de proyectos. Puedes descargar e instalar Maven desde [aquí](https://maven.apache.org/download.cgi).

   - Después de la instalación, verifique si Maven está instalado correctamente ejecutando: mvn -v Esto debería mostrar la versión de Maven instalada.
  
### Instalando
> [!NOTE]
> Debes realizar los siguientes pasos desde una terminal de Bash o desde PowerShell en Windows.

Para poner en funcionamiento su entorno de desarrollo:
1. **Clonar el repositorio:**
```bash
   git clone https://github.com/CristianAlvarez-b/AREP-Lab1
```
2. **Navegar al directorio del proyecto:**
```bash
   cd AREP-Lab1
```
3. **Construye el proyecto con Maven:**
```bash
   mvn clean install
```
  Esto compilará el código y lo empaquetará en un archivo JAR ejecutable.
4. **Ejecutar el servidor:**
```bash
   java -jar target/HttpServer-1.0-SNAPSHOT.jar
```
   El servidor se iniciará y escuchará en el puerto 35000 de forma predeterminada. Ahora puede acceder al servidor web a través de `http://localhost:35000`.

### Ejecución de las pruebas
Se incluyen pruebas automatizadas para garantizar la funcionalidad del servidor y la aplicación web.
1. **Ejecutar pruebas unitarias:**
   Para ejecutar las pruebas automatizadas, utilice el siguiente comando Maven: mvn test
   Esto ejecutará todas las pruebas unitarias y mostrará los resultados en la terminal.

## Desglose de pruebas
Las pruebas de extremo a extremo simulan el flujo de trabajo completo de un usuario que interactúa con el servidor. Estas pruebas validan el manejo correcto de múltiples solicitudes, el servicio de archivos y la comunicación asincrónica entre el frontend y el backend.
Example test:
```java
@Test
public void testFileServing() throws IOException {
 // Simulate a request for a static file
 String filePath = "/index.html";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 PrintWriter out = new PrintWriter(outputStream);

 HttpServer.handleStaticFileRequest(filePath, out, mockSocket);

 // VeSe verifica que la consulta del usuario si contenga el mensaje http correspondiente
 String output = outputStream.toString();
 assertTrue(output.contains("HTTP/1.1 200 OK"));
 assertTrue(output.contains("Content-Type: text/html"));
}
```
### Construido con
- Java: el lenguaje de programación utilizado
- Maven: herramienta de gestión de dependencias y compilación
- JUnit: marco de pruebas
- Mockito: marco de simulación para pruebas unitarias

### Autor
- Cristian Javier Alvarez Baquero
  
### License
Este proyecto está licenciado bajo la licencia MIT: consulte el archivo LICENSE.md para obtener más detalles

### Explicación de secciones:
- **Empezando**: Instrucciones para configurar el entorno de desarrollo.
- **Prerequisitos**: Qué herramientas necesitas y cómo instalarlas (Java y Maven).
- **Instalando**: Cómo clonar el repositorio, construir y ejecutar el proyecto.
- **Ejecución de las pruebas**: Cómo ejecutar los tests y qué tipo de pruebas hay (unitarias y de estilo de código).
- **Construido con**: Herramientas y bibliotecas utilizadas en el proyecto.
- **License**: Tipo de licencia (MIT) y enlace al archivo de licencia.

