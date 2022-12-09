package com.example.stopwatch4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private StopwatchViewModel model;

    TextView timeView;
    Button bStart, bStop, bReset;
    String newTime;

    Handler handler = new Handler();

     long timeInMillis = 0, timeSwapBuff = 0, elapsedTime = 0;

    /**
     * Needed onPause() - when Android flips the screen, onPause() is called to stop the Runnable
     * Then, we onDestroy() and when onCreate() is called, the SAME Runnable is used
     * Avoiding time confusion... :)))
     *
     */
    final Runnable updateTimerThread = new Runnable(){
        @Override
        public void run(){
            /** Find timeInMillis since start up*/
            timeInMillis = SystemClock.uptimeMillis() - model.getStartTime().getValue();

            timeSwapBuff = model.getTimeSwapBuff().getValue();

            model.getElapsedTime().setValue(timeSwapBuff + timeInMillis);
            elapsedTime = model.getElapsedTime().getValue();

            /** Calculate hours, mins, secs */
            int secs = (int) (elapsedTime / 1000);
            int mins = secs / 60;
            secs %= 60;
            int hours = mins / 60;
            int millis = (int) elapsedTime % 1000;

            newTime = String.format(Locale.getDefault(), "%02d:%02d:%02d.%01d", hours, mins, secs, millis);

            model.getCurrentTime().setValue(newTime);
            handler.post(this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeView = findViewById(R.id.time_view);
        bStart = findViewById(R.id.b_start);
        bStop = findViewById(R.id.b_stop);
        bReset = findViewById(R.id.b_reset);

        // Get StopwatchViewModel
        model = new ViewModelProvider(this).get(StopwatchViewModel.class);

        if(model.getIsRunning().getValue() == true){
            handler.post(updateTimerThread);
        }

        //updateTimerThread = updateTimerThread;

        // Create the Observer which updates the UI
        final Observer<String> timeObserver = new Observer<String>(){
            @Override
            public void onChanged(@Nullable final String newTime){

                timeView.setText(newTime);
            }
        };


        model.getCurrentTime().observe(this, timeObserver);

        /** When Start is clicked, calls Runnable and update
         *  time on the screen.
         */
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getIsRunning().getValue() == false) {
                    // Set LiveData isRunning = true
                    model.getIsRunning().setValue(true);
                    // Set LiveData startTime = SC.uptimeMillis
                    model.getStartTime().setValue(SystemClock.uptimeMillis());
                    //Call handler to run() clock
                    handler.post(updateTimerThread);
                }
            }
        });

        bStop.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               if(model.getIsRunning().getValue() == true){
                   model.getIsRunning().setValue(false);
                   model.getTimeSwapBuff().setValue(model.getTimeSwapBuff().getValue() + timeInMillis);
                   handler.removeCallbacks(updateTimerThread);
               }
           }
        });

        bReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                    model.getStartTime().setValue(SystemClock.uptimeMillis());
                    timeInMillis = 0;
                    model.getTimeSwapBuff().setValue(0L);
                    elapsedTime = 0;

                    newTime =  String.format(Locale.getDefault(), "%02d:%02d:%02d.%01d", 0, 0, 0, 0);
                    model.getCurrentTime().setValue(newTime);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimerThread);
    }

}