package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Tarea;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/crear")
public class CrearLista extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String maxTareas = req.getParameter("max");

        if (maxTareas == null || maxTareas.isEmpty()) {
            ctx.setVariable("mensajeError", "Te has olvidado de enviar el máximo de tareas en la URL.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        int maxTareasConvertido;

        try {
            maxTareasConvertido = Integer.parseInt(maxTareas);
        } catch (NumberFormatException e) {
            ctx.setVariable("mensajeError", "Deben ser números, no letras.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if  (maxTareasConvertido <= 0) {
            ctx.setVariable("mensajeError", "El número debe ser mayor a 0");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        req.getSession().setAttribute("maxTareas", maxTareasConvertido);

        List<Tarea> tareas = new ArrayList<>();

        req.getSession().setAttribute("tareas", tareas);
    }
}
