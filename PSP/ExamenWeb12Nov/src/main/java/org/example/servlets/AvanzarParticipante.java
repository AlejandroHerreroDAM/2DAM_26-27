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
import java.util.Random;

@WebServlet("/avanzar")
public class AvanzarParticipante extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String nombre = req.getParameter("nombre");
        if (nombre == null || nombre.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Debes indicar el nombre del participante.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        Carrera carrera = (Carrera) req.getSession().getAttribute("carrera");
        if (carrera == null || !carrera.isComenzada()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "La carrera no está configurada o aún no ha comenzado.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (carrera.isFinalizada()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "La carrera ya ha terminado. Ganador: " + carrera.getGanador());
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        Participante participante = carrera.buscarParticipante(nombre.trim());
        if (participante == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ctx.setVariable("mensajeError", "El participante '" + nombre + "' no está inscrito.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        // Control de turno: no puede tirar dos veces en la misma ronda
        if (!carrera.puedeTirarEnRondaActual(participante)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", participante.getNombre() + " ya ha tirado en la ronda " + carrera.getRondaActual() + ". Espera al resto.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        int dado = new Random().nextInt(1, 7);
        participante.registrarTirada(dado);
        carrera.comprobarFinDeRonda();

        // Construimos el resumen de distancias de todos los jugadores como pide el enunciado
        StringBuilder estadoJugadores = new StringBuilder();

        carrera.getParticipantes()
                .forEach(p -> estadoJugadores.append(" | ")
                        .append(p.getNombre())
                        .append(": ")
                        .append(p.getDistanciaTotal())
                        .append("/")
                        .append(carrera.getLongitud()));

//        for (Participante p : carrera.getParticipantes()) {
//            estadoJugadores.append(" | ").append(p.getNombre()).append(": ").append(p.getDistanciaTotal()).append("/").append(carrera.getLongitud());
//        }

        if (participante.getDistanciaTotal() >= carrera.getLongitud()) {
            carrera.setFinalizada(true);
            carrera.setGanador(participante.getNombre());
            ctx.setVariable("mensajeBien", "¡" + participante.getNombre() + " saca un " + dado + " y GANA LA CARRERA!" + estadoJugadores);
        } else {
            ctx.setVariable("mensajeBien", participante.getNombre() + " saca un " + dado + ". (Siguiente ronda: " + carrera.getRondaActual() + ")" + estadoJugadores);
        }

        engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());
    }
}