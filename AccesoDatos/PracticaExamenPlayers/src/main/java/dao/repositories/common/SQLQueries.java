package dao.repositories.common;

public class SQLQueries {


    //player queries
    public static final String INSERT_PLAYER_WITH_AUTO_ID = "INSERT INTO players (name, email, tokens) VALUES (?, ?, ?)";
    public static final String UPDATE_PLAYER = "UPDATE players SET name = ?, email = ?, tokens = ? WHERE player_id = ?";
    public static final String DELETE_PATIENT = "DELETE FROM players WHERE player_id = ?";
    public static final String SELECT_PLAYER_BY_ID = "SELECT * FROM Players WHERE Player_ID = ?";
    public static final String SELECT_PLAYER_BY_NAME = "SELECT * FROM Players WHERE Name = ?";
    public static final String UPDATE_PLAYER_TOKENS = "UPDATE Players SET Tokens = ? WHERE Player_ID = ?";

    //character queries
    public static final String INSERT_CHARACTER = "INSERT INTO characters (player_id, name, type, level) VALUES (?, ?, ?, ?)";
    public static final String DELETE_CHARACTER_BY_ID = "DELETE FROM Characters WHERE Character_ID = ?";
    public static final String SELECT_CHARACTER_BY_NAME = "SELECT * FROM Characters WHERE Name = ?";

    //completedmission queries
    public static final String INSERT_COMPLETED_MISSION = "INSERT INTO completed_missions (mission_id, character_id, completion_date) VALUES (?, ?, ?)";
    public static final String DELETE_COMPLETED_MISSIONS_ON = "DELETE FROM completed_missions WHERE character_id = ?";
    public static final String GET_COMPLETED_MISSIONS_BY_PLAYER_ID =
            "SELECT cm.Mission_ID, cm.Character_ID, cm.Completion_Date, c.Name AS Character_Name " +
                    "FROM completed_missions cm" +
                    " JOIN characters c ON cm.character_id = c.character_id" +
                    " JOIN players p ON c.player_id = p.player_id" +
                    " WHERE p.player_id = ?";

    public static final String GET_COMPLETED_MISSIONS_BY_CHARACTER_NAME
            =
            "SELECT cm.Mission_ID, cm.Character_ID, cm.Completion_Date, c.Name AS Character_Name " +
                    "FROM completed_missions cm" +
                    " JOIN characters c ON cm.character_id = c.character_id" +
                    " WHERE c.name = ?";


    //mission queries
    public static final String SELECT_MISSION_BY_NAME = "SELECT * FROM Missions WHERE Name = ?";
}
