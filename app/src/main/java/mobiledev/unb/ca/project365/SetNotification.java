package mobiledev.unb.ca.project365;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Created by lmeng on 02/04/16.
 */
public class SetNotification extends AppCompatActivity {

    private TimePicker timePicker;
    private Button mSetNotificationBtn;
    private Button mRemoveNotificationBtn;

    private int newHour;
    private int newMin;
    private AlarmReceiver alarm = new AlarmReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_notification_time);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        ActionBar ab = getSupportActionBar();

        timePicker = (TimePicker) findViewById(R.id.time_picker);

        // Display the previously set notification time (if it exists)

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int prevHour = sharedPref.getInt(getString(R.string.saved_notification_time_hour), 0); //default value of 0
        int prevMin = sharedPref.getInt(getString(R.string.saved_notification_time_min), 0); //default value of 0

        timePicker.setCurrentHour(prevHour);
        timePicker.setCurrentMinute(prevMin);

        mSetNotificationBtn = (Button) findViewById(R.id.btnSetNotification);
        mSetNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the new time values from the user's input
                newHour = timePicker.getCurrentHour();
                newMin = timePicker.getCurrentMinute();

                updateAlarmPreferences(newHour, newMin);
                alarm.setAlarm(getApplicationContext(), newHour, newMin);

                // Return to home screen
                Intent intent = new Intent(SetNotification.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mRemoveNotificationBtn = (Button) findViewById(R.id.btnRemoveNotificaiton);
        mRemoveNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.cancelAlarm(getApplicationContext());

                // Return to home screen
                Intent intent = new Intent(SetNotification.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateAlarmPreferences(int hour, int min) {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_notification_time_hour), hour);
        editor.putInt(getString(R.string.saved_notification_time_min), min);
        editor.commit();
    }
}
