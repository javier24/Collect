package com.android.collect;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class HttpHandler {
    String Name;
    String path_file;
    DataBaseAdapter helper;
    String n_cromos;
    String picture;
    SharedPreferences sharedPreferences2;
    String tempValue;

    public void upload(String ImageEncoded,Context context) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br=null;
        HttpURLConnection httpCon = null;
        //Initialize DabaseAdapterOBJECT
        //create the JSON object, with the email we take from share preference and the albumID.
        JSONObject loginmsg = new JSONObject();
        loginmsg=createJSONObjectToUploadImage(ImageEncoded,context);

        try {
            URL myurl = new URL("http://192.168.15.31/collector/upload_image.php");
            httpCon = (HttpURLConnection) myurl.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setUseCaches(false);
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestMethod("POST");
            httpCon.connect(); // Note the connect() here

            OutputStream os = httpCon.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(loginmsg.toString());
            osw.flush();
            osw.close();

            br = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
        }
        catch (IOException e) {

        }


    }

    public String DownloadFromServer(String posturl, String albumID, Context context) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br=null;
        HttpURLConnection httpCon = null;
        //Initialize DabaseAdapterOBJECT
        helper = new DataBaseAdapter(context);
        //create the JSON object, with the email we take from share preference and the albumID.
        JSONObject msg = new JSONObject();
        msg=createJSONObject(albumID,context);

        try {
            URL myurl = new URL("http://192.168.15.31/collector/download_albums.php");
            httpCon = (HttpURLConnection) myurl.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setUseCaches(false);
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestMethod("POST");
            httpCon.connect(); // Note the connect() here

            OutputStream os = httpCon.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(msg.toString());
            osw.flush();
            osw.close();

            br = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray album = jsonObject.getJSONArray(albumID);

            // Go through array with cromo objects.
            for (int i = 0; i < album.length(); i++) {
                JSONObject cromo = album.getJSONObject(i);
                // Create the cromo object
                // first element of any album is gonna be the profile picture and information of the album
                if (i == 0) {
                    Name = cromo.getString("NAME");
                    picture = cromo.getString("PICTURE_GREY");
                    n_cromos = cromo.getString("N_CROMOS");
                    path_file = SaveAlbum(context, picture, Name);
                    helper.insert_data_albums(Name, path_file,n_cromos);

                } else {
                    String ID = cromo.getString("ID");
                    Name = cromo.getString("NAME");
                    String picture = cromo.getString("PICTURE_GREY");
                    path_file = SaveAlbum(context, picture, Name);
                    helper.insert_data(albumID, Name, path_file);
                }
            }
            return path_file;

        }
         catch (IOException e) {
             return "false";
        } catch (JSONException e) {
             e.printStackTrace();
             return "capullo";
        }
    }

    public String logingSing( Context context) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br=null;
        HttpURLConnection httpCon = null;
        //Initialize DabaseAdapterOBJECT
        //create the JSON object, with the email we take from share preference and the albumID.
        JSONObject loginmsg = new JSONObject();
        loginmsg=createJSONObject(null,context);

        try {
            URL myurl = new URL("http://192.168.15.31/collector/access_app.php");
            httpCon = (HttpURLConnection) myurl.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setUseCaches(false);
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestMethod("POST");
            httpCon.connect(); // Note the connect() here

            OutputStream os = httpCon.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(loginmsg.toString());
            osw.flush();
            osw.close();

            br = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            return sb.toString();
        }
        catch (IOException e) {
            return "false";
        }

    }
    
    public String SaveAlbum(Context context, String picture, String name) {
        FileOutputStream fos;
        byte[] byteData = Base64.decode(picture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
        try {
            fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }


    public JSONObject createJSONObject(String AlbumName,Context context){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        tempValue= sharedPreferences.getString("email", null);

        JSONObject user = new JSONObject();
        try {
            user.put("AlbumID", AlbumName);
            user.put("email", tempValue);
            //continue adding what you need

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
    public JSONObject createJSONObjectToUploadImage(String ImageEncoded,Context context){
        sharedPreferences2=PreferenceManager.getDefaultSharedPreferences(context);
        String AlbumName= sharedPreferences2.getString("AlbumID", null);
        String CromoName= sharedPreferences2.getString("CromoID", null);

        JSONObject user = new JSONObject();
        try {
            user.put("AlbumID", AlbumName);
            user.put("CromoID", CromoName);
            user.put("ImageEncoded",ImageEncoded);
            //continue adding what you need

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}


/*HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;

        String pathToOurFile = selectedPath;
        String urlServer = "http://192.168.15.31/collector/upload_image.php";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        try {
            FileInputStream fileInputStream = new FileInputStream(new File(
                    pathToOurFile));

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                    + lineEnd);

            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception ex) {
        }*/