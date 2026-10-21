package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class QDate extends JPanel {

    private JComboBox<Integer> cbDay;
    private JComboBox<Integer> cbMonth;
    private JTextField cbYear;

    public QDate() {
        cbDay = new JComboBox<>();

        for (int i = 1; i <= 31; i++) {
            cbDay.addItem(i);
        }

        cbMonth = new JComboBox<>();

        for (int i = 1; i <= 12; i++) {
            cbMonth.addItem(i);
        }

        cbYear = new JTextField(4);

        this.add(cbDay);
        this.add(new JLabel("/"));
        this.add(cbMonth);
        this.add(new JLabel("/"));
        this.add(cbYear);

        cbDay.setSelectedIndex(0);
        cbMonth.setSelectedIndex(0);
        cbYear.setText("1900");
    }

    private boolean esBisiesto(int year) {

        boolean esBisiesto = false;

        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
            esBisiesto = true;
        }

        return esBisiesto;
    }

    private int getDiasMes(int month, int year) {
        int diasMes = 0;
        switch (month) {
            case 1, 3, 5, 7, 8, 10, 12:
                diasMes = 31;
                break;
            case 4, 6, 9, 11:
                diasMes = 30;
                break;
            case 2:
                if (esBisiesto(year)) {
                    diasMes = 29;
                } else  {
                    diasMes = 28;
                }
                break;
        }

        return diasMes;
    }

    private void validarFecha(int day, int month, String textYear) throws QDateException {

        int year;

        if(textYear.trim().isEmpty()){
            throw new QDateException("¡Fecha incompleta! Falta el año.");
        } else {
            try {
                year = Integer.parseInt(textYear);
            } catch(NumberFormatException e) {
                throw new QDateException("¡Formato incorrecto! El año deben ser números.");
            }


        }

        if (day > getDiasMes(month, year)) {
            if (month==2 && day==29) {
                throw new QDateException("¡Fecha imposible! Introduce una fecha válida.");
            } else {
                throw new QDateException("¡Fecha incorrecta! Introduce una fecha válida.");
            }
        }

    }

    public void setDate(int year, int month, int day) {
        cbYear.setText(String.valueOf(year));
        cbMonth.setSelectedItem(month);
        cbDay.setSelectedItem(day);

    }

    public Date getDate() throws QDateException {

        int day = (Integer) cbDay.getSelectedItem();
        int month = (Integer) cbMonth.getSelectedItem();
        String yearText =  cbYear.getText();

        validarFecha(day, month, yearText);

        int year = Integer.parseInt(yearText);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day);

        return cal.getTime();

    }


}
