package com.example.cd.workreminder;

//package com.example.cd.shiftreminder;

import android.provider.BaseColumns;

public final class WorkReaderContract {
    private WorkReaderContract() {}

    public static class WorkEntry implements BaseColumns {
        public static final String TABLE_NAME = "week";
        public static final String COLUMN_NAME_EMPLOYEE_ID = "employeeID";
        public static final String COLUMN_NAME_WEEK = "week";
        public static final String COLUMN_NAME_START_HOUR = "startHour";
        public static final String COLUMN_NAME_START_MINUTE = "startMinute";
        public static final String COLUMN_NAME_START_AM_OR_PM = "startAmOrPm";
        public static final String COLUMN_NAME_END_HOUR = "endHour";
        public static final String COLUMN_NAME_END_MINUTE = "endMinute";
        public static final String COLUMN_NAME_END_AM_OR_PM = "endAmOrPm";

        public static final String ALARM_DEFAULT = "20"; //20 minutes before start of shift
        public static final String START_HOUR_DEFAULT = "12"; //Added on 5 - 22 - 2019
        public static final String START_MINUTE_DEFAULT = "0";
        public static final String START_AM_OR_PM_DEFAULT = "AM";
        public static final String END_HOUR_DEFAULT = "12";
        public static final String END_MINUTE_DEFAULT = "0";
        public static final String END_AM_OR_PM_DEFAULT = "AM";
    }
}//end class
