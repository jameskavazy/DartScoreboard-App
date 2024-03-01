package com.example.dartscoreboard.Reminders;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ReminderViewModel extends AndroidViewModel {
    private int hourOfDaySelected;
    private int minuteOfDaySelected;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
    }

    public int getHourOfDaySelected(){
        return hourOfDaySelected;
    }

    public int getMinuteOfDaySelected() {
        return minuteOfDaySelected;
    }

    public void setHourOfDaySelected(int hourOfDaySelected) {
        this.hourOfDaySelected = hourOfDaySelected;
    }

    public void setMinuteOfDaySelected(int minuteOfDaySelected) {
        this.minuteOfDaySelected = minuteOfDaySelected;
    }
}
