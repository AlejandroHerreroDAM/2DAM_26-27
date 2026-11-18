package org.example.servlets;

import config.ThymeleafConstants;
import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Biblioteca;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet("/inicio")
public class InicioBiblioteca extends HttpServlet {

    private final String PASSWORD_BIBLIOTECA1 = "123";
    private final String PASSWORD_BIBLIOTECA2 = "asd";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String password = req.getParameter("password");

        if (password == null || password.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Faltan parámetros obligatorios (password).");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (!PASSWORD_BIBLIOTECA1.equals(password) && !PASSWORD_BIBLIOTECA2.equals(password) ) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ctx.setVariable("mensajeError", "Contraseña de administrador incorrecta.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (PASSWORD_BIBLIOTECA1.equals(password)) {
            Biblioteca biblioteca = new Biblioteca("Biblioteca1");

            req.getSession().setAttribute("biblioteca", biblioteca);
            ctx.setVariable("mensajeBien", "Partida configurada con éxito. " + biblioteca.getNombre());

        } else {
            Biblioteca biblioteca = new Biblioteca("Biblioteca2");

            req.getSession().setAttribute("biblioteca", biblioteca);

            ctx.setVariable("mensajeBien", "Biblioteca iniciada con éxito. " + biblioteca.getNombre());
        }

        engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());
    }

}
