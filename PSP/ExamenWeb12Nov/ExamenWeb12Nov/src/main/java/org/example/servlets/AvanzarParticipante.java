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
import java.util.List;
import java.util.Objects;
import java.util.Random;

@WebServlet("/avanzar")
public class AvanzarParticipante extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String nombre = req.getParameter("nombre");
        Random random = new Random();

        if (nombre == null || nombre.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Faltan parámetros obligatorios.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (getServletContext().getAttribute("longitud") == null) {
            ctx.setVariable("mensajeError", "Falta configurar la carrera");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        int longitud = (int) getServletContext().getAttribute("longitud");


        if (getServletContext().getAttribute("listaParticipantes") == null) {
            ctx.setVariable("mensajeError", "Falta añadir participantes");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (!(boolean) getServletContext().getAttribute("carreraComenzada")) {
            ctx.setVariable("mensajeError", "Falta comenzar la carrera");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if ((boolean) getServletContext().getAttribute("hanGanado")) {
            ctx.setVariable("mensajeError", "Alguien ya ha ganado la carrera");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        List<Participante> listaParticipantes = (List<Participante>) getServletContext().getAttribute("listaParticipantes");

        Participante participanteExiste = listaParticipantes.stream()
                .filter(p -> Objects.equals(p.getNombre(), nombre))
                .findFirst()
                .orElse(null);

        if (participanteExiste == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ctx.setVariable("mensajeError", "No existe ningún participante con ese nombre.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());

        } else {


            if (req.getSession().getAttribute("distanciaParticipante") == null) {
                req.getSession().setAttribute("distanciaParticipante", 0);
            }

            int distanciaParticipante = (int) req.getSession().getAttribute("distanciaParticipante");
            int distanciaRecorrida = random.nextInt(1, 7);
            distanciaParticipante = distanciaParticipante + distanciaRecorrida;


            req.getSession().setAttribute("distanciaParticipante", distanciaParticipante);

            if (distanciaParticipante >= longitud) {
                boolean hanGanado = true;
                getServletContext().setAttribute("hanGanado", hanGanado);
                ctx.setVariable("mensajeBien", "El participante " + nombre + " se ha movido " + distanciaRecorrida + " Total recorrido: " + distanciaParticipante + " Y HA GANADO LA CARRERA!");
                engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());
                return;
            }

            ctx.setVariable("mensajeBien", "El participante " + nombre + " se ha movido " + distanciaRecorrida + " Total recorrido: " + distanciaParticipante);
            engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());

        }

    }
}
