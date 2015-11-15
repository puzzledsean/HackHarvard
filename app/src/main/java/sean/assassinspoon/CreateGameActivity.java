package sean.assassinspoon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.ParseException;

import org.json.JSONArray;

/**
 * Created by huyle on 11/14/2015.
 */
public class CreateGameActivity extends AppCompatActivity {

    EditText nameOfGameTextBox;
    EditText numOfPlayersTextBox;
    private String objectId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        Button createGame = (Button) findViewById(R.id.createGameButton);
        createGame.setTextColor(Color.parseColor("#ffffff"));
        createGame.getBackground().setColorFilter(Color.parseColor("#E91E63"), PorterDuff.Mode.MULTIPLY);

        nameOfGameTextBox = (EditText) findViewById(R.id.nameOfGame);
        numOfPlayersTextBox = (EditText) findViewById(R.id.numOfPlayers);

        createGame.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = nameOfGameTextBox.getText().toString();
                        String numOfPlayers = numOfPlayersTextBox.getText().toString();
                        Log.v("Name of game", name);
                        Log.v("Number of players", numOfPlayers);
                        createGame(name, numOfPlayers);
                        Intent intent = new Intent(view.getContext(), CreateMapBoundaries.class);
                        intent.putExtra("name", name);
                        intent.putExtra("numOfPlayers", numOfPlayers);
                        startActivity(intent);
                    }
                }
        );
    }

    private void createGame(String name, String numOfPlayers){
        final ParseObject newGame = new ParseObject("Game");
        JSONArray emptyArray = new JSONArray();

        newGame.put("name", name);

        int num = 0;
        try{
            num = Integer.parseInt(numOfPlayers);
        }catch(NumberFormatException e){
            Log.d("Error", "Could not parse: " + e);
        }
        newGame.put("numOfPlayers", num);
        newGame.put("listOfPlayers", emptyArray);

        newGame.saveInBackground();
    }
}
