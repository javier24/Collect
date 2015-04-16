package com.android.collect;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class CromosListFragment extends Fragment {
    private TextView ListViewText;
    private ListView ListViewEx;
    private ListElements[] Options;
    private ListAdapter_downloads Adapter;
    Communicator comm;
    String[][] information;
    DataBaseAdapter helper;
    String[] albumName;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
         * I need to implement onAttacch to initialize the DataBaseAdapter object in this fragment is the only way I have to qet
         * the context of the activity
         */
        helper = new DataBaseAdapter(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_cromoslistfragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //just for to make sure the UI of the DownloadedAlbums is already created.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comm = (Communicator) getActivity();
        initAlbums();
    }

    void initAlbums() {
         albumName = comm.send_string();
        /* The name of the colums always are gonna be  NAME ane IMG_SHOW, for all pictures
         * in this way the system get simple.
         */
        String[] columns= {DataBaseContract.Beers.BEER_NAME, DataBaseContract.Beers.BEER_SHOW};

        ListViewText = (TextView) getView().findViewById(R.id.AdvListViewText);
        ListViewEx = (ListView) getView().findViewById(R.id.AdvListViewEx);

        /* recive the name of the Table in the database (Album, beers,buildings),and the leght of the album,
         * generate the array with that amount of cromos, and then fill the array with the information from data base
         */

        information=helper.get_data(albumName[0],columns);
        if (!albumName[0].equals(null)) {
            Options = new ListElements[information.length];

                for (int i = 0; i < Options.length; i++)
                    {
                        Options[i] = new ListElements(information[i][0], information[i][0], information[i][1]);
                    }
            }

            Adapter = new ListAdapter_downloads(getActivity(), Options);
            ListViewText.setText(albumName[0]);
            ListViewEx.setAdapter(Adapter);
            ListViewEx.setFastScrollEnabled(true);

            ListViewEx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            //run neccessary action after click button
                    ListViewText.setText("Selected " + Options[position].getTitle());
            //save Album and cromo name in a SharePeference because I need touse this information to upload the pictur.
                    saveAlbumAndCromoNames(Options[position].getImageId());
                    comm.respond(Options[position].getImageId());
                }
            });
        }
    public void saveAlbumAndCromoNames(String cromoName){
        SharedPreferences prefs2 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor2 = prefs2.edit();
        editor2.putString("AlbumID",albumName[0]);
        editor2.putString("CromoID", cromoName);
        editor2.commit();
    }
}
