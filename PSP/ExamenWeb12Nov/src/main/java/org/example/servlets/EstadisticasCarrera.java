package org.example.servlets;

import config.ThymeleafConstants;
import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Carrera;
import org.example.model.Participante;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet("/estadisticas")
public class EstadisticasCarrera extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        Carrera carrera = (Carrera) req.getSession().getAttribute("carrera");
        if (carrera == null || carrera.getParticipantes().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "No hay datos de carrera disponibles en esta sesión.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        StringBuilder resumen = new StringBuilder("Estadísticas de tiradas por turno: ");

        carrera.getParticipantes()
                .forEach(p -> resumen.append("\n- ").append(p.getNombre())
                        .append(" (Total: ").append(p.getDistanciaTotal()).append("): ")
                        .append(p.getHistorialTiradas()));

        ctx.setVariable("mensajeBien", resumen.toString());
        ctx.setVariable("participantes", carrera.getParticipantes());
        engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());
    }
}