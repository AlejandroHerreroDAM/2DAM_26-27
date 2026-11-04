# Guía de Supervivencia: Examen Práctico Web (Java)

Ejemplo de url http://localhost:8080/PruebaExamenWeb_war/puntos?puntos=1

**Esquema Rápido**
1. **Infraestructura:** Dependencias (POM), Tomcat y estructura de carpetas (webapp).
2. **Configuración:** Clases base (Constantes y ThymeleafInitializer).
3. **Enrutamiento:** `@WebServlet`, `doGet` y preparación del motor de plantillas.
4. **Entrada de Datos:** `req.getParameter` y validación de errores.
5. **Persistencia:** Recuperar, operar y guardar en `HttpSession`.
6. **Salida HTML:** Inyección de datos al `WebContext` y renderizado de la vista.

**Paso a Paso Detallado**

**1. Configuración del Proyecto (pom.xml y Tomcat)**
* Añade las dependencias de `jakarta.servlet-api` (scope provided) y `thymeleaf` al `pom.xml`.
* Añade `<packaging>war</packaging>` y el plugin `maven-war-plugin` para permitir el empaquetado web.
* Configura Tomcat: En la pestaña *Deployment*, pulsa **Fix** para añadir el artefacto `war exploded` y limpia el *Application context* dejándolo en `/`.

**2. Estructura de Carpetas**
* Crea la jerarquía exacta requerida para recursos web: `src/main/webapp/WEB-INF/templates/`.
* Mueve todos los archivos `.html` del examen directamente a la carpeta `templates`.

**3. Clases de Configuración (Carpeta config)**
* Pega el paquete `config` con tus clases de constantes y el `@WebListener` (`ThymeleafInitializer`).
* Actualiza `UrlConstants` definiendo las rutas de los nuevos HTML que exija el enunciado.
* Ejecuta el ciclo "Maven Clean" y luego "Compile" para asentar las constantes en la memoria antes de arrancar.

**4. Estructura Base del Servlet**
* Extiende la nueva clase de `HttpServlet` y declárala con `@WebServlet("/ruta-asignada")`.
* Inicializa el motor de Thymeleaf en las primeras líneas de `doGet` para poder procesar plantillas de error en cualquier momento.

**5. Captura y Validación de Errores**
* Recupera los datos del usuario mediante `req.getParameter("nombreDelInput")`.
* Valida siempre los datos nulos o vacíos antes de ejecutar lógica matemática o de negocio.
* Usa un bloque `try-catch` para controlar excepciones como `NumberFormatException` al convertir cadenas a números.
* En caso de error, inyecta el mensaje en el contexto, procesa tu plantilla `error.html` y ejecuta `return;` para detener el código.

**6. Manejo de la Sesión HTTP**
* Llama a `req.getSession()` para obtener el espacio de memoria del navegador actual.
* Lee los atributos previamente guardados recordando aplicar el casting necesario, por ejemplo `(Integer) session.getAttribute("clave")`.
* Intercepta el caso `null` (primera visita) para inicializar variables a sus valores por defecto.
* Procesa la lógica y sobreescribe los nuevos datos en sesión con `session.setAttribute("clave", valor)`.

**7. Renderizado Final**
* Usa `ctx.setVariable("nombreEnElHTML", valorJava)` para enviar la información final.
* Aplica `resp.setContentType(ThymeleafConstants.CONTENT_TYPE)` para formatear la cabecera.
* Concluye enviando la plantilla mediante `engine.process(UrlConstants.TEMPLATE_CORRESPONDIENTE, ctx, resp.getWriter())`.