package org.example;

import config.ThymeleafConstants;
import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/tres")
public class TresEnRayaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Partida partidaActual = (Partida) req.getSession().getAttribute("partidaActual");
        String[] Tablero = (String[]) req.getSession().getAttribute("Tablero");
        String turnoActual = (String) req.getSession().getAttribute("turnoActual");

        if (partidaActual == null || Tablero == null || turnoActual == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No hay ninguna partida en curso. Ve a /comienzo primero.");
            return;
        }

        String nombre = req.getParameter("nombre");
        String coordenada = req.getParameter("coordenada");

        if (nombre == null || coordenada == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parametros. Usa ?nombre=Juan&coordenada=5");
            return;
        }

        if (!nombre.equalsIgnoreCase(turnoActual)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No es tu turno. Le toca a: " + turnoActual);
            return;
        }

        int pos;
        try {
            pos = Integer.parseInt(coordenada);
            if (pos < 1 || pos > 9) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "La coordenada debe ser del 1 al 9.");
                return;
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "La coordenada debe ser un numero.");
            return;
        }

        if (!Tablero[pos - 1].equals("-")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Esa casilla ya esta ocupada.");
            return;
        }

        String miSimbolo = nombre.equalsIgnoreCase(partidaActual.getJugador1().getNombre())
                ? partidaActual.getSimbolo1()
                : partidaActual.getSimbolo2();

        Tablero[pos - 1] = miSimbolo;
        partidaActual.registrarMovimiento(new Movimiento(nombre, coordenada));

        boolean ganador =
                (Tablero[0].equals(miSimbolo) && Tablero[1].equals(miSimbolo) && Tablero[2].equals(miSimbolo)) ||
                        (Tablero[3].equals(miSimbolo) && Tablero[4].equals(miSimbolo) && Tablero[5].equals(miSimbolo)) ||
                        (Tablero[6].equals(miSimbolo) && Tablero[7].equals(miSimbolo) && Tablero[8].equals(miSimbolo)) ||
                        (Tablero[0].equals(miSimbolo) && Tablero[3].equals(miSimbolo) && Tablero[6].equals(miSimbolo)) ||
                        (Tablero[1].equals(miSimbolo) && Tablero[4].equals(miSimbolo) && Tablero[7].equals(miSimbolo)) ||
                        (Tablero[2].equals(miSimbolo) && Tablero[5].equals(miSimbolo) && Tablero[8].equals(miSimbolo)) ||
                        (Tablero[0].equals(miSimbolo) && Tablero[4].equals(miSimbolo) && Tablero[8].equals(miSimbolo)) ||
                        (Tablero[2].equals(miSimbolo) && Tablero[4].equals(miSimbolo) && Tablero[6].equals(miSimbolo));

        boolean empate = true;
        for (String casilla : Tablero) {
            if (casilla.equals("-")) {
                empate = false;
                break;
            }
        }

        if (ganador || empate) {
            if (ganador) {
                Jugador jGanador = nombre.equalsIgnoreCase(partidaActual.getJugador1().getNombre()) ? partidaActual.getJugador1() : partidaActual.getJugador2();
                Jugador jPerdedor = nombre.equalsIgnoreCase(partidaActual.getJugador1().getNombre()) ? partidaActual.getJugador2() : partidaActual.getJugador1();

                partidaActual.setGanador(jGanador);
                partidaActual.setPerdedor(jPerdedor);
                jGanador.setVictorias(jGanador.getVictorias() + 1);
            }

            List<Partida> historialPartidas = (List<Partida>) getServletContext().getAttribute("historialPartidas");
            if (historialPartidas == null) {
                historialPartidas = new ArrayList<>();
                getServletContext().setAttribute("historialPartidas", historialPartidas);
            }
            historialPartidas.add(partidaActual);

            req.getSession().removeAttribute("partidaActual");
            req.getSession().removeAttribute("Tablero");
            req.getSession().removeAttribute("turnoActual");

            resp.setContentType("text/html");
            if (ganador) {
                resp.getWriter().println("<h1>¡Ha ganado " + nombre + " (" + miSimbolo + ")!</h1>");
            } else {
                resp.getWriter().println("<h1>¡Empate!</h1>");
            }
            resp.getWriter().println("<p><a href='/tu_proyecto/comienzo'>Nueva partida</a></p>");
            return;
        }

        String siguienteTurno = nombre.equalsIgnoreCase(partidaActual.getJugador1().getNombre())
                ? partidaActual.getJugador2().getNombre()
                : partidaActual.getJugador1().getNombre();

        req.getSession().setAttribute("turnoActual", siguienteTurno);

        String fila1 = Tablero[0] + " " + Tablero[1] + " " + Tablero[2];
        String fila2 = Tablero[3] + " " + Tablero[4] + " " + Tablero[5];
        String fila3 = Tablero[6] + " " + Tablero[7] + " " + Tablero[8];

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        ctx.setVariable("fila1", fila1);
        ctx.setVariable("fila2", fila2);
        ctx.setVariable("fila3", fila3);
        ctx.setVariable("siguienteTurno", siguienteTurno);
        ctx.setVariable("mensajeComienzo", "Partida: " + partidaActual.getJugador1().getNombre() + " (" + partidaActual.getSimbolo1() + ") VS " + partidaActual.getJugador2().getNombre() + " (" + partidaActual.getSimbolo2() + ").");

        resp.setContentType(ThymeleafConstants.CONTENT_TYPE);
        ((TemplateEngine) getServletContext().getAttribute(ThymeleafConstants.TEMPLATE_ENGINE_ATTR))
                .process(UrlConstants.TEMPLATE_TRES_EN_RAYA, ctx, resp.getWriter());
    }
}