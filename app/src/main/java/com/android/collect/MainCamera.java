package com.android.collect;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.collect.HttpHandler;
import com.android.collect.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class MainCamera extends Activity implements View.OnClickListener {

    /** Called when the activity is first created. */

    private final int CAMERA_RESULT = 1;
    private final String Tag = getClass().getName();
    Button button1;
    ImageView imageView1;
    HttpHandler handler;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_camera);
        handler= new HttpHandler();
        button1 = (Button)findViewById(R.id.button1);
        imageView1 = (ImageView)findViewById(R.id.imageView1);
        button1.setOnClickListener(this);
    }
    public void onClick(View v) {

        PackageManager pm = getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);
            startActivityForResult(i, CAMERA_RESULT);

        } else { Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show(); }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(Tag, "Receive the camera result");

        if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {

            File out = new File(getFilesDir(), "newImage.jpg");
            if(!out.exists()) {
                Toast.makeText(getBaseContext(),"Error while capturing image", Toast.LENGTH_LONG).show();
                return;
            }
            //take the bitmap to show in the activy in the meantime we process the image in the server
            Bitmap mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
            //encode the bitmap toBase64 to send the image with its AlbumName and CromoName to the server in a JSONObject.
            String ImageEncoded=encodeToBase64(mBitmap);
            imageView1.setImageBitmap(mBitmap);

            new UploadServer().execute(ImageEncoded);
        }
    }
    public static String encodeToBase64(Bitmap image){
        System.gc();
        if(image==null){
            return null;
        }else{
            Bitmap imagex=image;
            ByteArrayOutputStream baos =new ByteArrayOutputStream();
            imagex.compress(Bitmap.CompressFormat.JPEG,50,baos);
            byte[] b=baos.toByteArray();
            String ImageEncoded= Base64.encodeToString(b, Base64.DEFAULT);
            return ImageEncoded;
        }
    }

    /*  AsyncTask is a generic class, it uses 3 types: AsyncTask<Params, Progress, Result>.
    *   Params – the input. what you pass to the AsyncTask
    *   Progress – if you have any updates, passed to onProgressUpdate()
    *   Result – the output. what returns doInBackground()
    */

    public class UploadServer extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPostExecute(String result){
          //  t.setText(result);
         //   uploaDserver.setClickable(true);
        }

        @Override
        protected String doInBackground(String... imageData) {
            String text = null;
            handler.upload(imageData[0],getApplicationContext());
            return text;
        }

        @Override
        protected  void onPreExecute(){ }

        protected void onProgressUpdate(Integer... values){ }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageView1 = null;
    }
}
