package dao.repositories.XML;

import common.Configuration;
import common.Constants;
import dao.model.CompletedMission;
import dao.model.XML.XMLCompletedMission;
import dao.model.XML.XMLCompletedMissionsModel;
import dao.model.XML.XMLPlayer;
import domain.exceptions.DatabaseError;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLCompletedMissionsRepository {

    private final Configuration config;

    @Inject
    public XMLCompletedMissionsRepository(Configuration config) {
        this.config = config;
    }

    // Lee el XML existente y lo convierte a objetos Java (unmarshal)
    private XMLCompletedMissionsModel load() {
        Path xmlFile = Paths.get(config.getProperty("xmlCompletedMissionsPath"));
        try (InputStream in = Files.newInputStream(xmlFile)) {
            JAXBContext context = JAXBContext.newInstance(XMLCompletedMissionsModel.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (XMLCompletedMissionsModel) unmarshaller.unmarshal(in);
        } catch (IOException | JAXBException e) {
            Logger.getLogger(XMLCompletedMissionsRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseError(Constants.XML_ERROR);
        }
    }

    // Vuelca el modelo actualizado al fichero XML formateado (marshal)
    private void write(XMLCompletedMissionsModel model) {
        Path xmlFile = Paths.get(config.getProperty("xmlCompletedMissionsPath"));
        try (OutputStream out = Files.newOutputStream(xmlFile)) {
            JAXBContext context = JAXBContext.newInstance(XMLCompletedMissionsModel.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(model, out);
        } catch (IOException | JAXBException e) {
            Logger.getLogger(XMLCompletedMissionsRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseError(Constants.XML_ERROR);
        }
    }

    public void save(int playerId, List<CompletedMission> missions) {
        // 1. Carga los jugadores que ya estaban en el XML para no perderlos
        XMLCompletedMissionsModel model = load();
        // 2. Evita duplicados si ya existía este jugador en el archivo
        model.getPlayers().removeIf(p -> p.getId() == playerId);

        // 3. Transforma los objetos de BD al modelo limpio del XML
        List<XMLCompletedMission> xmlMissions = missions.stream()
                .map(m -> new XMLCompletedMission(m.getMissionId(), m.getCharacterName()))
                .toList();

        // 4. Añade el nuevo jugador con sus misiones y guarda el XML
        model.getPlayers().add(new XMLPlayer(playerId, xmlMissions));
        write(model);
    }
}