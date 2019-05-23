package com.example.cd.workreminder;

//package com.example.cd.shiftreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Because the Main class is getting as bloated as your mom's ass.
public class CurrentWorkWeek extends com.example.cd.workreminder.MainActivity implements Parcelable {
    //public class CurrentWorkWeek extends FragmentActivity {
    private int currentDay;

    private String startHour;
    private String startMinute;
    private String startAmOrPm ;
    private String endHour;
    private String endMinute;
    private String endAmOrPm;
    private String currentWorkHours; //Modified on 3 - 8 - 2019
    private String dayOfWeek = "OFF"; //Added o n 3 - 12 - 2019

    private int hourBeforeShift;
    private int minutesBeforeShift; //Added on 4 - 13 - 2019
    private int currentPosition = -1; //error code

    //private SharedPreferences pref = this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);; //Changed to private on 11 - 19 - 2018
    private SharedPreferences pref;
    //SharedPreferences.Editor editPref = pref.edit();
    private SharedPreferences.Editor editPref;
    private String Key; //Added on 11 - 16 - 2018

    private String day; //Added on 2 - 10 - 2019

    //private ArrayList<String> workHours = new ArrayList<>(); //Added on 2 - 24 - 2019
    private int weekPosition = 0; //Added on 3 - 28 - 2019

    private int startMilitaryHour = 0; //Added on 4 - 16 - 2019
    private int startMilitaryMinute = 0; //Added on 5 - 14 - 2019
    private int endMiltiaryHour = 0;
    private int endMilitaryMinute = 0; //Added on 5 - 15 - 2019
    private int totalMilitaryStartTime = 0; //Added on 5 - 15 - 2019
    private int totalMilitaryEndTime = 0; //Added on 5 - 15 - 2019

    //Modified on 4 - 29 - 2019
    public CurrentWorkWeek() {
        setStartHour("");
        setStartMinute("");
        setStartAmOrPm("");
        setEndHour("");
        setEndMinute("");
        setEndAmOrPm("0");
        /*SharedPreferences.Editor editor = pref.edit();
        editor.putInt("HOUR", 99); //military
        editor.putInt("MINUTES", 99);
        editor.commit();
        */
    }

    //Modified on 4 - 29 - 2019
    public CurrentWorkWeek(String DoIWorkToday) {
        dayOfWeek = DoIWorkToday;
        setStartHour("0");
        setStartMinute("0");
        setStartAmOrPm("0");
        setEndHour("0");
        setEndMinute("0");
        setEndAmOrPm("0");
    }


    //Added on 2 - 15 - 2019
    public CurrentWorkWeek(SharedPreferences pref, Context context, String day, String startHour, String startMinute,
                           String startAmOrPm, String endHour, String endMinute, String endAmOrPm) {
        //super();
        this.pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        //this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!day.equals("OFF")) {
            this.dayOfWeek = day;
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.startAmOrPm = startAmOrPm;
            this.endHour = endHour;
            this.endMinute = endMinute;
            this.endAmOrPm = endAmOrPm;

            convertCivilanTimeToMilitaryTime(startHour, startMinute, startAmOrPm, endHour, endMinute, endAmOrPm);
            //SharedPreferences.Editor editor = pref.edit();

            //editor.putString(context.getString(R.string.SAVE_DAY), this.dayOfWeek);
            //editor.apply();
        }//end if

    }

    public void convertCivilanTimeToMilitaryTime(String startHour, String startMinute, String startAmOrPm) {
        int startMilitaryHour = 0;
        int endMilitaryHour = 0; //Need to convert back to string??

        if (startAmOrPm.equals("PM")) {
            startMilitaryHour = Integer.parseInt(startHour) + 12;
            setStartMilitaryHour(startMilitaryHour);
            setStartMilitaryMinute(Integer.parseInt(startMinute));
        } else {
            try {
                startMilitaryHour = Integer.parseInt(startHour);
                startMilitaryMinute = Integer.parseInt(startMinute);
            } catch (Exception e) {
                Log.e("LG_WORK_PHONE",  "BLANK: " + e);
            }
            setStartMilitaryHour(startMilitaryHour);
            setStartMilitaryMinute(startMilitaryMinute);
        }

    }

    public void convertCivilanTimeToMilitaryTime(String startHour, String startMinute, String startAmOrPm,
                                                 String endHour, String endMinute, String endAmOrPm) {
        int startMilitaryHour = 0;
        int endMilitaryHour = 0; //Need to convert back to string??

        if (startAmOrPm.equals("PM")) {
            startMilitaryHour = Integer.parseInt(startHour) + 12;
            setStartMilitaryHour(startMilitaryHour);
            setStartMilitaryMinute(Integer.parseInt(startMinute));
        } else {
            try {
                startMilitaryHour = Integer.parseInt(startHour);
                startMilitaryMinute = Integer.parseInt(startMinute);
            } catch (Exception e) {
                Log.e("LG_WORK_PHONE",  "BLANK: " + e);
            }
            setStartMilitaryHour(startMilitaryHour);
            setStartMilitaryMinute(startMilitaryMinute);
        }

        if (endAmOrPm.equals("PM")) {
            endMilitaryHour = Integer.parseInt(endHour) + 12;
            setEndMilitaryHour(endMilitaryHour);
            setEndMilitaryMinute(endMilitaryMinute);
        } else {
            try {
                endMilitaryHour = Integer.parseInt(endHour);
                endMilitaryMinute = Integer.parseInt(endMinute);
            } catch (Exception e) {
                Log.e("LG_WORK_PHONE",  "BLANK: " + e);
            }
            setEndMilitaryHour(endMilitaryHour);
            setEndMilitaryMinute(endMilitaryMinute);
        }

    }


    //Added on 4 - 15 - 2019.
    //Can remove from date object??
    private void setStartMilitaryHour(int startMilitaryHour) {
        this.startMilitaryHour = startMilitaryHour;
    }

    //Added on 5 - 14 - 2019
    private void setStartMilitaryMinute(int startMilitaryMinute) {
        this.startMilitaryMinute = startMilitaryMinute;
    }

    private void setEndMilitaryHour(int endMilitaryHour) {
        this.endMiltiaryHour = endMilitaryHour;
    }

    private void setEndMilitaryMinute(int endMilitaryMinute) {
        this.endMilitaryMinute = endMilitaryMinute;
    }
    public int getStartMilitaryHour() {
        return this.startMilitaryHour;
    }


    public int getStartMilitaryMinute() {
        //return Integer.parseInt(this.startMinute);
        return this.startMilitaryMinute;
    }

    public int getEndMiliaryHour() {
        return this.endMiltiaryHour;
    }

    //Added on 3 - 27 - 2019
    public CurrentWorkWeek(Parcel parcel) {
        this.dayOfWeek = parcel.readString();
        this.startHour = parcel.readString();
        this.startMinute = parcel.readString();
        this.startAmOrPm = parcel.readString();
        this.endHour = parcel.readString();
        this.endMinute = parcel.readString();
        this.endAmOrPm = parcel.readString();
    }

    public static final Parcelable.Creator<CurrentWorkWeek> CREATOR = new Parcelable.Creator<CurrentWorkWeek>() {

        @Override
        public CurrentWorkWeek createFromParcel(Parcel parcel) {
            return new CurrentWorkWeek(parcel);
        }

        @Override
        public CurrentWorkWeek[] newArray(int size) {
            return new CurrentWorkWeek[0];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dayOfWeek);
        dest.writeString(this.startHour);
        dest.writeString(this.startMinute);
        dest.writeString(this.startAmOrPm);
        dest.writeString(this.endHour);
        dest.writeString(this.endMinute);
        dest.writeString(this.endAmOrPm);
    }

    //Added on 3 - 28 - 2019 in order to debugger the menu options
    public void setDayofWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDayOfWeekBasedOnPosition(int weekPosition) {
        this.weekPosition = weekPosition;

    }

    //Added on 3 - 27 - 2019
    public String getDayOfWeek() {return this.dayOfWeek; }

    public String getStartHour() {
        return this.startHour;
    }

    public String getStartMinute() {
        return this.startMinute;
    }

    public String getStartAmOrPm() {
        return this.startAmOrPm;
    }

    public String getEndHour() {
        return this.endHour;
    }

    public String getEndMinute() {
        return this.endMinute;
    }

    public String getEndAmOrPm() {
        return this.endAmOrPm;
    }


    //Added on 3 - 29 - 2019
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    public void setStartAmOrPm(String startAmOrPm) {
        this.startAmOrPm = startAmOrPm;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }

    public void setEndAmOrPm(String endAmOrPm) {
        this.endAmOrPm = endAmOrPm;
    }

    //Added on 3 - 9 - 2019
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }

    //ugly format hack. Don't need to save preferences
    @Override
    public String toString() {
        return super.toString();
    }

}//end class
