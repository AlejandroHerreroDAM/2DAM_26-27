package dao.interfaces;

import dao.model.Mission;

public interface MissionRepository {
    Mission getByName(String name);

}
