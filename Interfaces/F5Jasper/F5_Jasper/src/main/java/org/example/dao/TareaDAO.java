package org.example.dao;

import org.example.model.Tarea;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO {
    private final Path fichero = Path.of(System.getProperty("user.home"), ".gestor-tareas.xml");

    public List<Tarea> listar() {
        if (!Files.exists(fichero)) return new ArrayList<>();

        try {
            var factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            Document document = factory.newDocumentBuilder().parse(fichero.toFile());
            var tareas = new ArrayList<Tarea>();
            var nodos = document.getDocumentElement().getElementsByTagName("tarea");

            for (int i = 0; i < nodos.getLength(); i++) {
                var elemento = (Element) nodos.item(i);
                tareas.add(new Tarea(
                        valor(elemento, "nombre"),
                        valor(elemento, "descripcion"),
                        Integer.parseInt(valor(elemento, "porcentaje")),
                        valor(elemento, "responsable"),
                        valor(elemento, "ubicacion")));
            }
            return tareas;
        } catch (IOException | SAXException | ParserConfigurationException | NumberFormatException e) {
            System.err.println("No se pudieron leer las tareas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void guardar(List<Tarea> nuevasTareas) {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().newDocument();
            Element root = document.createElement("tareas");
            document.appendChild(root);

            for (Tarea tarea : nuevasTareas) {
                Element elemento = document.createElement("tarea");
                agregar(document, elemento, "nombre", tarea.getNombre());
                agregar(document, elemento, "descripcion", tarea.getDescripcion());
                agregar(document, elemento, "porcentaje", String.valueOf(tarea.getPorcentaje()));
                agregar(document, elemento, "responsable", tarea.getResponsable());
                agregar(document, elemento, "ubicacion", tarea.getUbicacion());
                root.appendChild(elemento);
            }

            var transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(fichero.toFile()));
        } catch (ParserConfigurationException | TransformerException e) {
            throw new IllegalStateException("No se pudieron guardar las tareas en XML.", e);
        }
    }

    private String valor(Element elemento, String nombre) {
        var nodos = elemento.getElementsByTagName(nombre);
        return nodos.getLength() == 0 ? "" : nodos.item(0).getTextContent();
    }

    private void agregar(Document document, Element padre, String nombre, String valor) {
        Element elemento = document.createElement(nombre);
        elemento.setTextContent(valor == null ? "" : valor);
        padre.appendChild(elemento);
    }
}
