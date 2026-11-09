package org.example;

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

@WebServlet("/comienzo")
public class ComienzoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String nombre1 = req.getParameter("nombre1");
        String nombre2 = req.getParameter("nombre2");
        String simbolo1 = req.getParameter("simbolo1");
        String simbolo2 = req.getParameter("simbolo2");

        if (nombre1 == null || nombre2 == null || simbolo1 == null || simbolo2 == null) {
            ctx.setVariable("mensajeError", "Faltan parametros.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (simbolo1.length() != 1 || simbolo2.length() != 1) {
            ctx.setVariable("mensajeError", "El simbolo de cada jugador debe ser un solo caracter.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (simbolo1.equalsIgnoreCase(simbolo2)) {
            ctx.setVariable("mensajeError", "Los jugadores no pueden elegir el mismo simbolo.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }



        List<Jugador> jugadoresTorneo = (List<Jugador>) getServletContext().getAttribute("jugadoresTorneo");
        if (jugadoresTorneo == null) {
            jugadoresTorneo = new ArrayList<>();
            getServletContext().setAttribute("jugadoresTorneo", jugadoresTorneo);
        }

        Jugador j1 = buscarOCrearJugador(jugadoresTorneo, nombre1);
        Jugador j2 = buscarOCrearJugador(jugadoresTorneo, nombre2);

        Partida partidaActual = new Partida(j1, j2, simbolo1, simbolo2);

        String turnoActual = (Math.random() < 0.5) ? j1.getNombre() : j2.getNombre();

        String[] tablero = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", "-"};

        req.getSession().setAttribute("partidaActual", partidaActual);
        req.getSession().setAttribute("turnoActual", turnoActual);
        req.getSession().setAttribute("Tablero", tablero);

        ctx.setVariable("fila1", "- - -");
        ctx.setVariable("fila2", "- - -");
        ctx.setVariable("fila3", "- - -");
        ctx.setVariable("siguienteTurno", turnoActual);
        ctx.setVariable("mensajeComienzo", "Partida: " + nombre1 + " (" + simbolo1 + ") VS " + nombre2 + " (" + simbolo2 + ").");

        engine.process(UrlConstants.TEMPLATE_TRES_EN_RAYA, ctx, resp.getWriter());
    }

    private Jugador buscarOCrearJugador(List<Jugador> lista, String nombre) {
        for (Jugador j : lista) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                return j;
            }
        }
        Jugador nuevo = new Jugador(nombre);
        lista.add(nuevo);
        return nuevo;
    }
}