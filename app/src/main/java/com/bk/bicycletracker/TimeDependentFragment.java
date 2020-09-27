package com.bk.bicycletracker;

import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public abstract class TimeDependentFragment extends Fragment {
    protected TextView txtWeekorDate;
    protected Calendar currentWeek = Calendar.getInstance();

    abstract public void dateChanged(Calendar newDate);

    protected void updateTimeDependentStrings()
    {
        if (currentWeek.get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR))
        {
            txtWeekorDate.setText("This week");
        }
        else if (currentWeek.get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) -1)
        {
            txtWeekorDate.setText("Last week");
        }
        else
        {
            txtWeekorDate.setText("Week "+currentWeek.get(Calendar.WEEK_OF_YEAR));
        }
    }

}
