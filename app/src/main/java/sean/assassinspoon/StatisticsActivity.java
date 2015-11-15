package sean.assassinspoon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

import java.util.Arrays;

public class StatisticsActivity extends AppCompatActivity {
    private final static String TAG = StatisticsActivity.class.getSimpleName();
    private static String appid = "8R33P2-5QJYVY3HK4";

    private TextView txtKills;
    private TextView txtTimeAlive;
    private TextView txtAvgKillTime;

    // dummy variables
    private final int numOfKills = 7;
    private final double timeAlive = 108; // survival time in hours
    private final double[] timesBetweenKills = {1, 1.5, 0.75, 21.75, 24.193, 6.3, 35.77}; // times between successive kills
    //private final double[] dailyActivity = {3, 1, 2, 3}; // number of kills made per day

    WAEngine engine;
    WAQuery query;
    WAQueryResult queryResult;
    String inputString;
    String mean = "Null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtKills = (TextView) findViewById(R.id.killNumber);
        txtKills.setText(String.valueOf(numOfKills));

        txtTimeAlive = (TextView) findViewById(R.id.hoursAliveNumber);
        txtTimeAlive.setText(String.valueOf(timeAlive + " hrs."));

        // Find mean time between kills
        inputString = Arrays.toString(timesBetweenKills);
        Log.i(TAG, "Input String" + inputString);
        new getStats(inputString).execute();
    }


    private class getStats extends AsyncTask<WAQueryResult, Void, WAQueryResult> {
        private String input;
        getStats(String s) {
            input = s;
        }

        @Override
        protected WAQueryResult doInBackground(WAQueryResult... urls) {
            engine = new WAEngine();
            engine.setAppID(appid);
            engine.addFormat("plaintext");

            query = engine.createQuery();
            query.setInput(input);
            query.addIncludePodID("Statistics");

            try {
                // Print out the URL we are about to send:
                Log.i(TAG, "Query URL: " + engine.toURL(query));

                // This sends the URL to the Wolfram|Alpha server, gets the XML result
                // and parses it into an object hierarchy held by the WAQueryResult object.
                queryResult = engine.performQuery(query);
            } catch (WAException e) {
                e.printStackTrace();
            }

            return queryResult;
        }

        @Override
        protected void onPostExecute(WAQueryResult response) {
            // Log the execution result of the query
            if (queryResult.isError()) {
                Log.i(TAG, "Query error");
                Log.i(TAG, "  error code: " + queryResult.getErrorCode());
                Log.i(TAG, "  error message: " + queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {
                Log.i(TAG, "Query was not understood; no results available.");
            } else {
                // Got a result.
                Log.i(TAG, "Successful query. Pods follow:\n");
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        Log.i(TAG, pod.getTitle());
                        Log.i(TAG, "------------");
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                                if (element instanceof WAPlainText) {
                                    Log.i(TAG, ((WAPlainText) element).getText());
                                }
                            }
                        }
                    }
                }

                WAPod stats = queryResult.getPods()[0];
                WASubpod stat = stats.getSubpods()[0];
                for (Object element : stat.getContents()) {
                    if (element instanceof WAPlainText) {
                        mean = ((WAPlainText) element).getText();
                        txtAvgKillTime = (TextView) findViewById(R.id.avgKillTimeNumber);
                        txtAvgKillTime.setText(mean.substring(7, 12) + " hrs."); // removes the mean | part from the value
                        break;
                    }
                }
            }
        }
    }
}
