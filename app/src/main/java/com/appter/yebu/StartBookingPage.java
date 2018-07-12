package com.appter.yebu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StartBookingPage extends AppCompatActivity {

    private String[] pageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_booking_page);
        ListView listView1 = (ListView) findViewById(R.id.LV1);
        Intent iin= getIntent();
        pageContent = iin.getStringArrayExtra("FZGList");
        int nrFzg = iin.getIntExtra("nrFZG", 0);
        String[] items = new String[nrFzg];
        int count = 0;

        while (pageContent[count] != null) {
          items[count] = pageContent[count];
          count++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, items);

        listView1.setAdapter(adapter);



        //Log.i("Out",pageContent);
    }
}
