package sean.assassinspoon;

import java.io.File;

/**
 * Created by Andy Shen on 11/14/2015.
 */
public class Player {
    private int playerId, targetId, longitude, latitude;
    private String playerName, targetName;
    private File playerPicture;

    Player(int playerId, int targetId, int longitude, int latitude, String playerName, String targetName, File playerPicture) {
        this.playerId = playerId;
        this.targetId = targetId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.playerName = playerName;
        this.targetName = targetName;
        this.playerPicture = playerPicture;
    }

    public int getPlayerId() {
        return playerId;
    }
    public int getTargetId() {
        return targetId;
    }
    public int getLongitude(){
        return longitude;
    }
    public int getLatitude(){
        return latitude;
    }
    public String getPlayerName(){
        return playerName;
    }
    public String getTargetName(){
        return targetName;
    }
    public File getPlayerPicture(){
        return playerPicture;
    }



}

