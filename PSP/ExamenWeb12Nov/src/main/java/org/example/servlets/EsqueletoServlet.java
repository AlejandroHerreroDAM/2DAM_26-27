package org.example.servlets;

// IMPORTANTE: Asegúrate de ajustar tus imports de config.* si cambian en el examen
import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.List;

@WebServlet("/ruta_del_ejercicio")
public class EsqueletoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // ==========================================
        // 1. INICIALIZACIÓN DE THYMELEAF (SIEMPRE IGUAL)
        // ==========================================
        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        // ==========================================
        // 2. RECUPERACIÓN DE PARÁMETROS DE LA URL/FORMULARIO
        // ==========================================
        String paramTexto = req.getParameter("nombre_param_texto");
        String paramNum = req.getParameter("nombre_param_numero");

        // ==========================================
        // 3. VALIDACIÓN 1: NULOS Y VACÍOS (¡CRÍTICO!)
        // ==========================================
        if (paramTexto == null || paramTexto.isEmpty() || paramNum == null || paramNum.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Error 400
            ctx.setVariable("mensajeError", "Faltan parámetros obligatorios.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return; // ¡No olvides el return para que no siga ejecutando!
        }

        // ==========================================
        // 4. VALIDACIÓN 2: CONVERSIÓN DE NÚMEROS (TRY-CATCH)
        // ==========================================
        int numeroConvertido;
        try {
            numeroConvertido = Integer.parseInt(paramNum);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "El parámetro debe ser un número válido.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        // ==========================================
        // 5. VALIDACIÓN 3: LÓGICA DE NEGOCIO BÁSICA
        // ==========================================
        if (numeroConvertido <= 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "El número debe ser mayor que cero.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        // ==========================================
        // 6. GUARDAR (SET) -> ÚSALO EN SERVLETS DE "CREAR" / "INICIALIZAR"
        // ==========================================

        // A) En la SESIÓN (Datos personales del usuario. Ej: Su carrito, su lista de tareas)
        // List<Object> nuevaLista = new ArrayList<>();
        // req.getSession().setAttribute("nombre_lista_sesion", nuevaLista);
        // req.getSession().setAttribute("limite_usuario", numeroConvertido);

        // B) En el CONTEXTO (Datos globales compartidos. Ej: Estadísticas globales, tienda)
        // getServletContext().setAttribute("contador_global", numeroConvertido);


        // ==========================================
        // 7. RECUPERAR (GET) Y VALIDAR -> ÚSALO EN SERVLETS DE "AÑADIR", "CONSULTAR", ETC.
        // ==========================================

        // A) Recuperar de la SESIÓN (Datos del usuario)
        if (req.getSession().getAttribute("nombre_lista_sesion") == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Debes crear/configurar tu lista personal primero.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }
        List<Object> listaSesion = (List<Object>) req.getSession().getAttribute("nombre_lista_sesion");


        // B) Recuperar del CONTEXTO (Datos globales)
        /*
        if (getServletContext().getAttribute("contador_global") == null) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ctx.setVariable("mensajeError", "El sistema global no ha sido inicializado.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }
        int variableGlobal = (int) getServletContext().getAttribute("contador_global");
        */


        // ==========================================
        // 8. LÓGICA PRINCIPAL DEL SERVLET
        // ==========================================
        // Aquí va lo que pida el ejercicio:
        // - Crear objetos con new() y hacer listaSesion.add()
        // - Buscar con for / stream()
        // - Modificar o eliminar

        // ==========================================
        // 9. LÓGICA POR PASOS / CONTADOR DE CLICS (EJ: PODIO)
        // ==========================================
        // a) Recuperamos el contador de la sesión (usamos Integer, no int, para poder comprobar el null)
        Integer pasoActual = (Integer) req.getSession().getAttribute("contadorPasos");

        // b) Si es nulo, significa que es la primera vez que entramos
        if (pasoActual == null) {
            pasoActual = 1;
        } else {
            pasoActual++; // Si ya existía, le sumamos 1
        }

        // c) Guardamos el nuevo valor actualizado en la sesión para la próxima vez
        req.getSession().setAttribute("contadorPasos", pasoActual);

        // d) Hacemos algo diferente según el paso en el que estemos
        switch (pasoActual) {
            case 1:
                ctx.setVariable("mensajeBien", "¡Tercer puesto entregado!");
                // Lógica del tercer puesto...
                break;
            case 2:
                ctx.setVariable("mensajeBien", "¡Segundo puesto entregado!");
                // Lógica del segundo puesto...
                break;
            case 3:
                ctx.setVariable("mensajeBien", "¡Primer puesto entregado!");
                // Lógica del primer puesto...
                break;
            default:
                // Qué hacer si siguen pulsando el link después del último paso
                ctx.setVariable("mensajeError", "El podio ya está completo, no hay más premios.");

                // Opcional: Reiniciar el contador si quieres que vuelva a empezar
                // req.getSession().removeAttribute("contadorPasos");

                engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
                return;
        }


        // ==========================================
        // 10. RESPUESTA DE ÉXITO Y PLANTILLA FINAL
        // ==========================================
        // Pasamos datos a la vista si hace falta
        ctx.setVariable("mensajeBien", "Operación realizada con éxito.");
        ctx.setVariable("datoExtra", numeroConvertido);

        // Procesamos la plantilla HTML correcta (ej: UrlConstants.TEMPLATE_EXITO)
        engine.process("nombreDeTuPlantilla.html", ctx, resp.getWriter());
    }

    // NOTA: Si el formulario es un <form method="POST">, cambia doGet por doPost.
    // La estructura interna es exactamente la misma.
}