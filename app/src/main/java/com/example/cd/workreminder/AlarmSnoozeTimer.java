package com.example.cd.workreminder;

import java.util.TimerTask;

public class AlarmSnoozeTimer extends TimerTask {
    Thread myThreadObj;
    AlarmSnoozeTimer (Thread t){
        this.myThreadObj=t;
    }
    public void run() {
        myThreadObj.start();
    }
}