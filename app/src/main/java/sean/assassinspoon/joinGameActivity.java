package sean.assassinspoon;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sean on 11/14/15.
 */
public class joinGameActivity extends AppCompatActivity{
    private EditText editText;
    private String name;
    private String game;
    private boolean joinedGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_game_activity);


        final EditText gameField = (EditText) findViewById(R.id.joinGame);
        final EditText nameField = (EditText) findViewById(R.id.joinName);

        Button startButton = (Button) findViewById(R.id.start_after_join);
        startButton.setTextColor(Color.parseColor("#ffffff"));
        startButton.getBackground().setColorFilter(Color.parseColor("#E91E63"), PorterDuff.Mode.MULTIPLY);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game = gameField.getText().toString();
                name = nameField.getText().toString();
                try {
                    checkFields(game, name);
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }
                if(!joinedGame){
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    Snackbar.make(v, "A game with that name doesn't exist", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    Intent intent = new Intent(joinGameActivity.this, TargetInfoPageActivity.class);
                    intent.putExtra("name", name);
                    //intent.putExtra("listOfPlayers", name);
                    startActivity(intent);
                }

            }
        });

    }

    private void checkFields(final String gameName, final String name) throws com.parse.ParseException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");

        query.selectKeys(Arrays.asList("name"));
        List<ParseObject> results = query.find();
        String gameNameCheck;
        String objectId;
        //see if game of that gameName has already been created
        for(int i = 0; i < results.size(); i++) {
            gameNameCheck = results.get(i).get("name").toString();
//            Log.d("game name check", gameNameCheck);
            if(gameNameCheck.equals(gameName)){ //found objectID based on game name
                joinedGame = true;
                ParseObject gameObject = results.get(i);
                objectId = gameObject.fetchIfNeeded().getObjectId();
                Log.d("objectID", objectId);

                // store the data to that object ID
                // basically just add to list of players
                gameObject.put("name", gameName);

                JSONArray temp = gameObject.fetchIfNeeded().getJSONArray("listOfPlayers");
                Log.d("temp", temp.toString());
                Log.d("player name", name);
                temp.put(name);
                gameObject.put("listOfPlayers", temp);
                Log.v("temp ", String.valueOf(temp));
//                gameObject.increment("numOfPlayers");
                gameObject.saveInBackground();


            }
        }


    }
}
