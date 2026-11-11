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

@WebServlet("/lista")
public class ConsultarLista extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        // Validamos que exista la lista en sesión antes de nada
        if (req.getSession().getAttribute("tareas") == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Crea una lista primero.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        List<Tarea> tareas = (List<Tarea>) req.getSession().getAttribute("tareas");

        // Calculamos el resumen
        int totalTareas = tareas.size();
        int tareasCompletadas = 0;

        for (Tarea t : tareas) {
            if (t.isCompletada()) {
                tareasCompletadas++;
            }
        }

        int tareasPendientes = totalTareas - tareasCompletadas;

        // Inyectamos las variables al contexto
        ctx.setVariable("total", totalTareas);
        ctx.setVariable("completadas", tareasCompletadas);
        ctx.setVariable("pendientes", tareasPendientes);
        ctx.setVariable("tareas", tareas); // Pasamos también la lista por si quieres imprimir los títulos

        // NOTA: Asegúrate de tener una constante o string con la plantilla de resumen
        engine.process(UrlConstants.TEMPLATE_LISTA, ctx, resp.getWriter());
    }
}