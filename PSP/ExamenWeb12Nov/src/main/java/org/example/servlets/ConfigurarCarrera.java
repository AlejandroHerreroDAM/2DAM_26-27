package org.example.servlets;

import config.ThymeleafConstants;
import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Carrera;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet("/configurar")
public class ConfigurarCarrera extends HttpServlet {

    private static final String PASSWORD_ADMIN = "123";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String password = req.getParameter("password");
        String longitudStr = req.getParameter("longitud");

        if (password == null || password.isEmpty() || longitudStr == null || longitudStr.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Faltan parámetros obligatorios (password, longitud).");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (!PASSWORD_ADMIN.equals(password)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ctx.setVariable("mensajeError", "Contraseña de administrador incorrecta.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        int longitud;
        try {
            longitud = Integer.parseInt(longitudStr);
            if (longitud <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "La longitud debe ser un entero mayor que cero.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        // Guardamos la carrera en la SESIÓN
        Carrera carrera = new Carrera(longitud);
        req.getSession().setAttribute("carrera", carrera);

        ctx.setVariable("mensajeBien", "Carrera configurada con éxito. Distancia: " + longitud);
        engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());
    }
}