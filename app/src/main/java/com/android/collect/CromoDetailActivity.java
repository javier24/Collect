package com.android.collect;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.android.collect.Communicator;
import com.android.collect.CromosDetailFragment;
import com.android.collect.Message;
import com.android.collect.R;


public class CromoDetailActivity extends ActionBarActivity implements Communicator {
    FragmentManager manager;
    Button camera_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cromo_detail_activity);
        manager=getFragmentManager();
        Message.message(this, getIntent().getStringExtra(CromosDetailFragment.ShowCromoID));

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(CromosDetailFragment.ShowCromoID,getIntent().getStringExtra(CromosDetailFragment.ShowCromoID));
            CromosDetailFragment fragment5 = new CromosDetailFragment();
            fragment5.setArguments(arguments);
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.ContainerCromo, fragment5,"A");
            transaction.commit();
        }

    }
    public void camera(View view) {
        camera_button = (Button)findViewById(R.id.camera_button);
        Intent intent = new Intent(this, MainCamera.class);
        startActivity(intent);
    }

    @Override
    public void respond(String ImageID) {   }

    @Override
    public String[] send_string() {
        return null;
    }
}
