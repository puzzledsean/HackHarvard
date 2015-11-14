package sean.assassinspoon;

/**
 * Created by Sean on 11/14/15.
 */

public class Game {
    private String name;
    private int[] mapBoundaries;
    private int numOfPlayers;
    private int[] listOfPlayers;

    Game(String passedName, int[] passedBoundaries, int passedNumPlayers, int[] passedListOfPlayers) {
        name = passedName;
        mapBoundaries = passedBoundaries;
        numOfPlayers = passedNumPlayers;
        listOfPlayers = passedListOfPlayers;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int[] getMapBoundaries() {
        return mapBoundaries;
    }
    public void setMapBoundaries(int[] mapBoundaries) {
        for(int i = 0; i < mapBoundaries.length; i++){
            this.mapBoundaries[i] = mapBoundaries[i];
        }
    }
    public int getNumOfPlayers() {
        return numOfPlayers;
    }
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }
    public int[] getListOfPlayers() {
        return listOfPlayers;
    }
    public void setListOfPlayers(int[] listOfPlayers) {
        for(int i = 0; i < listOfPlayers.length; i++){
            this.listOfPlayers[i] = listOfPlayers[i];
        }
    }
}
