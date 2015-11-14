package sean.assassinspoon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by huyle on 11/14/2015.
 */
public class CreateGameActivity extends Activity {

    EditText nameOfGame;
    EditText mapBoundries;
    EditText numOfPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        Button createGame = (Button) findViewById(R.id.createGameButton);
        nameOfGame = (EditText) findViewById(R.id.nameOfGame);
        mapBoundries = (EditText) findViewById(R.id.mapBoundries);
        numOfPlayers = (EditText) findViewById(R.id.numOfPlayers);

        createGame.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    Log.v("Name of game", nameOfGame.getText().toString());
                    Log.v("Map boundries", mapBoundries.getText().toString());
                    Log.v("Number of players", numOfPlayers.getText().toString());
                }
            }
        );
    }
}
