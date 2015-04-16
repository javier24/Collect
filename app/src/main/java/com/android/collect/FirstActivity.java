package com.android.collect;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;


public class FirstActivity extends ActionBarActivity {

    DataBaseAdapter helper;
    HttpHandler handler;
    Button button;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstactivity);
        //initizlize the DataBaseAdapter object, if not  the helper.get_data will return null pointer exception
        helper = new DataBaseAdapter(this);
        handler=new HttpHandler();
        button=(Button)findViewById(R.id.button_borrar);
        text=(TextView)findViewById(R.id.textView2);
        new loginAndSign().execute("cool");


    }
// necesito opcion de borrado multiple mientras voy seleccionando en la lita de albums.
    public void Borrar_albums(View view){
        helper.delete_Row("Building");
    }

    public class loginAndSign extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPostExecute(String result){

            text.setText( "Download has finished successfully" + result);
            //Show something to the user telling him the download has finished ok. =)
        }

        @Override

        protected String doInBackground(String... imageData) {

                return handler.logingSing(getApplicationContext());

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
        getMenuInflater().inflate(R.menu.menu_main_activity32, menu);
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
        if (id== R.id.action_download){
            Intent intent = new Intent(getBaseContext(), Downloads.class);
            startActivity(intent);
        }
        if (id== R.id.action_albums){
            Intent intent = new Intent(getBaseContext(), DownloadedAlbums.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
