package org.example.servlets;

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

@WebServlet("/configurar")
public class ConfigurarCarrera extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String password = req.getParameter("password");
        String longitud = req.getParameter("longitud");



        if (password == null || password.isEmpty() || longitud == null || longitud.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Faltan parámetros obligatorios.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        String contrasena = "123";
        if (!password.equals(contrasena)){
            ctx.setVariable("mensajeError", "La contraseña no coincide.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        int longitudInt;

        try {
            longitudInt = Integer.parseInt(longitud);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "El parámetro longitud debe ser un número válido.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (longitudInt <= 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "La longitud debe ser mayor que cero.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        getServletContext().setAttribute("longitud", longitudInt);

        boolean carreraComenzada = false;
        getServletContext().setAttribute("carreraComenzada", carreraComenzada);

        boolean hanGanado = false;
        getServletContext().setAttribute("hanGanado", hanGanado);

        ctx.setVariable("mensajeBien", "Carrera configurada con éxito. Distancia :" + longitudInt );

        engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());

    }


}
