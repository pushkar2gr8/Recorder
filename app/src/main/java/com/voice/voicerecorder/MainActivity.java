package com.voice.voicerecorder;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    Thread send_data;
    EditText usr_email;
    EditText usr_password;
    ProgressDialog dialog;
    Button sign_in;
    TextView mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ANSWER_PHONE_CALLS,
                        Manifest.permission.READ_PHONE_NUMBERS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE},1);

        sign_in = (Button)findViewById(R.id.signin_button);

        //Button login_button = new Button(findViewById(R.id.login_button));

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Get_Data().execute();
                    }
                });
            }
        });


        mSignUp = (TextView)findViewById(R.id.sign_up);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager mFragmentManager = getFragmentManager();
                        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

                        SignUp msign = new SignUp();
                        //mFragmentTransaction.add(R.id.login_activity,msign);
                        mFragmentTransaction.replace(R.id.login_activity,msign);
                        mFragmentTransaction.commit();
                    }
                });
            }
        });
    }

    public class Get_Data extends AsyncTask<String , Void ,String> {

        @Override
        protected void onPreExecute() {
            new Uiworker().pre_exec();
        }

        @Override
        protected String doInBackground(String... strings) {
            sendStrem();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //new Uiworker().post_exec();
        }
    }

    private void sendStrem(){
        try
        {
            usr_email = (EditText)findViewById(R.id.user_name);
            usr_password = (EditText)findViewById(R.id.password);

            String User_id = usr_email.getText().toString();
            String mPassword = usr_password.getText().toString();

            //sending user data to server
            String data = URLEncoder.encode("email", "UTF-8")
                    + "=" + URLEncoder.encode(User_id, "UTF-8");

            data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                    + URLEncoder.encode(mPassword, "UTF-8");

            URL url = new URL("http://pushkar2gr8.000webhostapp.com/dbconnect.php");
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
                if(line.contains("valid")){
                    System.out.printf("good");
                    new Uiworker().post_exec(line);

                    SharedPreferences sharedPref = getApplicationContext()
                            .getSharedPreferences("Login_Status", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("login_stat","done");
                    editor.commit();
                }else {
                    System.out.printf("not good");
                    new Uiworker().post_exec(line);
                }
            }
        }catch (Exception e){
            System.out.println("Exception sending data::"+e);
        }
    }

    public class Uiworker
    {
        public void pre_exec(){
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("please wait...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        public void post_exec(final String result){
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast tst = Toast.makeText(MainActivity.this,""+result,Toast.LENGTH_LONG);
                    tst.show();
                    usr_email.setText("");
                    usr_password.setText("");
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
