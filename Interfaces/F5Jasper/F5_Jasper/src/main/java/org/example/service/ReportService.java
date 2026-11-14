package org.example.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.example.model.Tarea;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class ReportService {
    public void mostrarInforme(List<Tarea> tareas) throws JRException {
        try (InputStream plantilla = getClass().getResourceAsStream("/informes/tareas.jrxml")) {
            if (plantilla == null) {
                throw new JRException("No se encontró la plantilla del informe.");
            }
            JasperReport report = JasperCompileManager.compileReport(plantilla);
            JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(),
                    new JRBeanCollectionDataSource(tareas));
            JasperViewer.viewReport(print, false);
        } catch (IOException e) {
            throw new JRException("No se pudo leer la plantilla del informe.", e);
        }
    }
}
