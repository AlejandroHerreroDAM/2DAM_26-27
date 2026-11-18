package org.example.servlets;

import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Intento;
import org.example.model.Jugador;
import org.example.model.Partida;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/jugar")
public class JugarPartida extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String respuesta = req.getParameter("respuesta");

        if(respuesta == null || respuesta.isEmpty()){
            ctx.setVariable("mensajeError", "Te has olvidado de enviar la respuesta en la URL.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        Partida partida = (Partida) req.getSession().getAttribute("partida");
        if (partida == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Falta iniciar sesion en biblioteca.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (!partida.isComenzada()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Falta configurar la partida.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (partida.isFinalizada()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "La partida ya ha terminado.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        String turnoActual = (String) req.getSession().getAttribute("turno");
        Jugador jugadorActual = turnoActual.equalsIgnoreCase(partida.getJugador1().getNombre()) ? partida.getJugador1() : partida.getJugador2();

        boolean ganador = false;

        if (!respuesta.equalsIgnoreCase(partida.getPalabra())){
            jugadorActual.registrarIntento(new Intento(respuesta));

            ctx.setVariable("mensajeBien", "Palabra incorrecta! "+ (6-jugadorActual.getHistorialIntentos().size()) + " intentos restantes");
        } else {
            ganador = true;
        }
        
        boolean empate = !ganador && partida.getJugador1().getHistorialIntentos().size()+1 == 7 && partida.getJugador2().getHistorialIntentos().size()+1 == 7;
        
        if (ganador || empate) {

            List<Partida> historialPartidas = (List<Partida>) getServletContext().getAttribute("historialPartidas");

            if (historialPartidas == null) {
                historialPartidas = new ArrayList<>();
                getServletContext().setAttribute("historialPartidas", historialPartidas);
            }

            if (ganador) {
                Jugador jGanador = turnoActual.equalsIgnoreCase(partida.getJugador1().getNombre()) ? partida.getJugador1() : partida.getJugador2();
                Jugador jPerdedor = turnoActual.equalsIgnoreCase(partida.getJugador1().getNombre()) ? partida.getJugador2() : partida.getJugador1();

                partida.setGanador(jGanador);
                partida.setPerdedor(jPerdedor);
                partida.setFinalizada(true);
                jGanador.setVictorias(jGanador.getVictorias() + 1);
                ctx.setVariable("mensajeBien", "¡Partida finalizada! Ha ganado " + partida.getGanador().getNombre());

            } else {
                ctx.setVariable("mensajeBien", "¡Partida finalizada! Ha habido un empate.");
                partida.setFinalizada(true);
            }

            historialPartidas.add(partida);

        } else {
            ctx.setVariable("turno", "Turno de: "+ turnoActual +" Vidas "+ (6-(partida.getJugador1().getHistorialIntentos().size())) );
            String siguienteTurno = turnoActual.equalsIgnoreCase(partida.getJugador1().getNombre()) ? partida.getJugador2().getNombre() : partida.getJugador1().getNombre();
            req.getSession().setAttribute("turno", siguienteTurno);


        }

        engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());
    }

}
