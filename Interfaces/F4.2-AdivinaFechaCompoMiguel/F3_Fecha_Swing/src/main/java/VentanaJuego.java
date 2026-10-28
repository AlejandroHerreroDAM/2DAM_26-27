import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class VentanaJuego extends JFrame {

    private LogicaJuego logica;

    private QDateImplementation qDateInicio;
    private QDateImplementation qDateFin;
    private QDateImplementation qDateIntento;

    private JPanel pnlJuego;
    private JButton btnIniciar;

    private JLabel lblFeedDia, lblFeedMes, lblFeedAno;
    private JLabel lblTextoDia, lblTextoMes, lblTextoAno;

    private JLabel lblIntentos;

    public VentanaJuego() {
        setTitle("Adivina la Fecha - QDateImplementation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        JPanel pnlConfig = new JPanel(new GridLayout(3, 1));
        pnlConfig.setBorder(BorderFactory.createTitledBorder("1. Configurar Dificultad"));

        JPanel pnlInicio = new JPanel(new FlowLayout());
        pnlInicio.add(new JLabel("Fecha Inicio:"));
        qDateInicio = new QDateImplementation();
        try {
            qDateInicio.setDate(2015, 1, 1);
        } catch (Exception ignored) {}
        pnlInicio.add(qDateInicio);

        JPanel pnlFin = new JPanel(new FlowLayout());
        pnlFin.add(new JLabel("Fecha Fin:"));
        qDateFin = new QDateImplementation();
        try {
            qDateFin.setDate(2024, 12, 31);
        } catch (Exception ignored) {}
        pnlFin.add(qDateFin);

        JPanel pnlBtnConfig = new JPanel();
        JButton btnRangoAleatorio = new JButton("Rango Aleatorio");
        btnIniciar = new JButton("Establecer Rango y Jugar");
        pnlBtnConfig.add(btnRangoAleatorio);
        pnlBtnConfig.add(btnIniciar);

        pnlConfig.add(pnlInicio);
        pnlConfig.add(pnlFin);
        pnlConfig.add(pnlBtnConfig);

        pnlJuego = new JPanel(new BorderLayout());
        pnlJuego.setBorder(BorderFactory.createTitledBorder("2. Adivina la Fecha"));
        pnlJuego.setVisible(false);

        JPanel pnlIntento = new JPanel(new FlowLayout());
        pnlIntento.add(new JLabel("Tu Intento:"));
        qDateIntento = new QDateImplementation();
        pnlIntento.add(qDateIntento);

        JButton btnComprobar = new JButton("Comprobar");
        pnlIntento.add(btnComprobar);

        pnlJuego.add(pnlIntento, BorderLayout.NORTH);

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

        pnlJuego.add(pnlFeedback, BorderLayout.CENTER);

        JPanel pnlControl = new JPanel(new FlowLayout());
        lblIntentos = new JLabel("Intentos: 0");
        lblIntentos.setFont(new Font("Arial", Font.BOLD, 14));
        pnlControl.add(lblIntentos);

        pnlJuego.add(pnlControl, BorderLayout.SOUTH);

        add(pnlConfig, BorderLayout.NORTH);
        add(pnlJuego, BorderLayout.CENTER);

        btnRangoAleatorio.addActionListener(e -> generarRangoAleatorio());
        btnIniciar.addActionListener(e -> iniciarJuego());
        btnComprobar.addActionListener(e -> comprobarIntento());
    }

    private void generarRangoAleatorio() {
        try {
            LocalDate[] rango = LogicaJuego.generarRangoAleatorio(1990, 2025);
            qDateInicio.setDate(rango[0].getYear(), rango[0].getMonthValue(), rango[0].getDayOfMonth());
            qDateFin.setDate(rango[1].getYear(), rango[1].getMonthValue(), rango[1].getDayOfMonth());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al generar rango", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel crearLabelColor(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(Color.LIGHT_GRAY);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        return lbl;
    }

    private void iniciarJuego() {
        try {
            Date utilInicio = qDateInicio.getDate();
            Date utilFin = qDateFin.getDate();

            LocalDate localInicio = utilInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localFin = utilFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (localInicio.isAfter(localFin)) {
                JOptionPane.showMessageDialog(this, "La fecha de inicio debe ser anterior o igual a la fecha de fin.", "Error de rango", JOptionPane.ERROR_MESSAGE);
                return;
            }

            logica = new LogicaJuego(localInicio, localFin);

            pnlJuego.setVisible(true);
            btnIniciar.setText("Reiniciar con nuevas fechas");
            lblIntentos.setText("Intentos: 0");
            limpiarFeedback();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error en fechas", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void comprobarIntento() {
        try {
            Date utilIntento = qDateIntento.getDate();
            LocalDate localIntento = utilIntento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            int d = localIntento.getDayOfMonth();
            int m = localIntento.getMonthValue();
            int a = localIntento.getYear();

            logica.incrementarIntento();
            lblIntentos.setText("Intentos: " + logica.getIntentos());

            int compD = logica.compararDia(d);
            int compM = logica.compararMes(m);
            int compA = logica.compararAno(a);

            aplicarFeedback(lblFeedDia, lblTextoDia, compD);
            aplicarFeedback(lblFeedMes, lblTextoMes, compM);
            aplicarFeedback(lblFeedAno, lblTextoAno, compA);

            if (compD == 0 && compM == 0 && compA == 0) {
                JOptionPane.showMessageDialog(this,
                        "¡Felicidades! Has adivinado la fecha en " + logica.getIntentos() + " intentos.",
                        "¡Has ganado!",
                        JOptionPane.INFORMATION_MESSAGE);
                pnlJuego.setVisible(false);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Entrada inválida", JOptionPane.ERROR_MESSAGE);
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

    private void limpiarFeedback() {
        lblFeedDia.setBackground(Color.LIGHT_GRAY);
        lblFeedMes.setBackground(Color.LIGHT_GRAY);
        lblFeedAno.setBackground(Color.LIGHT_GRAY);
        lblTextoDia.setText("");
        lblTextoMes.setText("");
        lblTextoAno.setText("");
    }
}