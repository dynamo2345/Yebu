package com.appter.yebu;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ConnectionDetector {

    private Context context;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


   public boolean isOnline(String pingurl)  {
       String response = "";
       Boolean output = false;
       HttpURLConnection conn ;
       StringBuilder httpResults = new StringBuilder();
       try {
           //setting URL to connect with
           URL url = new URL(pingurl);
           //creating connection
           conn = (HttpURLConnection) url.openConnection();
            /*
            converting response into String
            */
           InputStreamReader in = new InputStreamReader(conn.getInputStream());
           int read;
           char[] buff = new char[1024];
           while ((read = in.read(buff)) != -1) {
               httpResults.append(buff, 0, read);
           }
           response = httpResults.toString();
           if (response != null) { output = true;}
       }catch(Exception e){
           e.printStackTrace();
       }
       return output;
   }
   }











