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

@WebServlet("/completar")
public class CompletarTarea extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String id = req.getParameter("id");

        if (id == null || id.isEmpty()) {
            ctx.setVariable("mensajeError", "Faltan parametros.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        int idInt;

        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            ctx.setVariable("mensajeError", "El ID debe ser un número.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (req.getSession().getAttribute("tareas") == null){
            ctx.setVariable("mensajeError", "Crea una lista primero");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        List<Tarea> tareas = (List<Tarea>) req.getSession().getAttribute("tareas");

        boolean encontrada = false;
        for (Tarea t: tareas) {
            if (t.getId() == idInt){
                t.setCompletada(true);
                encontrada = true;

                ctx.setVariable("mensajeBien", "Tarea completada correctamente.");
                engine.process(UrlConstants.TEMPLATE_COMPLETAR, ctx, resp.getWriter());
            }
        }

        if(!encontrada){
            ctx.setVariable("mensajeError", "El ID introducido no existe.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }



    }

}
