package com.voice.voicerecorder;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class DataWorker extends AsyncTask<Void, Void, Void> {

    String calldetails,date,calltime;

    public DataWorker(String calldetails, String date, String calltime) {
        this.calldetails = calldetails;
        this.date = date;
        this.calltime = calltime;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        writer(calldetails,date,calltime);
        return null;
    }

    public void writer(String calldetails, String date, String calltime){
        try
        {

            //sending user data to server
            String data = URLEncoder.encode("CallDetails", "UTF-8")
                    + "=" + URLEncoder.encode(calldetails, "UTF-8");

            data += "&" + URLEncoder.encode("date", "UTF-8") + "="
                    + URLEncoder.encode(date, "UTF-8");

            data += "&" + URLEncoder.encode("CallTime", "UTF-8") + "="
                    + URLEncoder.encode(calltime, "UTF-8");


            URL url = new URL("http://pushkar2gr8.000webhostapp.com/recordinsert.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            //reading server responce
            String text = "";
            BufferedReader reader=null;
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");

                System.out.println("Server Responce::"+line);
                if(line.contains("success")){
                    System.out.printf("good");
                }else {
                    System.out.printf("not good");
                }
            }
        }catch (Exception e){
            System.out.println("Exception sending data::"+e);
        }
    }

}
