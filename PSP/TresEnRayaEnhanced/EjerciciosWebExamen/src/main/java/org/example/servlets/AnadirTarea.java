package org.example.servlets;

import config.UrlConstants;
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
import java.util.List;

@WebServlet("/anadir")
public class AnadirTarea extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String titulo = req.getParameter("titulo");
        String descripcion = req.getParameter("descripcion");
        String prioridad = req.getParameter("prioridad");

        if (titulo == null || descripcion == null || prioridad == null || titulo.isEmpty() || descripcion.isEmpty() || prioridad.isEmpty()) {
            ctx.setVariable("mensajeError", "Faltan parametros.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        int prioridadInt;

        try {
            prioridadInt = Integer.parseInt(prioridad);
        } catch (NumberFormatException e) {
            ctx.setVariable("mensajeError", "La prioridad debe ser un número.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (prioridadInt <= 0 || prioridadInt > 3) {
            ctx.setVariable("mensajeError", "La prioridad debe ser un número del 1 al 3");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (req.getSession().getAttribute("maxTareas") == null || req.getSession().getAttribute("tareas") == null) {
            ctx.setVariable("mensajeError", "Crea una lista primero");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        int maxTareas = (int) req.getSession().getAttribute("maxTareas");

        List<Tarea> tareas = (List<Tarea>) req.getSession().getAttribute("tareas");

        if (tareas.size() >= maxTareas) {
            ctx.setVariable("mensajeError", "Ya has superado el máximo de tareas");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        } else {
            Tarea nuevaTarea = new Tarea(titulo, descripcion, prioridadInt);
            tareas.add(nuevaTarea);
        }


    }

}

