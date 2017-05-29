package nimbl3.com.mixpanledemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Replace project token with your token
    String projectToken = "56dd6112da36a96fdeb43ff9790d9924";
    TextView textView;
    MixpanelAPI mixpanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize textview
        textView = (TextView) findViewById(R.id.textview);

        //  Add project token in the getInstance() method.
        mixpanel = MixpanelAPI.getInstance(this, projectToken);


        /**
         * Associate all future events sent from
         * the library with the distinct_id 132
         */
        mixpanel.identify("132");

        // Call trackEvent method
        trackEvent();

        // start the timer for the event "display data"
        mixpanel.timeEvent("display data");

        if(trackTiming()){

            mixpanel.track("display data");
        }

        // Calling sentEventWithProperties() method.
        sentEventWithProperties();

        // Calling revenueTrack() method.
        revenueTrack();

        // Calling setNumericProperties() method
        setNumericProperties();

    }

    /**
     *  track event by calling MixPanelAPI.track() method with event name and properties
     */
    public void trackEvent(){

        try {

            JSONObject props = new JSONObject();
            props.put("Gender", "Female");
            props.put("Plan", "Premium");

            // track event by calling track() method
            mixpanel.track("Plan Selected", props);

        } catch (JSONException e) {

            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

    }

    /**
     *  Register properties as super properties.
     *  Send Properties with event.
     */
    public void sentEventWithProperties(){

        try{

            // Send a "User Type: Paid" property will be sent
            // with all future track calls.
            JSONObject props = new JSONObject();
            props.put("User Type", "Paid");
            mixpanel.registerSuperProperties(props);
            mixpanel.track("setImportant");

        } catch (Exception e){

        }
    }

    /**
     * Set text on TextView
     * @return
     */
    public boolean trackTiming() {

        textView.setText("name: Ashish \n email: ashishchauhan.gbu@gmail.com");
        return true;

    }

    /**
     *  Using getPeople.trackCharge() method, we can track revenue for individual user
     */
    public void revenueTrack(){

        /**
         * Make getPeople() identify has been called before making revenue updates
         */
        mixpanel.getPeople().identify("132");

        // Tracks $100 in revenue for user 1372
        mixpanel.getPeople().trackCharge(100, null);

        // Refund this user 50 dollars
        mixpanel.getPeople().trackCharge(-50, null);

        try {

            /**
             * Tracks $25 in revenue for user 132 on the 2nd of january
             */
            JSONObject properties = new JSONObject();
            properties.put("$time", "2012-01-02T00:00:00");
            mixpanel.getPeople().trackCharge(25, properties);

        } catch (Exception e){

            e.printStackTrace();
        }
    }

    /**
     *  Set numeric properties.
     */
    public void setNumericProperties(){

        /**
         * Add 500 to the current value of "points earned" in Mixpanel
         */
        mixpanel.getPeople().increment("points earned", 1000);

        // Pass a Map to increment multiple properties
        Map<String, Integer> properties =
                new HashMap<String, Integer>();

        properties.put("dollars spent", 34);

        // Subtract by passing a negative value
        properties.put("credits remaining", -68);

        mixpanel.getPeople().increment(properties);
    }

    /**
     *  Called when app closed.
     *  Calling flush() method of MixPanelAPI class to send all recorded event.
     */
    @Override
    protected void onDestroy() {
        mixpanel.flush();
        super.onDestroy();
    }

}
