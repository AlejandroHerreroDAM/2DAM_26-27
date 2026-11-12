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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/comenzar")
public class ComienzoCarrera extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        resp.setContentType(config.ThymeleafConstants.CONTENT_TYPE);
        TemplateEngine engine = (TemplateEngine) getServletContext().getAttribute(config.ThymeleafConstants.TEMPLATE_ENGINE_ATTR);

        String password = req.getParameter("password");

        if (password == null || password.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ctx.setVariable("mensajeError", "Faltan parámetros obligatorios.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        String contrasena = "123";
        if (!password.equals(contrasena)){
            ctx.setVariable("mensajeError", "La contraseña no coincide.");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        if (getServletContext().getAttribute("longitud") == null){
            ctx.setVariable("mensajeError", "Falta configurar la carrera");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        List<Participante> listaParticipantes = (List<Participante>) getServletContext().getAttribute("listaParticipantes");

        if (listaParticipantes == null) {
            ctx.setVariable("mensajeError", "Falta añadir participantes");
            engine.process(UrlConstants.TEMPLATE_ERROR, ctx, resp.getWriter());
            return;
        }

        boolean carreraComenzada = true;

        getServletContext().setAttribute("carreraComenzada", carreraComenzada);

        ctx.setVariable("mensajeBien", "Carrera comenzada con éxito.");

        engine.process(UrlConstants.TEMPLATE_COMPLETADO, ctx, resp.getWriter());
    }

}
