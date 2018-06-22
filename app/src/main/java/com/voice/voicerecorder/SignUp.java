package com.voice.voicerecorder;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends Fragment implements View.OnClickListener
{
    private static Button mSingUp;
    private static View view;
    private static EditText mUserName;
    private static TextView mExistingUser;
    private static EditText mEmail;
    private static EditText mPhone;
    private static EditText mPassword;
    private static EditText mConfirmPassword;
    private static CheckBox mTermsnConditions;

    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_signup, container, false);
        getviews();
        setListners();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:
                checkValidation();
                break;

            case R.id.already_user:
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getviews(){

        mSingUp = (Button)view.findViewById(R.id.signUpBtn);
        mUserName = (EditText)view.findViewById(R.id.fullName);
        mEmail= (EditText)view.findViewById(R.id.userEmailId);
        mPhone=(EditText)view.findViewById(R.id.mobileNumber);
        mPassword= (EditText)view.findViewById(R.id.sign_up_password);
        mConfirmPassword= (EditText)view.findViewById(R.id.confirmPassword);
        mTermsnConditions = (CheckBox)view.findViewById(R.id.terms_conditions);
        mExistingUser = (TextView)view.findViewById(R.id.already_user);
    }

    private void setListners()
    {
        mSingUp.setOnClickListener(this);
        mExistingUser.setOnClickListener(this);
    }

    public class SendData extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            new SignUp.Uiworker().pre_exec();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sendStrem();
            return null;
        }
    }

    private void checkValidation() {

        // Get all edittext texts
        String getFullName = mUserName.getText().toString();
        String getEmailId = mEmail.getText().toString();
        String getMobileNumber = mPhone.getText().toString();
        String getPassword = mPassword.getText().toString();
        String getConfirmPassword = mConfirmPassword.getText().toString();

        // Pattern match for email id
        String regex= "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() < 4
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() < 10
                || getPassword.equals("") || getPassword.length() == 5
                || getConfirmPassword.equals("") || getConfirmPassword.length() == 5)

//            new CustomToast().Show_Toast(getActivity(), view,
//                    "All fields are required.");
            Toast.makeText(getActivity(), "Check Fields.", Toast.LENGTH_SHORT)
                    .show();

            // Check if email id valid or not
        else if (!m.find())
            Toast.makeText(getActivity(), "Your Email Id is Invalid.", Toast.LENGTH_SHORT)
                    .show();
//            new CustomToast().Show_Toast(getActivity(), view,
//                    "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            Toast.makeText(getActivity(), "Both password doesn't match.", Toast.LENGTH_SHORT)
                    .show();
//            new CustomToast().Show_Toast(getActivity(), view,
//                    "Both password doesn't match.");

            // Make sure user should check Terms and Conditions checkbox
        else if (!mTermsnConditions.isChecked())
            Toast.makeText(getActivity(), "Please select Terms and Conditions.", Toast.LENGTH_SHORT)
                    .show();
//            new CustomToast().Show_Toast(getActivity(), view,
//                    "Please select Terms and Conditions.");

            // Else do signup or do your stuff
        else
            new SendData().execute();

    }

    private void sendStrem(){
        try
        {

            String User_name =  mUserName.getText().toString();
            String Email =  mEmail.getText().toString();
            String Phone = mPhone.getText().toString();
            String Password = mPassword.getText().toString();


            //sending user data to server
            String data = URLEncoder.encode("name", "UTF-8")
                    + "=" + URLEncoder.encode(User_name, "UTF-8");

            data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                    + URLEncoder.encode(Password, "UTF-8");

            data += "&" + URLEncoder.encode("email", "UTF-8") + "="
                    + URLEncoder.encode(Email, "UTF-8");

            data += "&" + URLEncoder.encode("phone", "UTF-8") + "="
                    + URLEncoder.encode(Phone, "UTF-8");

            URL url = new URL("http://pushkar2gr8.000webhostapp.com/dbinsert.php");
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
                if(line.equals("valid")){
                    System.out.printf("good");
                    new SignUp.Uiworker().post_exec(line);
                }else {
                    System.out.printf("not good");
                    new SignUp.Uiworker().post_exec(line);
                }
            }
        }catch (Exception e){
            System.out.println("Exception sending data::"+e);
        }
    }

    public class Uiworker
    {
        public void pre_exec(){
            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("please wait...");
            dialog.show();
        }

        public void post_exec(final String result){
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast tst = Toast.makeText(getActivity(),""+result,Toast.LENGTH_LONG);
                    tst.show();
                    mUserName.setText("");
                    mEmail.setText("");
                    mPhone.setText("");
                    mPassword.setText("");
                    mConfirmPassword.setText("");
                    mTermsnConditions.setText("");
                }
            });
        }
    }
}
