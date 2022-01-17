package com.example.Wakim.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.example.Wakim.service.AlarmService;
import com.example.Wakim.service.RescheduleAlarmsService;

import java.util.Calendar;

/**
 * This class handles the broadcast from the AlarmManager.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String WEDNESDAY = "WEDNESDAY";
    public static final String THURSDAY = "THURSDAY";
    public static final String FRIDAY = "FRIDAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String RECURRING = "RECURRING";

    /**
     * This method verifies that the broadcast received is not the same as the broadcast transmitted when
     * the device is powered on. If that is not the case, the broadcast refers to an alert,
     * and we then obtain data from the Intent to determine whether or not the alarm is reoccurring.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //intent action sent in broadcast when the device boots
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context,toastText,Toast.LENGTH_LONG).show();
            startRescheduleAlarmsService(context);
        }
        else{
            String toastText = String.format("Alarm Received");
            Toast.makeText(context,toastText,Toast.LENGTH_LONG).show();
            if(!intent.getBooleanExtra(RECURRING,false)){
                startAlarmService(context, intent);
            }
            if(alarmIsToday(intent)){
                startAlarmService(context, intent);
            }
        }
    }

    /**
     * This method checks if the alarm scheduled today
     * @param intent
     * @return
     */
    private boolean alarmIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch (today){
            case Calendar.MONDAY:
                if(intent.getBooleanExtra(MONDAY, false))
                    return true;
                return  false;
            case Calendar.TUESDAY:
                if(intent.getBooleanExtra(TUESDAY, false))
                    return true;
                return  false;
            case Calendar.WEDNESDAY:
                if(intent.getBooleanExtra(WEDNESDAY, false))
                    return true;
                return  false;
            case Calendar.THURSDAY:
                if(intent.getBooleanExtra(THURSDAY, false))
                    return true;
                return  false;
            case Calendar.FRIDAY:
                if(intent.getBooleanExtra(FRIDAY, false))
                    return true;
                return  false;
            case Calendar.SATURDAY:
                if(intent.getBooleanExtra(SATURDAY, false))
                    return true;
                return  false;
            case Calendar.SUNDAY:
                if(intent.getBooleanExtra(SUNDAY, false))
                    return true;
                return  false;
        }
        return false;

    }

    /**
     * This method calls the AlarmService class responsible for the notification
     * @param context
     * @param intent
     */
    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        }
        else {
            context.startService(intentService);
        }
    }

    /**
     * This method calls the RescheduleAlarmsService which reschedule the Alarm using it’s schedule method
     * @param context
     */
    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        }
        else {
            context.startService(intentService);
        }
    }
}
