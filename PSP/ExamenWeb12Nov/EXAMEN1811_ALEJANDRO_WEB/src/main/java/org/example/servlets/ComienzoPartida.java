package org.example.servlets;

import config.ThymeleafConstants;
import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Biblioteca;
import org.example.model.Jugador;
import org.example.model.Partida;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/comenzar")
public class ComienzoPartida extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        Biblioteca biblioteca = (Biblioteca) req.getSession().getAttribute("biblioteca");
        if (biblioteca == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Falta iniciar sesion en biblioteca.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        Partida partida = new Partida();
        req.getSession().setAttribute("partida", partida);

        String nombre1 = req.getParameter("jugador1");
        String nombre2 = req.getParameter("jugador2");
        String palabraSecreta = req.getParameter("palabra");

        if(nombre1 == null || nombre1.isEmpty() || nombre2 == null || nombre2.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Faltan parámetros en los nombres.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (palabraSecreta == null || palabraSecreta.isEmpty() ){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Falta la palabra secreta");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (partida.isComenzada()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "La partida ya ha comenzado.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }


        if (req.getSession().getAttribute("finTorneo")==null){
            req.getSession().setAttribute("finTorneo",false);
        }

        if ((boolean)req.getSession().getAttribute("finTorneo")){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "El torneo ya ha terminado");
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

        partida.setJugador1(j1);
        partida.setJugador2(j2);
        partida.setPalabra(palabraSecreta);
        partida.setComenzada(true);

        String turnoActual = (Math.random() < 0.5) ? j1.getNombre() : j2.getNombre();

        req.getSession().setAttribute("turno", turnoActual);
        req.getSession().setAttribute("partida", partida);

        ctx.setVariable("mensajeBien", "Partida comenzada.");
        engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());

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
