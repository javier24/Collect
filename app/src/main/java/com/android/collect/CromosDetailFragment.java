package com.android.collect;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class CromosDetailFragment extends Fragment {

    View button;
    String image;
    Bitmap bitmap=null;
    public static final String ShowCromoID= "ID";

    public CromosDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null)
        {
            if (getArguments().containsKey(ShowCromoID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                image = getArguments().getString(ShowCromoID);
                //image = name of the cromo
                FileInputStream fis;
                try {
                    fis = getActivity().openFileInput(image);
                    bitmap = BitmapFactory.decodeStream(fis);
                    fis.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                  catch (IOException e) {
                      e.printStackTrace();
                }
            }
        } else {
            Message.message(getActivity(),"no data");
            // no arguments supplied...
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cromo_detail_fragment, container, false);
        // Show the dummy content as text in a TextView.
        if (image != null) {
            ((ImageView) rootView.findViewById(R.id.imageView)).setImageBitmap(bitmap);
             button=rootView.findViewById(R.id.camera_button);
        }
        return rootView;
    }
}


