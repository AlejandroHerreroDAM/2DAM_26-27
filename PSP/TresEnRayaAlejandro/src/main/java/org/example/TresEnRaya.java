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

@WebServlet("/tres")
public class TresEnRaya extends HttpServlet {




    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String marca = req.getParameter("marca");
        String coordenada = req.getParameter("coordenada");

        if (marca==null || coordenada==null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"ASver si te entreras cabron, parametro requertido");
            return;
        }

        marca = marca.toUpperCase();
        if (!marca.equals("X") && !marca.equals("O")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"La marca tiene que ser X o O.");
            return;
        }

        int pos;
        try {
            pos = Integer.parseInt(coordenada);
            if (pos < 1 || pos > 9) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"La coordenada debe ser del 1 al 9.");
                return;
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"La coordenada debe ser un numero.");
            return;
        }

        String[] Tablero = (String[]) req.getSession().getAttribute("Tablero");
        String Turno = (String) req.getSession().getAttribute("Turno");

        if (Tablero==null) {
            Tablero = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", "-"};
            Turno = "X";
            req.getSession().setAttribute("Tablero",Tablero);
            req.getSession().setAttribute("Turno", Turno);
        }

        if (!marca.equals(Turno)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"No es tu turno. Le toca a: " + Turno);
            return;
        }

        if (!Tablero[pos-1].equals("-")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"Esa casilla ya esta ocupada.");
            return;
        }

        Tablero[pos-1]=marca;

        String siguienteTurno = marca.equals("X") ? "O" : "X";
        req.getSession().setAttribute("Turno", siguienteTurno);

        String fila1 = Tablero[0]+ " " + Tablero[1]+ " " + Tablero[2];
        String fila2 = Tablero[3]+ " " + Tablero[4]+ " " + Tablero[5];
        String fila3 = Tablero[6]+ " " + Tablero[7] + " " + Tablero[8];

        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);
        ctx.setVariable("fila1", fila1);
        ctx.setVariable("fila2", fila2);
        ctx.setVariable("fila3", fila3);
        ctx.setVariable("siguienteTurno", siguienteTurno);
        resp.setContentType(ThymeleafConstants.CONTENT_TYPE);
        ((TemplateEngine)getServletContext().getAttribute(ThymeleafConstants.TEMPLATE_ENGINE_ATTR))
                .process(UrlConstants.TEMPLATE_TRES_EN_RAYA, ctx, resp.getWriter());


    }


}