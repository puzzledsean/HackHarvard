package sean.assassinspoon;

/**
 * Created by huyle on 11/15/2015.
 */
import android.app.Activity;
import android.app.Application;

public class VibrationNotification extends Application {
    private Activity mCurrentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }
}