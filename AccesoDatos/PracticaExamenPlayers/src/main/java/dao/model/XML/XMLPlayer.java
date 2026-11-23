package dao.model.XML;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLPlayer {

    @XmlAttribute
    private int id;

    @XmlElement(name="cMission")
    private List<XMLCompletedMission> cMissions = new ArrayList<>();
}
