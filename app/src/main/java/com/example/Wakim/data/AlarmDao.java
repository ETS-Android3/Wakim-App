package com.example.Wakim.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * This interface defines the standard operations to be performed on an alarm model.
 */
@Dao
public interface AlarmDao {
    @Insert
    void insert(Alarm alarm);

    @Query("DELETE FROM alarm_table")
    void deleteAll();

    @Query("SELECT * FROM alarm_table ORDER BY hour, minute ASC")
    LiveData<List<Alarm>> getAlarms();

    @Update
    void update(Alarm alarm);

    @Delete
    void delete(Alarm alarm);
}
