package com.example.cd.workreminder;

import android.app.Application;
import android.content.Context;

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
        CurrentWorkHours currentWorkHours = new CurrentWorkHours();
        //boolean result = currentWorkHours.doIStart(MainActivity.this, 1, 4, 5);
        //boolean expected = true;
        //assertEquals(expected, result)
    }
}