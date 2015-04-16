package com.android.collect;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Downloads extends ActionBarActivity {

    private TextView ListViewText;
    private ListView ListViewEx;
    private ListElements[] Options;
    private ListAdapter Adapter;
    //DataBase Object
    DataBaseAdapter helper;
    String[]columns={DataBaseContract.Albums._ID, DataBaseContract.Albums.ALBUM_NAME, DataBaseContract.Albums.ALBUM_IMAGE, DataBaseContract.Albums.NUMBER_CROMOS};
    String[][]information;
  //variables to connect to the server
    String URL="http://192.168.15.31/collector/access_app.php";
    HttpHandler handler = new HttpHandler();
    String albumID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);
        //initizlize the DataBaseAdapter object, if not  the helper.get_data will return null pointer exception
        helper = new DataBaseAdapter(this);
        /*  Drawables in android are dinamics, so we need to store the name of the drawable in our DataBaae and after to retrive
         *  the id of the Drawable with the function
         *  Resources resources = getResources();
         *  String name = resources.getResourceEntryName(R.drawable.cerveza); Retrieve image name from the Drawable
         *  int resId = resources.getIdentifier(name, "drawable", getPackageName()); Retrive Id from name, tipe and package name
         */
        ListViewText = (TextView)findViewById(R.id.AdvListViewText);
        ListViewEx = (ListView)findViewById(R.id.AdvListViewEx);

        /* Right now the number of album are fixed but the idea is to download the number of albums that the user wants
         * so we would have to go through the Data Base looking for what albums does the user have?
         */
        information=helper.get_data(DataBaseContract.Albums.TABLE_NAME,columns);

        //We have called the method get_data that returns an array with the information of the colums we asked. =)

        Options = new ListElements[information.length];

        for (int i = 0; i < Options.length; i++){
            Options[i] = new ListElements(information[i][1], information[i][3], information[i][2]);
        }

        Adapter = new ListAdapter(this,Options);
        ListViewText.setText("Nothing selected yet");
        ListViewEx.setAdapter(Adapter);
        ListViewEx.setFastScrollEnabled(true);
        ListViewEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        /* Actions you wanna perform when you click on any items of the list
         * Take the name of the album  and send to the Asynctask to send this to the server and download this album
         */
        //albumID is the name of the album we define this in DataBaseAdapter.
               albumID=Options[position].getTitle();
               Toast.makeText(getBaseContext(),albumID,Toast.LENGTH_LONG).show();
               new DownloadFromServer().execute(albumID);
            }
        });
    }

    public class DownloadFromServer extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPostExecute(String result){
            ListViewText.setText(result);
            Toast.makeText(getBaseContext(), "Download has finished successfully" + result, Toast.LENGTH_LONG).show();
            //Show something to the user telling him the download has finished ok. =)
        }

        @Override
        protected String doInBackground(String... imageData) {
           return handler.DownloadFromServer(URL,imageData[0],getApplicationContext());
        }

        @Override
        protected  void onPreExecute(){ }
        protected void onProgressUpdate(Integer... values){
            // I need to do something to show the progress of the downloads files like a clock or a bar
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_downloads, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
