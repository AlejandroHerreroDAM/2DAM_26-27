package org.example.servlets;

import config.UrlConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Jugador;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@WebServlet("/fin")
public class MostrarPodio extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        req.getSession().setAttribute("finTorneo",true);

        List<Jugador> jugadoresTorneo = (List<Jugador>) getServletContext().getAttribute("jugadoresTorneo");
        if (jugadoresTorneo == null) {
            jugadoresTorneo = new ArrayList<>();
        }
        List<Jugador> ranking = new ArrayList<>(jugadoresTorneo);
        ranking.sort(Comparator.comparingInt(Jugador::getVictorias).reversed());

        var session = req.getSession();
        Integer pasoCeremonia = (Integer) session.getAttribute("pasoCeremonia");

        if (pasoCeremonia == null) {
            pasoCeremonia = 1;
        }

        String tituloPuesto = "";
        Jugador jugadorPremiado = null;

        if (pasoCeremonia == 1) {
            tituloPuesto = "Tercera posicion:";
            if (ranking.size() >= 3) jugadorPremiado = ranking.get(2);

        } else if (pasoCeremonia == 2) {
            tituloPuesto = "Segunda posicion:";
            if (ranking.size() >= 2) jugadorPremiado = ranking.get(1);

        } else {
            tituloPuesto = "Primera posicion:";
            if (ranking.size() >= 1) jugadorPremiado = ranking.get(0);
        }

        session.setAttribute("pasoCeremonia", pasoCeremonia + 1);

        ctx.setVariable("tituloPuesto", tituloPuesto);
        ctx.setVariable("jugadorPremiado", jugadorPremiado);
        ctx.setVariable("pasoActual", pasoCeremonia);

        engine.process(UrlConstants.TEMPLATE_FINTORNEO, ctx, resp.getWriter());
    }

}
