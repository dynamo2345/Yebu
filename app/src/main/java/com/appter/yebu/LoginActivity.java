package com.appter.yebu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private OkHttpClient okHttpClient;
    private String url = "https://yebu.de/cgi-bin/rr/rrmain.py";
    public String message = "LEER";
    String[] Fzg = new String[30];
    String[] FzgResp = new String[30];
    String[] bookingLine = new String[100];
    String[][] booking_item = new String[100][7];

//Booking Array
    String[] fzgBooking = new String [100];
    String[] fromDateBooking = new String[100];
    String[] toDateBooking = new String [100];
    String[] fromBooking = new String[100];
    String[] forBooking = new String[100];
    String[] commentsBooking = new String[100];


    private Intent intent;
    //private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(LoginActivity.this, StartBookingPage.class);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        mEmailSignInButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                attemptLogin();

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        new UserLoginTask().execute();
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;


        UserLoginTask() {
            mEmail = "";
            mPassword = "";

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("account", "czermak")
                    .add("i_password", "digital30")
                    .add("f", "login")
                    //.add("username", "tester")
                    //.add("password", "test")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();


            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("Out", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    message = response.body().string();
                    Log.i("out", message);


                }
            });


            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        // TODO: register the new account here.

        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            //showProgress(false);
            String selectedVal = null;

            int Fzgcount = 0;
            int bookingLineCount = 0;
            int bookingRowsCount = 0;
            int startpos = 0;
            int endpos = 0;
            Log.i("out", message);
            org.jsoup.nodes.Document doc =  Jsoup.parse(message);
            //Element content = doc.getElementById("resource");

            Elements options = doc.getElementsByAttributeValue("name", "resource").get(0).children();
            for (Element option : options) {
                selectedVal = option.toString();
                startpos = selectedVal.indexOf("=");
                endpos = selectedVal.indexOf(">");
                FzgResp[Fzgcount] = selectedVal.substring(startpos + 2, endpos-1);
                Log.i("out", selectedVal.substring(startpos + 1, endpos));

                startpos = selectedVal.indexOf(">");
                endpos = selectedVal.lastIndexOf("<");
                Fzg[Fzgcount] = selectedVal.substring(startpos + 1, endpos);
                Log.i("out", selectedVal.substring(startpos + 1, endpos));
                Fzgcount++;
            }
            Element option1 = doc.getElementById("sortable_table");
            Elements  section = option1.select("tbody");
            Elements rows = section.select("tr");
            for (Element item : rows) {
                Elements booking = item.select("td");
                for (Element booking_field : booking) {
                    booking_item[bookingLineCount][bookingRowsCount] = booking_field.toString();
                    if (booking_item[bookingLineCount][bookingRowsCount].indexOf("</span>") > 0) {
                        startpos = booking_item[bookingLineCount][bookingRowsCount].indexOf("</span>");
                        endpos = booking_item[bookingLineCount][bookingRowsCount].indexOf("</td>");
                        booking_item[bookingLineCount][bookingRowsCount] = booking_item[bookingLineCount][bookingRowsCount].substring(startpos + 8, endpos);
                    } else {
                        startpos = booking_item[bookingLineCount][bookingRowsCount].indexOf("<td>");
                        endpos = booking_item[bookingLineCount][bookingRowsCount].indexOf("</td>");
                        booking_item[bookingLineCount][bookingRowsCount] = booking_item[bookingLineCount][bookingRowsCount].substring(startpos + 4, endpos);
                    }

                    switch (bookingRowsCount) {
                        case 0:
                            fzgBooking[bookingLineCount] = booking_item[bookingLineCount][bookingRowsCount];
                            break;
                        case 1:
                            fromDateBooking[bookingLineCount] = booking_item[bookingLineCount][bookingRowsCount];
                            break;
                        case 2:
                            toDateBooking[bookingLineCount] = booking_item[bookingLineCount][bookingRowsCount];
                            break;
                        case 3:
                            fromBooking[bookingLineCount] = booking_item[bookingLineCount][bookingRowsCount];
                            break;
                        case 4:
                            forBooking[bookingLineCount] = booking_item[bookingLineCount][bookingRowsCount];
                            break;
                        case 5:
                            commentsBooking[bookingLineCount] = booking_item[bookingLineCount][bookingRowsCount];
                            break;
                    }


                    bookingRowsCount++;
                }

                bookingLine[bookingLineCount] = booking_item[bookingLineCount][0] + " " + booking_item[bookingLineCount][1] + " " + booking_item[bookingLineCount][2] + " " + booking_item[bookingLineCount][3] + " " +
                        booking_item[bookingLineCount][4] + " " + booking_item[bookingLineCount][5];
               // Log.i("out", booking_item[bookingLineCount][0] + " " + booking_item[bookingLineCount][1] + " " + booking_item[bookingLineCount][2] + " " + booking_item[bookingLineCount][3] + " " +
                //        booking_item[bookingLineCount][4] + " " + booking_item[bookingLineCount][5]);

                Log.i("out", fzgBooking[bookingLineCount] + " " + fromDateBooking[bookingLineCount] + " " + toDateBooking[bookingLineCount]
                        + " " + fromBooking[bookingLineCount] + " " + forBooking[bookingLineCount] + " " + commentsBooking[bookingLineCount]);
                bookingLineCount++;
                bookingRowsCount = 0;
            }

  //          Log.i("out",booking_item[3][4]);
  //          Log.i("out",String.valueOf(bookingLineCount));
  //          Log.i("out",String.valueOf(bookingRowsCount));

            //List <String> FZGlist = new ArrayList<>(Arrays.asList(Fzg));
            //String [] fzgList = new String[Fzgcount];
            //fzgList = Fzg;
            //Log.i("out",Fzg[3]);
            intent = new Intent(LoginActivity.this, StartBookingPage.class);
            //intent.putExtra("FZGList", Fzg);
            //intent.putExtra("nrFZG", Fzgcount);
            intent.putExtra("FZGList", bookingLine);
            intent.putExtra("nrFZG", bookingLineCount);
            startActivity(intent);


            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }


            @Override
            protected void onCancelled () {
                //mAuthTask = null;
                //showProgress(false);
            }
        }
    }






