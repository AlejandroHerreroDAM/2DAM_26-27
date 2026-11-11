package org.example;

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
import java.util.List;

@WebServlet("/eliminar")
public class EliminarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String nombre = req.getParameter("nombre");

        List<Partida> historialPartidas = (List<Partida>) getServletContext().getAttribute("historialPartidas");
        List<Jugador> jugadoresTorneo = (List<Jugador>) getServletContext().getAttribute("jugadoresTorneo");

        if (nombre != null && !nombre.isEmpty()) {
            if (jugadoresTorneo != null) {
                jugadoresTorneo.removeIf(jugador -> jugador.getNombre().equalsIgnoreCase(nombre));
            }

            if (historialPartidas != null) {
                historialPartidas.removeIf(p -> (p.getJugador1() != null && p.getJugador1().getNombre().equalsIgnoreCase(nombre)) || (p.getJugador2() != null && p.getJugador2().getNombre().equalsIgnoreCase(nombre)));
            }

        } else {
            ctx.setVariable("mensajeError", "Te has olvidado de enviar el nombre en la URL.");
            engine.process(config.UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
        }

    }
}
