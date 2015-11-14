package sean.assassinspoon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Sean on 11/14/15.
 */
public class CreateOrJoinActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_join);


        FloatingActionButton join = (FloatingActionButton) findViewById(R.id.joinBtn);
        join.setImageDrawable(getDrawable(R.drawable.common_signin_btn_icon_focus_light));
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrJoinActivity.this, joinGameActivity.class);
                startActivity(intent);

            }
        });

        FloatingActionButton map = (FloatingActionButton) findViewById(R.id.mapBtn);
        map.setImageDrawable(getDrawable(R.drawable.common_signin_btn_icon_dark));
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrJoinActivity.this, MapActivity.class);
                startActivity(intent);

            }
        });


        FloatingActionButton create = (FloatingActionButton) findViewById(R.id.createBtn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateGameActivity.class);
                startActivity(intent);
            }
        });
    }
}
