package com.voice.voicerecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Date;

public class CallReceiver extends BroadcastReceiver {

    String savedNumber;
    int state = 5;

    MediaRecorder mMediaRecorder = new MediaRecorder();

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;

    @Override
    public void onReceive(Context context, Intent intent) {

        mMediaRecorder =null;
        try{
            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

            }
            else{
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                    if(state !=0){
                        new FileWriter().writer(number, new Date().toString(),callStartTime.toString());
                        stop_recorder(context);
                        //new Recordingservice().stop_recording();
                    }
                    state = TelephonyManager.CALL_STATE_IDLE;
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                    state = TelephonyManager.CALL_STATE_OFFHOOK;
                }
                else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                    state = TelephonyManager.CALL_STATE_RINGING;
                }

                onCallStateChanged(context, state, number);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void onCallStateChanged(Context context, int state, String number)
            throws InterruptedException {


        if(lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                Toast.makeText(context, "Incoming Call Ringing" , Toast.LENGTH_LONG).show();
                callrecord(context,number);

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();
                    Toast.makeText(context, "Outgoing Call Started" , Toast.LENGTH_LONG).show();
                    callrecord(context,number);
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:

                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                    Toast.makeText(context, "Ringing but no pickup" +
                            number + " Call time " +
                            callStartTime +" Date " + new Date() ,
                            Toast.LENGTH_LONG).show();
                }
                else if(isIncoming){

                    String calldetails = "incoming:"+number;
                    Toast.makeText(context, "Incoming " +
                            number + " Call time " +
                            callStartTime  + new Date()
                            , Toast.LENGTH_LONG).show();
                    new FileWriter().writer(calldetails, new Date().toString(),callStartTime.toString());
                    stop_recorder(context);
                    //new Recordingservice().stop_recording();

                }
                else{
                    String calldetails = "outgoing"+number;
                    System.out.print(""+calldetails);
                    Toast.makeText(context, "outgoing " +
                            number + " Call time " +
                            callStartTime +" Date " + new Date() ,
                            Toast.LENGTH_LONG).show();

                    new FileWriter().writer(calldetails, new Date().toString(),callStartTime.toString());
                    stop_recorder(context);
//                    new Recordingservice().stop_recording();

                }

                break;
        }
        lastState = state;
    }

    public void callrecord(final Context context, final String number){

        Thread t = new Thread(){
            public void run(){
                Intent i =  new Intent(context,Recordingservice.class);
                i.putExtra("CallingNumber", number);
                context.startService(i);
            }
        };
        t.start();

    }

    public void stop_recorder(Context context){
        context.stopService(new Intent(context, Recordingservice.class));
    }
}
