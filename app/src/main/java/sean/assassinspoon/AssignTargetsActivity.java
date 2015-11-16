package sean.assassinspoon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
public class AssignTargetsActivity extends Activity {

    //private String player;
    public ParseObject player = new ParseObject("Player");
    private String name;
    private String targetName;
    public int[] idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.target_information_page);

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
            targetName = matchMake();
            if (matchMake() != null){
                changePage(targetName);
            }
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

    }

    //Match make reads all the playerIds, and matches them with targetIds
    //reads playerIds from Parce
    //adds targetID & targetName into Parce
    //only need to call matchmake once
    private String matchMake() throws com.parse.ParseException {
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
            Random rand = new Random();
            int pp = rand.nextInt(results.size());
            return results.get(pp).getString("targetName");
        }
        return null;
    }

    public void changePage(String targetName) {
        targetName = this.targetName;
        Intent intent = new Intent(AssignTargetsActivity.this, TargetInfoPageActivity.class);
        intent.putExtra("name", targetName);
        startActivity(intent);
    }

}
