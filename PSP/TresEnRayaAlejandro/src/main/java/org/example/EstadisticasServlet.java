package org.example;

import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Jugador;
import org.example.model.Partida;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@WebServlet("/estadisticas")
public class EstadisticasServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        List<Partida> historialPartidas = (List<Partida>) getServletContext().getAttribute("historialPartidas");

        if (historialPartidas == null) {
            historialPartidas = new ArrayList<>();
            getServletContext().setAttribute("historialPartidas", historialPartidas);
        }

        List<Jugador> jugadoresTorneo = (List<Jugador>) getServletContext().getAttribute("jugadoresTorneo");

        if (jugadoresTorneo == null) {
            jugadoresTorneo = new ArrayList<>();
            getServletContext().setAttribute("jugadoresTorneo", jugadoresTorneo);
        }

        String nombre = req.getParameter("nombre");

        String partida = req.getParameter("partida");

        if (nombre != null && !nombre.isEmpty()) {
            ctx.setVariable("tituloEstadisticas", "Estadísticas de " + nombre);
            ctx.setVariable("tipoVista", "porNombre");

            List<Partida> partidasDelJugador = new ArrayList<>();

            for (Partida p : historialPartidas) {
                boolean juegaComoJ1 = p.getJugador1() != null && p.getJugador1().getNombre().equalsIgnoreCase(nombre);
                boolean juegaComoJ2 = p.getJugador2() != null && p.getJugador2().getNombre().equalsIgnoreCase(nombre);

                if (juegaComoJ1 || juegaComoJ2) {
                    partidasDelJugador.add(p);
                }
            }

            ctx.setVariable("partidasJugador", partidasDelJugador);
            ctx.setVariable("nombreBuscado", nombre);


        } else if (partida != null && !partida.isEmpty()) {
            ctx.setVariable("tipoVista", "detallePartida");

            try {
                int indice = Integer.parseInt(partida);

                if (indice >= 0 && indice < historialPartidas.size()) {
                    Partida p = historialPartidas.get(indice);
                    ctx.setVariable("tituloEstadisticas", "Detalle de la Partida #" + indice);
                    ctx.setVariable("partidaSeleccionada", p);
                    ctx.setVariable("movimientos", p.getMovimientos());
                } else {
                    ctx.setVariable("tituloEstadisticas", "Error: La partida no existe");
                }

            } catch (NumberFormatException e) {
                ctx.setVariable("tituloEstadisticas", "Error: Número de partida inválido");
            }

        } else {
            ctx.setVariable("tipoVista", "general");

            List<Jugador> ranking = new ArrayList<>(jugadoresTorneo);
            ranking.sort(Comparator.comparing(Jugador::getVictorias).reversed());

            ctx.setVariable("tituloEstadisticas", "Ranking General");
            ctx.setVariable("ranking", ranking);

        }

        engine.process(UrlConstants.TEMPLATE_ESTADISTICAS, ctx, resp.getWriter());

    }
}