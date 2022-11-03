/*
 Copyright © 2017-2019 Chad Altenburg <cdalten@PumpingDansHotLookingStepMom.com>

 Permission to use, copy, modify, distribute, and sell this software and its
 documentation for any purpose is hereby granted without fee, provided that
 the above copyright notice appear in all copies and that both that
 copyright notice and this permission notice appear in supporting
 documentation.  No representations are made about the suitability of this
 software for any purpose.  It is provided "as is" without express or
 implied warranty.
*/
package com.example.cd.workreminder;

import android.provider.BaseColumns;

public final class WorkReaderContract {
    private WorkReaderContract() {}

  //  public static class WorkEntry implements BaseColumns {

        public static final int ALARM_MINUTE_DEFAULT = 20; //20 minutes before start of shift
        public static final int ALARM_HOUR_DEFAULT = 0; //0 hours before start of shift
        public static final String DAY_OFF_DEFAULT = "OFF"; //Added on 11 - 15 - 2019
        public static final String START_HOUR_DEFAULT = "12"; //Added on 5 - 22 - 2019
        public static final String START_MINUTE_DEFAULT = "00";
        public static final String START_AM_OR_PM_DEFAULT = "AM";
        public static final String END_HOUR_DEFAULT = "12";
        public static final String END_MINUTE_DEFAULT = "00";
        public static final String END_AM_OR_PM_DEFAULT = "AM";

        public static final int SUNDAY = 0;
        public static final int MONDAY = 1;
        public static final int TUESDAY = 2;
        public static final int WEDNESDAY = 3;
        public static final int THURSDAY = 4;
        public static final int FRIDAY = 5;
        public static final int SATURDAY = 6;
        public static final int OFF = 7; //Added on 5 - 28 - 2019

        public static final int DAY_OF_WEEK = 0; //Added on 11 - 15 - 2019
        public static final int START_HOUR = 1; //Added on 5 - 29 - 2019
        public static final int START_MINUTE = 2;
        public static final int START_AM_OR_PM = 3;
        public static final int END_HOUR = 4;
        public static final int END_MINUTE = 5;
        public static final int END_AM_OR_PM = 6;

        public static final int RESULT_OK_WORK = 1; //Added on 11 - 12 - 2019
        public static final int RESULT_FAILED = 2;
        public static final int RESULT_OKAY_NO_WORK = 3; //Added on 11 - 13 - 2019
        public static final int RESULT_OKAY_UPDATE_WORK_ALARM_TIME = 4; //Added on 12 - 19 - 2019

        public static final boolean ALARM_NOTIFICATION_RINGS = true;
        public static final boolean ALARM_NOTIFICATION_SILENT = false;
        public static final boolean SNOOZE_ON = true;
        public static final boolean SNOOZE_OFF = false;
        public static final boolean ALARM_RINGS = true; //Added on 1 - 31 - 2020
        public static final boolean ALARM_SILENT = false; //Added on 1 - 31 - 2020

        public static final int hour = 60;

        public static int alarm_default = 20;

        public static final int ON_DAY = 0; //Added on 10 - 12 - 2022
        public static final int OFF_DAY = 1; //Added onn 10 - 12 - 2022
        public static int SELECTION_DEFAULT_VALUE = 0; //Added on 10 - 12 - 2022


        //change int to Integer.


    //}
}//end class
