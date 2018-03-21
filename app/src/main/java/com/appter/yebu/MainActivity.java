package com.appter.yebu;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private ProgressBar prg;

    private Handler mHandler = new Handler();
    private int progress =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prg = (ProgressBar) findViewById(R.id.progressBar);
        ConnectionDetector ob = new ConnectionDetector(this);
        boolean networkConn = ob.isConnected();
        if (networkConn == true) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                android.os.SystemClock.sleep(5000);
                while(progress <100){
                    progress++;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                                      @Override
                                      public void run() {
                                          prg.setProgress(progress);

                                      }
                                  });

                }
            }
        }).start();}
    }
}
