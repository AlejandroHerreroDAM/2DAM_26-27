package org.example;

import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Jugador;
import org.example.model.Movimiento;
import org.example.model.Partida;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet("/tres")
public class TresEnRayaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String posicion = req.getParameter("posicion");

        if (posicion == null || posicion.isEmpty()) {
            ctx.setVariable("mensajeError", "Te has olvidado de enviar la posición en la URL.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        int posicionAConvertir;
        try {
            posicionAConvertir = Integer.parseInt(posicion);
        } catch (NumberFormatException e) {
            ctx.setVariable("mensajeError", "Los puntos deben ser números, no letras.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (posicionAConvertir < 1 || posicionAConvertir > 9) {
            ctx.setVariable("mensajeError", "La posición debe ser un número entre 1 y 9.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        var session = req.getSession();

        Partida partidaActual = (Partida) session.getAttribute("partidaActual");
        String[] tablero = (String[]) session.getAttribute("Tablero");
        String turnoActual = (String) session.getAttribute("turnoActual");

        if (partidaActual == null || turnoActual == null) {
            ctx.setVariable("mensajeError", "Debes registrar los jugadores antes de empezar");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (tablero == null) {
            tablero = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", "-"};
            session.setAttribute("Tablero", tablero);
        }

        if (!Objects.equals(tablero[posicionAConvertir - 1], "-")) {
            pintarTablero(tablero, ctx);
            ctx.setVariable("siguienteTurno", turnoActual);
            ctx.setVariable("mensajeComienzo", "Partida " + partidaActual.getJugador1().getNombre() + " (" + partidaActual.getSimbolo1() + ") VS " + partidaActual.getJugador2().getNombre() + " (" + partidaActual.getSimbolo2() + ").");
            ctx.setVariable("mensajeErrorPartida", "Esa posición ya está ocupada, elige otra coordenada.");

            engine.process(UrlConstants.TEMPLATE_TRES_EN_RAYA, ctx, resp.getWriter());
            return;
        }

        String simboloActual = turnoActual.equalsIgnoreCase(partidaActual.getJugador1().getNombre()) ? partidaActual.getSimbolo1() : partidaActual.getSimbolo2();

        tablero[posicionAConvertir - 1] = simboloActual;
        partidaActual.registrarMovimiento(new Movimiento(turnoActual, posicion));

        String siguienteTurno = turnoActual.equalsIgnoreCase(partidaActual.getJugador1().getNombre()) ? partidaActual.getJugador2().getNombre() : partidaActual.getJugador1().getNombre();

        session.setAttribute("turnoActual", siguienteTurno);

        pintarTablero(tablero, ctx);
        ctx.setVariable("siguienteTurno", siguienteTurno);
        ctx.setVariable("mensajeComienzo", "Partida " + partidaActual.getJugador1().getNombre() + " (" + partidaActual.getSimbolo1() + ") VS " + partidaActual.getJugador2().getNombre() + " (" + partidaActual.getSimbolo2() + ")");


        boolean ganador =
                (tablero[0].equals(simboloActual) && tablero[1].equals(simboloActual) && tablero[2].equals(simboloActual)) ||
                        (tablero[3].equals(simboloActual) && tablero[4].equals(simboloActual) && tablero[5].equals(simboloActual)) ||
                        (tablero[6].equals(simboloActual) && tablero[7].equals(simboloActual) && tablero[8].equals(simboloActual)) ||
                        (tablero[0].equals(simboloActual) && tablero[3].equals(simboloActual) && tablero[6].equals(simboloActual)) ||
                        (tablero[1].equals(simboloActual) && tablero[4].equals(simboloActual) && tablero[7].equals(simboloActual)) ||
                        (tablero[2].equals(simboloActual) && tablero[5].equals(simboloActual) && tablero[8].equals(simboloActual)) ||
                        (tablero[0].equals(simboloActual) && tablero[4].equals(simboloActual) && tablero[8].equals(simboloActual)) ||
                        (tablero[2].equals(simboloActual) && tablero[4].equals(simboloActual) && tablero[6].equals(simboloActual));

        boolean empate = !ganador && partidaActual.getContadorMovimientos() == 9;

        if (ganador || empate) {

            List<Partida> historialPartidas = (List<Partida>) getServletContext().getAttribute("historialPartidas");

            if (historialPartidas == null) {
                historialPartidas = new ArrayList<>();
                getServletContext().setAttribute("historialPartidas", historialPartidas);
            }

            if (ganador) {
                Jugador jGanador = turnoActual.equalsIgnoreCase(partidaActual.getJugador1().getNombre()) ? partidaActual.getJugador1() : partidaActual.getJugador2();
                Jugador jPerdedor = turnoActual.equalsIgnoreCase(partidaActual.getJugador1().getNombre()) ? partidaActual.getJugador2() : partidaActual.getJugador1();

                partidaActual.setGanador(jGanador);
                partidaActual.setPerdedor(jPerdedor);
                jGanador.setVictorias(jGanador.getVictorias() + 1);
                ctx.setVariable("mensajeComienzo", "¡Partida finalizada! Ha ganado " + partidaActual.getGanador().getNombre());

            } else {
                ctx.setVariable("mensajeComienzo", "¡Partida finalizada! Ha habido un empate.");
            }

            historialPartidas.add(partidaActual);

            session.removeAttribute("partidaActual");
            session.removeAttribute("Tablero");
            session.removeAttribute("turnoActual");

            engine.process(UrlConstants.TEMPLATE_TRES_EN_RAYA, ctx, resp.getWriter());
            return;
        }

        engine.process(UrlConstants.TEMPLATE_TRES_EN_RAYA, ctx, resp.getWriter());
    }

    private static void pintarTablero(String[] tablero, WebContext ctx) {
        String fila1 = tablero[0] + " " + tablero[1] + " " + tablero[2];
        String fila2 = tablero[3] + " " + tablero[4] + " " + tablero[5];
        String fila3 = tablero[6] + " " + tablero[7] + " " + tablero[8];

        ctx.setVariable("fila1", fila1);
        ctx.setVariable("fila2", fila2);
        ctx.setVariable("fila3", fila3);
    }

}