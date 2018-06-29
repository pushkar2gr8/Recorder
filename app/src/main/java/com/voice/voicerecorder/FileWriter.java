package com.voice.voicerecorder;

import android.app.Service;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileWriter extends AsyncTask<Void, Void, Void>{

    String number,date,time;

    public FileWriter(String number, String date, String time) {
        this.number = number;
        this.date = date;
        this.time = time;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        writer(number,date,time);
        return null;
    }

    public void writer(String number, String date, String time){

        String data = number + time + date ;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
            Date now = new Date();
            String fileName = "/Recordings/"+formatter.format(now) + ".txt";
            File root = new File(Environment.getExternalStorageDirectory(), "");
            if (!root.exists()) {
                root.mkdirs();
            }

            System.out.print("file path::"+root.getAbsolutePath()+fileName);

            try {
                File file = new File(root.getAbsolutePath()+ fileName);
                if (!file.exists()) {
                    new File(root.getAbsolutePath()).mkdir();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file,true);
                fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();


            }  catch(Exception ex) {
                ex.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
