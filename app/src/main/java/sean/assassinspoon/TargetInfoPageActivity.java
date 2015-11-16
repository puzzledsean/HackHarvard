package sean.assassinspoon;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Andy Shen on 11/14/2015.
 */
public class TargetInfoPageActivity extends AppCompatActivity{

    private String targetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_information_page);

        Intent myIntent = getIntent();
        targetName = myIntent.getStringExtra("name");

        Button myButton = (Button) findViewById(R.id.toMapButton);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TargetInfoPageActivity.this, MapActivity.class);
                //intent.putExtra("listOfPlayers", name);
                startActivity(intent);
            }
        });

        TextView targetText = (TextView) findViewById(R.id.targetName);
        //targetText.setText("Your Target");

    }


}
