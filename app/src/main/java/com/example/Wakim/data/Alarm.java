package com.example.Wakim.data;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver;
import com.example.Wakim.createAlarm.DayUtil;

import java.util.Calendar;

import static com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver.DESCRIPTION;
import static com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver.FRIDAY;
import static com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver.MONDAY;
import static com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver.RECURRING;
import static com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver.SATURDAY;
import static com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver.SUNDAY;
import static com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver.THURSDAY;
import static com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver.TITLE;
import static com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver.TUESDAY;
import static com.example.Wakim.broadcastReceiver.AlarmBroadcastReceiver.WEDNESDAY;

@Entity(tableName = "alarm_table")
public class Alarm {
    @PrimaryKey
    @NonNull
    private int alarmId;

    private int hour, minute;
    private boolean started, recurring;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private String title, description;

    private long created;

    public Alarm(int alarmId, int hour, int minute, String title, String description, long created, boolean started, boolean recurring,
                 boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.started = started;

        this.recurring = recurring;

        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;

        this.title = title;
        this.description = description;

        this.created = created;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isStarted() {
        return started;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void schedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(RECURRING, recurring);
        intent.putExtra(MONDAY, monday);
        intent.putExtra(TUESDAY, tuesday);
        intent.putExtra(WEDNESDAY, wednesday);
        intent.putExtra(THURSDAY, thursday);
        intent.putExtra(FRIDAY, friday);
        intent.putExtra(SATURDAY, saturday);
        intent.putExtra(SUNDAY, sunday);
        intent.putExtra(TITLE, title);
        intent.putExtra(DESCRIPTION, description);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);

        //take the time and locale of the system
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //check if the alarm has already passed. If it does, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH + 1));
        }
        if (!recurring) {
            String toastText = null;
            try {
                toastText = String.format("One Time Alarm %s scheduled for %s at %02d:%02d", title, DayUtil.toStringDay(calendar.get(Calendar.DAY_OF_WEEK)), hour, minute, alarmId);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmPendingIntent);
        }
        else{
            String toastText = String.format("Recurring Alarm %s scheduled for %s at %02d:%02d", title, getRecurringDaysString(), hour, minute, alarmId);
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
        }

    }

    private String getRecurringDaysString() {
        String days = "";
        if(monday){
            days += "Mon ";
        }
        if(tuesday){
            days += "Tue ";
        }
        if(wednesday){
            days += "Wed ";
        }
        if(thursday){
            days += "Thurs ";
        }
        if(friday){
            days += "Fri ";
        }
        if(saturday){
            days += "Sat ";
        }
        if(sunday){
            days += "Sun ";
        }
        return days;
    }
}
