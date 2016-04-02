package mobiledev.unb.ca.project365;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import java.util.Calendar;

/**
 * Created by James on 01/04/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private AlarmManager manager;
    private PendingIntent pending;

    @Override
    public void onReceive(Context context,Intent intent){
        Log.i("log","Received alarm notification broadcast. ");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Project 365")
                        .setContentText("Did you remember to take today's photo?")
                        .setAutoCancel((true));

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        int mId = 23423;
        mNotificationManager.notify(mId, mBuilder.build());

    }

    public void setAlarm(Context context, int hour, int min){

        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent openAlarmReceiverIntent = new Intent(context, AlarmReceiver.class);
        pending = PendingIntent.getBroadcast(context, 0, openAlarmReceiverIntent, 0);

        // Set the alarm to start at 3:20 pm and every day after that.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 00);

        // Prevent alarms from the previous day from firing immediately when selecting a new time.
        if(calendar.before(now)){
            calendar.add(Calendar.DATE, 1);
        }

        // Repeat alarm every 24 hours.
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pending);
    }


    public void cancelAlarm(Context context){
        // If the alarm has been set, cancel it.
        if (manager!= null) {
            manager.cancel(pending);
        }

        // Alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

}
