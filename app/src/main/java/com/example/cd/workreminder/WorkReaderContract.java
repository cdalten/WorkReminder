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

        public static boolean updateSchesdule = false; //Added on 6 - 26 - 2019
    }
}//end class
