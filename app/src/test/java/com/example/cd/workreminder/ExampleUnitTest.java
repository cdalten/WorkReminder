package com.example.cd.workreminder;

import android.app.Application;

import android.icu.util.Calendar;


import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleUnitTest {
    @Test
    public void startTime_isCorrect() {
        //assertEquals(4, 2 + 2);
        MilitaryTime militaryTime = MilitaryTime.getInstance();
        militaryTime.convertStartCivilianTimeToMilitaryTime("12", "0", "PM");
        int result = militaryTime.getStartMilitaryHour();
        int expected = 12;
        assertEquals(expected, result);

        militaryTime.convertStartCivilianTimeToMilitaryTime("12", "0", "AM");
        result = militaryTime.getStartMilitaryHour();
        expected = 24;
        assertEquals(expected, result);
    }

    @Test
    public void endTime_isCorrect() {
        MilitaryTime militaryTime = MilitaryTime.getInstance();
        militaryTime.convertEndCivilianTimeToMilitaryTime("12", "0", "PM");
        int result = militaryTime.getEndMilitaryHour();
        int expected = 12;
        assertEquals(expected, result);

        militaryTime.convertEndCivilianTimeToMilitaryTime("12", "0", "AM");
        result = militaryTime.getEndMilitaryHour();
        expected = 24;
        assertEquals(expected, result);
    }

    @Test
    public void doIStart_isCorrect() {
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR);
        int currentMinute = cal.get(Calendar.MINUTE);
        int startHour = 12;
        int startMinute = 0;
        int endHour = 7;
        int endMinute = 0;
        if (cal.get(Calendar.AM_PM) == 0) {
            assertTrue(startHour >= currentHour && currentHour < endHour);
                //Log.e(PRODUCTION_TAG, "YOU'RE SUPPOSED TO BE AT WORK");
            //} else if ((currentHour == endHour) && (currentMinute > startMinute)) {
                //Log.e(PRODUCTION_TAG, "YOU'RE DONE FOR THE DAY");
            //}

        }
    }
}