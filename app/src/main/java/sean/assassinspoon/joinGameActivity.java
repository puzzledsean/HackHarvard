package sean.assassinspoon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;

/**
 * Created by Sean on 11/14/15.
 */
public class joinGameActivity extends AppCompatActivity{
    private EditText editText;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_game_activity);


        editText = (EditText) findViewById(R.id.nameField);
        Button startButton = (Button) findViewById(R.id.start_after_join);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editText.getText().toString();
                checkNameField(name);

            }
        });

    }

    private void checkNameField(final String name){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");

        // Retrieve the object by id
        query.getInBackground("i4TXC7XjKx", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject game, com.parse.ParseException e) {
                Log.d("name", game.getString("name"));
                game.put("name", name);
                game.saveInBackground();

            }
        });
    }
}
