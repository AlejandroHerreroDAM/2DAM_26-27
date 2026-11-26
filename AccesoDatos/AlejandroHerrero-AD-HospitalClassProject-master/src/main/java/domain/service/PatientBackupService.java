package domain.service;

import dao.repositories.JDBCPatientRepository;
import domain.exceptions.BackupNotFoundException;
import domain.exceptions.DatabaseError;
import domain.model.BackupRow;
import domain.model.BackupValue;
import domain.model.PatientBackup;
import jakarta.inject.Inject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PatientBackupService {
    private static final Path BACKUP_DIRECTORY = Paths.get("backups");
    private static final Path BACKUP_FILE = BACKUP_DIRECTORY.resolve("deleted-patients.xml");

    private final JDBCPatientRepository patientRepository;

    @Inject
    public PatientBackupService(JDBCPatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public void backup(int patientId) {
        try {
            Files.createDirectories(BACKUP_DIRECTORY);
            Document document = loadOrCreateDocument();
            removePatientBackup(document, patientId);

            Element patientBackup = document.createElement("patientBackup");
            patientBackup.setAttribute("patientId", Integer.toString(patientId));
            document.getDocumentElement().appendChild(patientBackup);
            appendBackup(document, patientBackup, patientRepository.createBackup(patientId));

            saveDocument(document);
            System.out.println("Patient backup created at: " + BACKUP_FILE.toAbsolutePath());
        } catch (DatabaseError e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseError("Could not write patient backup: " + e.getMessage());
        }
    }

    public void restore(int patientId) {
        if (!Files.exists(BACKUP_FILE) && !Files.exists(BACKUP_DIRECTORY)) {
            throw new BackupNotFoundException(patientId);
        }

        try {
            Document document = loadOrCreateDocument();
            Element patientBackup = findPatientBackup(document, patientId);
            if (patientBackup == null) {
                throw new BackupNotFoundException(patientId);
            }

            PatientBackup backup = readBackup(patientBackup);
            patientRepository.restore(backup);

            document.getDocumentElement().removeChild(patientBackup);
            saveDocument(document);
        } catch (BackupNotFoundException e) {
            throw e;
        } catch (DatabaseError e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseError("Could not restore patient backup: " + e.getMessage());
        }
    }

    private Document loadOrCreateDocument() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        if (Files.exists(BACKUP_FILE)) {
            Document document = factory.newDocumentBuilder().parse(BACKUP_FILE.toFile());
            if (!"deletedPatients".equals(document.getDocumentElement().getTagName())) {
                throw new DatabaseError("Invalid patient backup XML root");
            }
            return document;
        }

        Document document = factory.newDocumentBuilder().newDocument();
        Element root = document.createElement("deletedPatients");
        document.appendChild(root);
        importLegacyBackups(document, root, factory);
        return document;
    }

    private void importLegacyBackups(Document document, Element root,
                                     DocumentBuilderFactory factory) throws Exception {
        try (Stream<Path> files = Files.list(BACKUP_DIRECTORY)) {
            files.filter(path -> path.getFileName().toString().matches("patient-\\d+\\.xml"))
                    .forEach(path -> {
                        try {
                            Document legacy = factory.newDocumentBuilder().parse(path.toFile());
                            Element legacyRoot = legacy.getDocumentElement();
                            String patientId = getPatientId(legacyRoot);
                            if (patientId != null) {
                                Node imported = document.importNode(legacyRoot, true);
                                ((Element) imported).setAttribute("patientId", patientId);
                                root.appendChild(imported);
                            }
                        } catch (Exception e) {
                            throw new DatabaseError("Could not migrate patient backup: " + e.getMessage());
                        }
                    });
        }
    }

    private String getPatientId(Element backup) {
        NodeList tables = backup.getElementsByTagName("table");
        for (int i = 0; i < tables.getLength(); i++) {
            Element table = (Element) tables.item(i);
            if (!"patients".equals(table.getAttribute("name"))) {
                continue;
            }
            NodeList fields = table.getElementsByTagName("field");
            for (int j = 0; j < fields.getLength(); j++) {
                Element field = (Element) fields.item(j);
                if ("patient_id".equals(field.getAttribute("name"))) {
                    return field.getTextContent();
                }
            }
        }
        return null;
    }

    private Element findPatientBackup(Document document, int patientId) {
        NodeList backups = document.getDocumentElement().getElementsByTagName("patientBackup");
        for (int i = 0; i < backups.getLength(); i++) {
            Element backup = (Element) backups.item(i);
            if (Integer.toString(patientId).equals(backup.getAttribute("patientId"))) {
                return backup;
            }
        }
        return null;
    }

    private void removePatientBackup(Document document, int patientId) {
        Element patientBackup = findPatientBackup(document, patientId);
        if (patientBackup != null) {
            document.getDocumentElement().removeChild(patientBackup);
        }
    }

    private void appendBackup(Document document, Element patientBackup, PatientBackup backup) {
        for (Map.Entry<String, List<BackupRow>> table : backup.getTables().entrySet()) {
            Element tableElement = document.createElement("table");
            tableElement.setAttribute("name", table.getKey());
            patientBackup.appendChild(tableElement);
            for (BackupRow row : table.getValue()) {
                Element rowElement = document.createElement("row");
                tableElement.appendChild(rowElement);
                for (Map.Entry<String, BackupValue> value : row.getValues().entrySet()) {
                    Element field = document.createElement("field");
                    field.setAttribute("name", value.getKey());
                    field.setAttribute("type", value.getValue().getType());
                    if (value.getValue().getValue() == null) {
                        field.setAttribute("null", "true");
                    } else {
                        field.setTextContent(value.getValue().getValue());
                    }
                    rowElement.appendChild(field);
                }
            }
        }
    }

    private PatientBackup readBackup(Element patientBackup) {
        PatientBackup backup = new PatientBackup();
        NodeList tables = patientBackup.getElementsByTagName("table");
        for (int i = 0; i < tables.getLength(); i++) {
            Element tableElement = (Element) tables.item(i);
            List<BackupRow> rows = new ArrayList<>();
            NodeList rowNodes = tableElement.getElementsByTagName("row");
            for (int j = 0; j < rowNodes.getLength(); j++) {
                Element rowElement = (Element) rowNodes.item(j);
                BackupRow row = new BackupRow();
                NodeList fields = rowElement.getElementsByTagName("field");
                for (int k = 0; k < fields.getLength(); k++) {
                    Element field = (Element) fields.item(k);
                    String value = "true".equals(field.getAttribute("null"))
                            ? null : field.getTextContent();
                    row.put(field.getAttribute("name"),
                            new BackupValue(field.getAttribute("type"), value));
                }
                rows.add(row);
            }
            backup.getTables().put(tableElement.getAttribute("name"), rows);
        }
        return backup;
    }

    private void saveDocument(Document document) throws Exception {
        var transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(BACKUP_FILE.toFile()));
        try (Stream<Path> files = Files.list(BACKUP_DIRECTORY)) {
            files.filter(path -> path.getFileName().toString().matches("patient-\\d+\\.xml"))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (Exception e) {
                            throw new DatabaseError("Could not remove legacy patient backup: " + e.getMessage());
                        }
                    });
        }
    }
}
