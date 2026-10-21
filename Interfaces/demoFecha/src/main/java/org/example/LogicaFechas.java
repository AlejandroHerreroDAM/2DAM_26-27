package org.example;

import java.util.Date;

public class LogicaFechas {

    public String calcularDiferencia(Date inicio, Date fin) throws Exception {

        if (inicio.after(fin)) {
            throw new WrongDateException("La fecha inicial es posterior a la final.");
        }

        long diferenciaMilis = fin.getTime() - inicio.getTime();
        long dias = diferenciaMilis / (1000 * 60 * 60 * 24);

        return "La diferencia es de " + dias + " días.";
    }
}