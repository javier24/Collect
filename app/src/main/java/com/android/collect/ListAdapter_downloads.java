package com.android.collect;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by root on 01/04/15.
 */
public class ListAdapter_downloads extends ArrayAdapter{
    static class ViewHolder{
        TextView Title;
        TextView DateCreated;
        ImageView ImageId;
    }

    Activity Context;
    ListElements[] list;

    ListAdapter_downloads (Activity Context,ListElements[] list){
        super(Context,R.layout.list_layout,list);
        this.Context = Context;
        this.list = list;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View item = convertView;
        ViewHolder holder;

        if (item == null) {
            // When the item invoked is empty/not used:
            // Invoke layout inflater, in order to obtain xlm layout
            LayoutInflater inflater = Context.getLayoutInflater();
            item = inflater.inflate(R.layout.list_layout, null);

            //Start new variables and associate id
            holder = new ViewHolder();
            holder.Title = (TextView)item.findViewById(R.id.ListTitle);
            holder.DateCreated = (TextView)item.findViewById(R.id.ListDateCreated);
            holder.ImageId = (ImageView)item.findViewById(R.id.ListImage);
            item.setTag(holder);
        }
        else{
            // When the item is used, simply obtain it
            holder = (ViewHolder)item.getTag();
        }

        // Set parameters of elements
        holder.Title.setText(list[position].getTitle());
        holder.DateCreated.setText(list[position].getDateCreated());
        // Resources resources = getContext().getResources();
        FileInputStream fis;
        Bitmap bitmap=null;
        try {
            fis = getContext().openFileInput(list[position].getImageId());
            bitmap = BitmapFactory.decodeStream(fis);
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.ImageId.setImageBitmap(bitmap);
        return(item);
    }

}

