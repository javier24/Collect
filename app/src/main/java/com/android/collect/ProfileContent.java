package com.android.collect;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileContent {

private JSONObject object;
private String name;
private String email;
private String ID;



    public ProfileContent(JSONObject object,String name, String email, String ID) throws JSONException {
        this.name = object.getString(name);
        this.email = object.getString(email);
        this.ID=object.getString(ID);
    }

    public String getProfileName(){ return this.name;}

    public String getEmail(){
        return this.email;
    }

    public String getID(){ return this.ID;}
}