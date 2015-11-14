package sean.assassinspoon;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Sean on 11/14/15.
 */
public class InfoActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        TextView info = (TextView) findViewById(R.id.info);
        TextView infoHeader = (TextView)  findViewById(R.id.infoHeader);
        Typeface proxima = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.otf");
        info.setTypeface(proxima);
        infoHeader.setTypeface(proxima);



    }
}
