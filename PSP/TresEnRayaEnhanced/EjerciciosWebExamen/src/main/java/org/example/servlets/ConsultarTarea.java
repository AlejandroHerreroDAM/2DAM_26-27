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

@WebServlet("/consultar")
public class ConsultarTarea extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String id = req.getParameter("id");

        if (id == null || id.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Error HTTP 400
            ctx.setVariable("mensajeError", "Falta el parámetro ID.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        int idInt;

        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Error HTTP 400
            ctx.setVariable("mensajeError", "El ID debe ser un número.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (req.getSession().getAttribute("tareas") == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Crea una lista primero.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        List<Tarea> tareas = (List<Tarea>) req.getSession().getAttribute("tareas");
        Tarea tareaEncontrada = null;

        // Buscamos la tarea
        for (Tarea t : tareas) {
            if (t.getId() == idInt) {
                tareaEncontrada = t;
                break; // Rompemos el bucle porque ya la hemos encontrado
            }
        }

        // Gestionamos si la hemos encontrado o no
        if (tareaEncontrada == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // Error HTTP 404
            ctx.setVariable("mensajeError", "No existe ninguna tarea con ese ID.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
        } else {
            // Pasamos el objeto entero a la plantilla para mostrar sus datos
            ctx.setVariable("tarea", tareaEncontrada);
            // NOTA: Asegúrate de tener una constante o string con la plantilla de detalles
            engine.process(UrlConstants.TEMPLATE_DETALLES, ctx, resp.getWriter());
        }
    }
}