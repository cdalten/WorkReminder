package com.example.cd.workreminder;

//package com.example.cd.shiftreminder;

import android.provider.BaseColumns;

public final class WorkReaderContract {
    private WorkReaderContract() {}

    public static class WorkEntry implements BaseColumns {

        public static final int ALARM_DEFAULT = 20; //20 minutes before start of shift
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

        public static final int START_HOUR = 1; //Added on 5 - 29 - 2019
        public static final int START_MINUTE = 2;
        public static final int START_AM_OR_PM = 3;
        public static final int END_HOUR = 4;
        public static final int END_MINUTE = 5;
        public static final int END_AM_OR_PM = 6;


    }
}//end class
