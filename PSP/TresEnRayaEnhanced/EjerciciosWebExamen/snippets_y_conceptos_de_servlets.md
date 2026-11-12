# 🧩 SNIPPETS ÚTILES (El "Por si acaso")

### 🔍 Buscar un objeto en la lista (Con Streams)
```java
// Busca el primer objeto que coincida con el ID numérico
Objeto encontrado = listaSesion.stream()
    .filter(o -> o.getId() == idBuscado)
    .findFirst()
    .orElse(null);

if (encontrado == null) {
    resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
    // Lanzar plantilla de error...
}
```

### 🗑️ Borrar un objeto de la lista (Por ID)
```java
// Devuelve true si borró algo, false si no lo encontró
boolean borrado = listaSesion.removeIf(o -> o.getId() == idBuscado);

if (!borrado) {
    // Lanzar plantilla de error informando que no existe...
}
```

### 🔄 Redirección automática (Saltar a otra URL)
Útil si el enunciado pide que al terminar te mande directamente a consultar la lista.
```java
// NO usas engine.process, usas esto:
resp.sendRedirect("lista"); // El navegador saltará a /lista automáticamente
```

### 💣 Reiniciar / Vaciar el sistema (Destruir Sesión)
```java
// Borra completamente la caja de sesión del usuario
req.getSession().invalidate();
ctx.setVariable("mensajeBien", "Todo ha sido borrado.");
engine.process("inicio.html", ctx, resp.getWriter());
```

### 🔢 Máquina de Estados (Contador de veces que visitas la URL)
```java
Integer pasoActual = (Integer) req.getSession().getAttribute("paso");

if (pasoActual == null) {
    pasoActual = 1;
} else {
    pasoActual++;
}

req.getSession().setAttribute("paso", pasoActual);

switch (pasoActual) {
    case 1: // Primera visita
        // ...
        break;
    case 2: // Segunda visita
        // ...
        break;
}
```

### 🗂️ Leer parámetros repetidos en la URL (`?filtro=A&filtro=B`)
```java
String[] filtros = req.getParameterValues("filtro");

if (filtros != null) {
    for(String f : filtros) {
        // ...
    }
}
```

---

# 🧠 CONCEPTOS CLAVE RÁPIDOS
*   `req.getParameter("x")`: Lee lo que viene de la URL. Siempre es un `String`.
*   `req.getSession().setAttribute("k", v)`: Guardar datos en la taquilla personal del usuario.
*   `req.getSession().getAttribute("k")`: Recuperar datos. Siempre hay que comprobar si es null y castear `(List<Objeto>)`.
*   `getServletContext()`: Taquilla global de la aplicación (todos los usuarios ven lo mismo).
*   `return;`: ¡Ponlo siempre después de lanzar una plantilla de error para frenar el código de Java!