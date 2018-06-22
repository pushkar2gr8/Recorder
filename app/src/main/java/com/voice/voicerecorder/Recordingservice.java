package com.voice.voicerecorder;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;

public class Recordingservice extends Service {

    String number;
    MediaRecorder mediaRecorder = null;

    @Override
    public void onCreate(){
        super.onCreate();
//        number = new Intent().getStringExtra("CallingNumber");
//        start_recording(number);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        File root = new File(Environment.getExternalStorageDirectory(), "Recordings");
        if (!root.exists()) {
            root.mkdirs();
        }
        number = intent.getStringExtra("CallingNumber");
        MediaRecorderReady(number);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void MediaRecorderReady(String number){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory()+"/"+"Recordings/"+number+".mp3");
        start_recording();
    }

    public void start_recording(){
        try {

            System.out.print("recording started");
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    public void stop_recording(){
        System.out.println("Stopping Recorder");
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

    }
}
