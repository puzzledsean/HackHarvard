package sean.assassinspoon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by huyle on 11/14/2015.
 */
public class CreateGameActivity extends Activity {

    EditText nameOfGameTextBox;
    EditText mapBoundariesTextBox;
    EditText numOfPlayersTextBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        Button createGame = (Button) findViewById(R.id.createGameButton);
        nameOfGameTextBox = (EditText) findViewById(R.id.nameOfGame);
        mapBoundariesTextBox = (EditText) findViewById(R.id.mapBoundaries);
        numOfPlayersTextBox = (EditText) findViewById(R.id.numOfPlayers);

        createGame.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    String name = nameOfGameTextBox.getText().toString();
                    String mapBoundaries = mapBoundariesTextBox.getText().toString();
                    String numOfPlayers = numOfPlayersTextBox.getText().toString();
                    Log.v("Name of game", name);
                    Log.v("Map boundaries", mapBoundaries);
                    Log.v("Number of players", numOfPlayers);
                    createGame(name, mapBoundaries, numOfPlayers);
                }
            }
        );
    }

    private void createGame(final String name, final String mapBoundaries, String numOfPlayers){
        ParseObject newGame = new ParseObject("Game");
        newGame.put("name", name);
        // newGame.put("mapBoundaries", mapBoundaries);
        int num = 0;
        try{
            num = Integer.parseInt(numOfPlayers);
        }catch(NumberFormatException e){
            Log.d("Error", "Could not parse: " + e);
        }
        newGame.put("numOfPlayers", num);
        newGame.saveInBackground();
    }
}
