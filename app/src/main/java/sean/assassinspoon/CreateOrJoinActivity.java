package sean.assassinspoon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.notifications.MessageFlags;
import com.microsoft.band.notifications.VibrationType;
import com.microsoft.band.tiles.BandIcon;
import com.microsoft.band.tiles.BandTile;
import com.microsoft.band.tiles.BandTileManager;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sean on 11/14/15.
 */
public class CreateOrJoinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_join);

        FloatingActionButton join = (FloatingActionButton) findViewById(R.id.joinBtn);
        join.setImageDrawable(getDrawable(R.drawable.ic_directions_walk_white_24dp));
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrJoinActivity.this, joinGameActivity.class);
                startActivity(intent);

            }
        });

        FloatingActionButton map = (FloatingActionButton) findViewById(R.id.mapBtn);
        map.setImageDrawable(getDrawable(R.drawable.ic_map_white_24dp));
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOrJoinActivity.this, MapActivity.class);
                startActivity(intent);

            }
        });


        FloatingActionButton create = (FloatingActionButton) findViewById(R.id.createBtn);
        create.setImageDrawable(getDrawable(R.drawable.ic_group_add_white_24dp));

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateGameActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton info = (FloatingActionButton) findViewById(R.id.infoBtn);
        info.setImageDrawable(getDrawable(R.drawable.ic_info_outline_white_24dp));

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InfoActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton stat = (FloatingActionButton) findViewById(R.id.statBtn);
        stat.setImageDrawable(getDrawable(R.drawable.ic_face_white_24dp));

        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StatisticsActivity.class);
                startActivity(intent);
            }
        });

        TextView createText = (TextView) findViewById(R.id.createText);
        TextView joinText = (TextView) findViewById(R.id.joinText);
        TextView instructionText = (TextView) findViewById(R.id.instructionText);
        Typeface proxima = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.otf");
        createText.setTypeface(proxima);
        joinText.setTypeface(proxima);
        instructionText.setTypeface(proxima);
    }
}
