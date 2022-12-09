package com.example.stopwatch4;

import android.os.SystemClock;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class StopwatchViewModel extends ViewModel {
    private MutableLiveData<String> currentTime;
    private MutableLiveData<Boolean> isRunning;
    private MutableLiveData<Long> startTime;
    private MutableLiveData<Long> elapsedTime;
    private MutableLiveData<Long> timeSwapBuff;

    /**
     * If currentTime hasn't been given a value,
     * create it and initialize to 0.
     * @return
     */
    public MutableLiveData<String> getCurrentTime(){
        if(currentTime == null){
            currentTime = new MutableLiveData<>();
            currentTime.setValue(String.format(Locale.getDefault(), "%02d:%02d:%02d.%01d", 0, 0, 0, 0));
        }
        return currentTime;
    }

    /**
     * If isRunning hasn't been assigned a value yet,
     * create it and set it to false.
     * @return
     */
    public MutableLiveData<Boolean> getIsRunning(){
        if(isRunning == null){
            isRunning = new MutableLiveData<>();
            isRunning.setValue(false);
        }
        return isRunning;
    }

    /**
     * If startTime hasn't been assigned a value yet,
     * create it and set it time since app start
     * @return
     */
    public MutableLiveData<Long> getStartTime(){
        if(startTime == null){
            startTime = new MutableLiveData<>();
            startTime.setValue(SystemClock.uptimeMillis());
        }
        return startTime;
    }

    public MutableLiveData<Long> getElapsedTime(){
        if(elapsedTime == null){
            elapsedTime = new MutableLiveData<>();
        }
        return elapsedTime;
    }

    public MutableLiveData<Long> getTimeSwapBuff(){
        if(timeSwapBuff == null) {
            timeSwapBuff = new MutableLiveData<>();
            timeSwapBuff.setValue(0L);
        }
        return timeSwapBuff;
    }
}
