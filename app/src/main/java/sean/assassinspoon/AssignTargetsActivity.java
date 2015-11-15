package sean.assassinspoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;

/**
 * Created by Andy Shen on 11/14/2015.
 */
public class AssignTargetsActivity extends AppCompatActivity {

    //private String player;
    public ParseObject player = new ParseObject("Player");
    private String name;
    private String targetName;
    public int[] idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecting_target);

        final TextView targetText = (TextView) findViewById(R.id.targetName);


        Intent myIntent = getIntent();
        name = myIntent.getStringExtra("name");

        //declare query
        ParseQuery<ParseObject> nameListQuery = ParseQuery.getQuery("Game");
        nameListQuery.selectKeys(Arrays.asList("listOfPlayers"));
        //execute query
        nameListQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, com.parse.ParseException e) {
                if (object == null) {
                    //do nothing
                } else {
                    int temp = object.getJSONArray("listOfPlayers").length();
                    player.put("playerId", temp);
                }
            }
        });

        player.put("playerName", name);
        player.saveInBackground();
        try {
           matchMake();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        if(getTarget() == null){
            Log.d("getTarget return null", "null");
        }
        else {
            targetText.setText(getTarget());
        }
    }

    //Match make reads all the playerIds, and matches them with targetIds
    //reads playerIds from Parce
    //adds targetID & targetName into Parce
    //only need to call matchmake once
    private void matchMake() throws com.parse.ParseException {
        //declare query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Player");

        query.selectKeys(Arrays.asList("playerId"));
        //execute query
        List<ParseObject> results = query.find();

        idList = new int[results.size()];

        if (results.size() <= 3) {
            Log.d("Error:Need more players", " ");
        }
        else if (results.size() > 3) {
            for (int i = 0; i < results.size(); i++) {
                Log.d("the thing", "" + results.get(i).fetchIfNeeded().getInt("playerId"));
                idList[i] = results.get(i).fetchIfNeeded().getInt("playerId");
            }

            for (int j = 0; j < idList.length; j++) {
                final Random rand = new Random();
                int temp = rand.nextInt(idList.length) + 1;
                player.put("targetId", temp);
                targetName = player.getString("playerName");
                player.put("targetName", targetName);
                Log.d("targetId", " ");
            }

            player.saveInBackground();
        }
    }

    public String getTarget(){
        return player.getString("targetId");
    }
}
