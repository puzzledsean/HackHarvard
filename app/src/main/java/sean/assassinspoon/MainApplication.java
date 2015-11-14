package sean.assassinspoon;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
/**
 * Created by Sean on 11/14/15.
 */
public class MainApplication extends Application {
    public void onCreate(){
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "Fk1hhiOgeY8WOEbM9C5xqfV7gjxhsvaBYMiGNJIu", "PXsn4N4gRcWwIBjvv8HJAZsGOEHng2zjyIyp0dqs");
        ParseInstallation.getCurrentInstallation().saveInBackground();


    }
}

