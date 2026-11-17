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

@WebServlet("/comenzar")
public class ComienzoCarrera extends HttpServlet {

    private static final String PASSWORD_ADMIN = "123";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String password = req.getParameter("password");
        if (password == null || !PASSWORD_ADMIN.equals(password)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ctx.setVariable("mensajeError", "Contraseña de administrador incorrecta o ausente.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        Carrera carrera = (Carrera) req.getSession().getAttribute("carrera");
        if (carrera == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Falta configurar la carrera.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (carrera.getParticipantes().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "No hay participantes inscritos para empezar.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        carrera.setComenzada(true);

        ctx.setVariable("mensajeBien", "¡La carrera ha comenzado! Ronda 1 abierta.");
        engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());
    }
}