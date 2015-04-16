package com.android.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;


public class DownloadedAlbums extends ActionBarActivity {

    private TextView ListViewText;
    private ListView ListViewEx;
    private ListElements[] Options;
    private ListAdapter_downloads Adapter;
    //DataBase Object
    DataBaseAdapter helper;

    String[]columns={DataBaseContract.AlbumsD._ID, DataBaseContract.AlbumsD.ALBUM_NAME, DataBaseContract.AlbumsD.ALBUM_SHOW, DataBaseContract.AlbumsD.NUMBER_CROMOS};
    String[][]information;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_albums);
        //initizlize the DataBaseAdapter object, if not  the helper.get_data will return null pointer exception
        helper = new DataBaseAdapter(this);
      /* We have here albums loaded from the database the albums comes from files not drawables, drawables are present
       * in the activity downloads, because we can modify them just in compilation time, so we need them because they are
       * not gonna change.
       */
        ListViewText = (TextView)findViewById(R.id.AdvListViewText);
        ListViewEx = (ListView)findViewById(R.id.AdvListViewEx);

        information=helper.get_data(DataBaseContract.AlbumsD.TABLE_NAME,columns);
     //We have called the method get_data that returns an array with the information of the colums we asked. =)

        Options = new ListElements[information.length];

        for (int i = 0; i < Options.length; i++){
            Options[i] = new ListElements(information[i][1], information[i][3], information[i][2]);
        }

        Adapter = new ListAdapter_downloads(this,Options);
        ListViewText.setText("Nothing selected yet");
        ListViewEx.setAdapter(Adapter);
        ListViewEx.setFastScrollEnabled(true);

        ListViewEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        //Neccessary accition after click button.
                ListViewText.setText("Selected " + Options[position].getTitle());

                Intent intent = new Intent(getBaseContext(), CromosColecction.class);
                intent.putExtra("ALBUM_ID", Options[position].getTitle());
                intent.putExtra("NUMERO_CROMOS", Options[position].getDateCreated());

                startActivity(intent);
            }
        });
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id== R.id.action_download){
            Intent intent = new Intent(getBaseContext(), Downloads.class);
            startActivity(intent);
        }
        if (id== R.id.action_albums){
            Intent intent = new Intent(getBaseContext(), FirstActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}