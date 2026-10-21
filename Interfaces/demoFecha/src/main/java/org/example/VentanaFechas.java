package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class VentanaFechas extends JFrame {

    private QDate fechaInicio;
    private QDate fechaFin;
    private JButton btnComprobar;
    private LogicaFechas logica;

    public VentanaFechas() {

        this.setTitle("Comprobador de Fechas");
        this.setSize(450, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1));

        logica = new LogicaFechas();
        fechaInicio = new QDate();
        fechaFin = new QDate();
        btnComprobar = new JButton("Comprobar");

        JPanel panelInicio = new JPanel();
        panelInicio.add(new JLabel("Fecha Inicio:"));
        panelInicio.add(fechaInicio);
        this.add(panelInicio);

        JPanel panelFin = new JPanel();
        panelFin.add(new JLabel("Fecha Fin:"));
        panelFin.add(fechaFin);
        this.add(panelFin);

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnComprobar);
        this.add(panelBoton);

        btnComprobar.addActionListener(e -> evaluarFechas());
    }

    private void evaluarFechas() {
        try {
            Date inicio = fechaInicio.getDate();
            Date fin = fechaFin.getDate();

            String resultado = logica.calcularDiferencia(inicio, fin);
            JOptionPane.showMessageDialog(this, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (QDateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Fecha Inválida", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Orden", JOptionPane.ERROR_MESSAGE);
        }
    }
}