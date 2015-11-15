package sean.assassinspoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.reflect.Array;
import java.text.ParseException;
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
    private int[] idList;
    private String targetName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecting_target);

        Intent myIntent = getIntent();
        name = myIntent.getStringExtra("name");

        //player = new ParseObject("Player");
        //ParseFile playerPicture = new ParseFile("playerPicture");

        player.put("playerName", name);

        player.increment("playerId");
        int temp = (int) player.get("playerId");

        player.saveInBackground();

        matchMake();
        getTarget();
    }

    //Match make reads all the playerIds, and matches them with targetIds
    //reads playerIds from Parce
    //adds targetID & targetName into Parce
    //only need to call matchmake once
    private void matchMake() {
        //declare query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Player");

        query.include("playerId");
        //execute query
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> playerParse, com.parse.ParseException e) {
                for (ParseObject Id : playerParse) {
                    for (int i = 0; i < playerParse.size(); i++) {
                        idList[i] = Id.getInt("playerId");
                    }
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


        });
    }

    public String getTarget() {
        return player.getString("targetId");
    }
}
