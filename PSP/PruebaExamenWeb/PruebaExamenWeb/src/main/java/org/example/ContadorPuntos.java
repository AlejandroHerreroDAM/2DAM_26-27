package org.example;

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

// CAMBIA: La ruta de acceso web a este Servlet
@WebServlet("/puntos")
public class ContadorPuntos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // =========================================================================
        // 1. NUNCA CAMBIA: Preparación inicial de Thymeleaf
        // =========================================================================
        // Esto siempre va al principio igual, arranca el motor web para este Servlet.
        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);


        // =========================================================================
        // 2. CAMBIA: Captura de datos y validaciones de tu juego/app
        // =========================================================================
        // CAMBIA: El nombre del parámetro que buscas ("puntos", "coordenada", etc.)
        String puntosRecibidos = req.getParameter("puntos");

        if (puntosRecibidos == null || puntosRecibidos.isEmpty()) {
            ctx.setVariable("mensajeError", "Te has olvidado de enviar los puntos en la URL.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return; // Corta la ejecución si hay error
        }

        int puntosAConvertir;
        try {
            puntosAConvertir = Integer.parseInt(puntosRecibidos);
        } catch (NumberFormatException e) {
            ctx.setVariable("mensajeError", "Los puntos deben ser números, no letras.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }


        // =========================================================================
        // 3. CAMBIA: Lógica de la Sesión
        // =========================================================================
        // La sintaxis de get/set NUNCA cambia, pero las CLAVES ("puntosTotales") sí.
        var session = req.getSession();

        Integer totalAcumulado = (Integer) session.getAttribute("puntosTotales");

        //Si es la primera vez pone valor inicial
        if (totalAcumulado == null) {
            totalAcumulado = 0;
        }

        totalAcumulado += puntosAConvertir;

        session.setAttribute("puntosTotales", totalAcumulado);


        // =========================================================================
        // 4. CAMBIA: Inyección de datos y carga de la vista final
        // =========================================================================
        // CAMBIA: Las variables que tu HTML necesita leer
        ctx.setVariable("puntosSumados", puntosAConvertir);
        ctx.setVariable("totalPuntos", totalAcumulado);

        // CAMBIA: La constante del HTML que quieres mostrar (TEMPLATE_PUNTOS)
        engine.process(UrlConstants.TEMPLATE_PUNTOS, ctx, resp.getWriter());
    }
}