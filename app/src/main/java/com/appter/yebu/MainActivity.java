package com.appter.yebu;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private ProgressBar prg;
    private Button loginButton;
    private Button shutdownButton;
    private TextView startText;
    private TextView serverFailText;
    private Handler mHandler = new Handler();
    private String pingUrl = "http://www.yebu.de";
    private EditText user;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.login);
        shutdownButton = findViewById(R.id.shutdown);
        startText = findViewById(R.id.startpagetext);
        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        serverFailText = findViewById(R.id.serverFailtext);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new checkServer_async(MainActivity.this, pingUrl).execute("");
            }
        }, 5);

    }

    private class checkServer_async extends AsyncTask<String, Void, Boolean> {

        private Context context;
        private String pingUrl;


        checkServer_async(Context context, String url) {
            this.context = context;
            pingUrl = url;

        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Server test started", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            ConnectionDetector internetConn = new ConnectionDetector(context);
            boolean networkConn = internetConn.haveNetworkConnection();
            boolean pingTest = internetConn.isOnline(pingUrl);
            return (networkConn && pingTest);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            final Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            if (result) {
                Toast.makeText(context, "Server verfügbar", Toast.LENGTH_LONG).show();
                MainActivity.this.startActivity(myIntent);
            } else {
                Toast.makeText(context, "Internet Verbindung gestört", Toast.LENGTH_LONG).show();
                startText.setVisibility(View.INVISIBLE);
                serverFailText.setVisibility(View.VISIBLE);
                shutdownButton.setVisibility(View.VISIBLE);
                shutdownButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
            }
            ;
        }
    }
}




