package org.example;

import javax.swing.*;
import java.awt.*;

public class VentanaJuego extends JFrame {

    private LogicaJuego logica;

    private final JTextField txtDia;
    private final JTextField txtMes;
    private final JTextField txtAno;

    private final JLabel lblFeedDia;
    private final JLabel lblFeedMes;
    private final JLabel lblFeedAno;

    private final JLabel lblTextoDia;
    private final JLabel lblTextoMes;
    private final JLabel lblTextoAno;

    private final JLabel lblIntentos;

    public VentanaJuego() {
        logica = new LogicaJuego();

        setTitle("Adivina la Fecha");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 250);
        setLayout(new GridLayout(3, 1));
        setLocationRelativeTo(null);

        JPanel pnlInputs = new JPanel(new FlowLayout());
        txtDia = new JTextField(3);
        txtMes = new JTextField(3);
        txtAno = new JTextField(4);

        pnlInputs.add(new JLabel("Día:"));
        pnlInputs.add(txtDia);
        pnlInputs.add(new JLabel("Mes:"));
        pnlInputs.add(txtMes);
        pnlInputs.add(new JLabel("Año:"));
        pnlInputs.add(txtAno);

        JPanel pnlFeedback = new JPanel(new GridLayout(2, 3, 5, 5));
        pnlFeedback.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblFeedDia = crearLabelColor("Día");
        lblFeedMes = crearLabelColor("Mes");
        lblFeedAno = crearLabelColor("Año");

        lblTextoDia = new JLabel("", SwingConstants.CENTER);
        lblTextoMes = new JLabel("", SwingConstants.CENTER);
        lblTextoAno = new JLabel("", SwingConstants.CENTER);

        pnlFeedback.add(lblFeedDia);
        pnlFeedback.add(lblFeedMes);
        pnlFeedback.add(lblFeedAno);
        pnlFeedback.add(lblTextoDia);
        pnlFeedback.add(lblTextoMes);
        pnlFeedback.add(lblTextoAno);

        JPanel pnlControl = new JPanel(new FlowLayout());
        JButton btnComprobar = new JButton("Comprobar");
        lblIntentos = new JLabel("Intentos: 0");

        pnlControl.add(btnComprobar);
        pnlControl.add(lblIntentos);

        add(pnlInputs);
        add(pnlFeedback);
        add(pnlControl);

        btnComprobar.addActionListener(e -> comprobarIntento());
    }

    private JLabel crearLabelColor(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(Color.LIGHT_GRAY);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        return lbl;
    }

    private void comprobarIntento() {
        try {
            int d = Integer.parseInt(txtDia.getText());
            int m = Integer.parseInt(txtMes.getText());
            int a = Integer.parseInt(txtAno.getText());

            logica.incrementarIntento();
            lblIntentos.setText("Intentos: " + logica.getIntentos());

            int compD = logica.compararDia(d);
            int compM = logica.compararMes(m);
            int compA = logica.compararAno(a);

            aplicarFeedback(lblFeedDia, lblTextoDia, compD);
            aplicarFeedback(lblFeedMes, lblTextoMes, compM);
            aplicarFeedback(lblFeedAno, lblTextoAno, compA);

            if (compD == 0 && compM == 0 && compA == 0) {
                Object[] opciones = {"Jugar otra vez"};
                int eleccion = JOptionPane.showOptionDialog(this,
                        "¡Felicidades! Has adivinado la fecha en " + logica.getIntentos() + " intentos.",
                        "¡Has ganado!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]);

                if (eleccion == 0) {
                    reiniciarJuego();
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: Por favor, introduce solo números.",
                    "Entrada inválida",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarFeedback(JLabel lblColor, JLabel lblTexto, int comparacion) {
        if (comparacion == 0) {
            lblColor.setBackground(Color.GREEN);
            lblTexto.setText("¡Correcto!");
            lblTexto.setForeground(new Color(0, 150, 0));
        } else if (comparacion > 0) {
            lblColor.setBackground(Color.RED);
            lblTexto.setText("Te has pasado");
            lblTexto.setForeground(Color.RED);
        } else {
            lblColor.setBackground(Color.BLUE);
            lblTexto.setText("Te has quedado corto");
            lblTexto.setForeground(Color.BLUE);
        }
    }

    private void reiniciarJuego() {
        logica = new LogicaJuego();

        txtDia.setText("");
        txtMes.setText("");
        txtAno.setText("");
        lblIntentos.setText("Intentos: 0");

        lblFeedDia.setBackground(Color.LIGHT_GRAY);
        lblFeedMes.setBackground(Color.LIGHT_GRAY);
        lblFeedAno.setBackground(Color.LIGHT_GRAY);

        lblTextoDia.setText("");
        lblTextoMes.setText("");
        lblTextoAno.setText("");
    }
}