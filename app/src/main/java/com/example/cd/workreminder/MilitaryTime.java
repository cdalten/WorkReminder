package com.example.cd.workreminder;

import android.app.ListActivity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

//Because I don't have the time nor patience to install the new fangled library the device emulator.
//Maybe they shouldn't fucking dep. shit?
public class MilitaryTime extends FragmentActivity {
    private int startMilitaryHour = 0;
    private int endMilitaryHour = 0; //Need to convert back to string??
    private int startMilitaryMinute = 0;


    private static MilitaryTime instance = new MilitaryTime();

    CurrentWorkHours currentWorkHours;
    private MilitaryTime () {
        currentWorkHours = new CurrentWorkHours();
    }

    public static MilitaryTime getInstance(){
        return instance;
    }
    public void convertCivilanTimeToMilitaryTime(String startHour, String startMinute, String startAmOrPm) {
        if (startAmOrPm.equals("PM")) {
            startMilitaryHour = Integer.parseInt(startHour) +12;
            //setStartMilitaryHour(startMilitaryHour);
            //setStartMilitaryMinute(Integer.parseInt(startMinute));
        } else if (startAmOrPm.equals("AM") && startHour.equals("12")) {
            startMilitaryHour = 24;
        }
        else {
            try {
                startMilitaryHour = Integer.parseInt(startHour);
                //startMilitaryMinute = Integer.parseInt(startMinute);
            } catch (Exception e) {
                Log.e("LG_WORK_PHONE",  "BLANK: " + e);
            }
            //setStartMilitaryHour(startMilitaryHour);
            //setStartMilitaryMinute(startMilitaryMinute);
        }

    }

    public int getStartMilitaryHour() {
        return this.startMilitaryHour;
    }


    public int getStartMilitaryMinute() {
        //return Integer.parseInt(this.startMinute);
        return this.startMilitaryMinute;
    }

    //Added on 10 - 18 - 2018. True if I work. False if I'm either done working or have the day off.
    public boolean getStartingHour(int startHour, int startMinute) {
        //today = "12:30am - 3:30am"; //debugging only
        //dateDebugMode(today);

        /*if (startHour.equals("12")) {
            if (startAmOrPm.equals("AM")) {
                return true;
            }
        }
        //Handle initial installation case. Do I need?
        if (!Character.isDigit(startHour.charAt(0))) {
            return false;
        }

        //Can I remove this test case??
        if (startHour.equals("OFF")) { //2 --->6a or 6A
            return false;
        }*/

        return currentWorkHours.doIWorkToday(startHour, startMinute);
    }
}
