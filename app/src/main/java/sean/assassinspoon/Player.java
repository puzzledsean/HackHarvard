package sean.assassinspoon;

import com.parse.ParseObject;

import java.io.File;

/**
 * Created by Andy Shen on 11/14/2015.
 */
public class Player {

    public ParseObject player = new ParseObject("Player");
    private int playerId, targetId, longitude, latitude;
    private String playerName, targetName;
    private File playerPicture;

    Player(int playerId, int longitude, int latitude, String playerName, File playerPicture) {
        this.playerId = playerId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.playerName = playerName;
        this.playerPicture = playerPicture;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getTargetId() {
        return targetId;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getTargetName() {
        return player.getString("targetName");
    }

    public File getPlayerPicture() {
        return playerPicture;
    }

}

