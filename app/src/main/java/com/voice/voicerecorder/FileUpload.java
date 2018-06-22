package com.voice.voicerecorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

public class FileUpload extends AsyncTask<Void, Void, Void>{

    String upLoadServerUri = "http://pushkar2gr8.000webhostapp.com/recorder.php";

    static String lineEnd = "\r\n";
    static String twoHyphens = "--";
    static String boundary = "AaB03x87yxdkjnxvi7";
    String data;
    int serverResponseCode = 0;


    @Override
    protected Void doInBackground(Void... voids) {
//        upload();

        String existingFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Recordings/";
        File f = new File(existingFileName);
        File[] contents = f.listFiles();

        for (int i =0; i<contents.length;i++ ){
            uploadFile(contents[i].toString());
        }
        return null;
    }

//    public void upload() {
//
//        HttpURLConnection conn = null;
//        DataOutputStream dos = null;
//        DataInputStream inStream = null;
//        //String existingFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mypic.png";
//        String existingFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/";
//        File f = new File(existingFileName);
//        File[] contents = f.listFiles();
//        for(int i=0;i<contents.length;i++){
//            System.out.println("files::"+contents[i]);
//
//            String lineEnd = "\r\n";
//            String twoHyphens = "--";
//            String boundary = "*****";
//            int bytesRead, bytesAvailable, bufferSize;
//            byte[] buffer;
//            int maxBufferSize = 1 * 1024 * 1024;
//            String responseFromServer = "";
//            String urlString = "http://pushkar2gr8.000webhostapp.com/recorder.php";
//
//            try {
//
////            System.out.print("file path::"+contents[0].toString());
//                //------------------ CLIENT REQUEST
//                FileInputStream fileInputStream = new FileInputStream(new File(contents[i].toString()));
//                // open a URL connection to the Servlet
//                URL url = new URL(urlString);
//                // Open a HTTP connection to the URL
//                conn = (HttpURLConnection) url.openConnection();
//                // Allow Inputs
//                conn.setDoInput(true);
//                // Allow Outputs
//                conn.setDoOutput(true);
//                // Don't use a cached copy.
//                conn.setUseCaches(false);
//                // Use a post method.
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Connection", "Keep-Alive");
//                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                dos = new DataOutputStream(conn.getOutputStream());
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + existingFileName + "\"" + lineEnd);
//                dos.writeBytes(lineEnd);
//                // create a buffer of maximum size
//                bytesAvailable = fileInputStream.available();
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                buffer = new byte[bufferSize];
//                // read file and write it into form...
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                while (bytesRead > 0) {
//
//                    dos.write(buffer, 0, bufferSize);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                }
//
//                // send multipart form data necesssary after file data...
//                dos.writeBytes(lineEnd);
//                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//                // close streams
//                Log.e("Debug", "File is written");
//                fileInputStream.close();
//                dos.flush();
//                dos.close();
//
//            } catch (Exception ex) {
//                System.out.println("Exception::"+ex);
//                Log.e("Debug", "error: " + ex.getMessage(), ex);
//            }
//
//            //------------------ read the SERVER RESPONSE
//            try {
//
//                inStream = new DataInputStream(conn.getInputStream());
//                String str;
//
//                while ((str = inStream.readLine()) != null) {
//
//                    Log.e("Debug", "Server Response " + str);
//
//                }
//
//                inStream.close();
//
//            } catch (IOException ioex) {
//                System.out.println("Exception::"+ioex);
//                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
//            }
//
//
//        }
//    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            Log.e("uploadFile", "Source File not exist :"
                    +sourceFile );

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    System.out.println("200 Server Response");

                    File file = new File(sourceFileUri);
                    boolean deleted = file.delete();
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (Exception ex) {
                ex.printStackTrace();

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            }
            return serverResponseCode;

        } // End else block
    }


}
