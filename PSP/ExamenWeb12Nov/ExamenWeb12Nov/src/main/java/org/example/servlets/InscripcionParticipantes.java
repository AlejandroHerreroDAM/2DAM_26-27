package org.example.servlets;

import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Participante;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/inscribir")
public class InscripcionParticipantes extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String nombre = req.getParameter("nombre");

        if (nombre == null || nombre.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Faltan parámetros obligatorios.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        List<Participante> listaParticipantes = (List<Participante>) getServletContext().getAttribute("jugadoresTorneo");
        if (listaParticipantes == null) {
            listaParticipantes = new ArrayList<>();
            getServletContext().setAttribute("listaParticipantes", listaParticipantes);
        }

        Participante p = buscarOCrearJugador(listaParticipantes, nombre);

        ctx.setVariable("mensajeBien", "Participante "+ nombre +" añadido con éxito.");

        engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());
    }

    private Participante buscarOCrearJugador(List<Participante> lista, String nombre) {
        for (Participante p : lista) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        Participante nuevo = new Participante(nombre);
        lista.add(nuevo);
        return nuevo;
    }
}
