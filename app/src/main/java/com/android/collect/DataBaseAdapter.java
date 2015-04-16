package com.android.collect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseAdapter {
    OpenDBHelper helper;
    int j=0;
    public DataBaseAdapter(Context context){
        //this constructor is neccesary because we need the context to initialize the object of OpenDBHelper
        helper=new OpenDBHelper(context);

    }
    public long insert_data(String albumName,String cromoName, String ImageAlbum){
        //we take the database object helper, and open in read and write mode
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME",cromoName);
        contentValues.put("IMG_SHOW",ImageAlbum);
        long id=db.insert(albumName,null,contentValues);
        //the previous sentence return -1 if something went wrong, and the id of the last row inserted.
        return id;
    }

    //this class insert data in the albumsD database
    public long insert_data_albums(String cromoName, String ImageAlbum, String n_cromos){
        //we take the database object helper, and open in read and write mode
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("NAME",cromoName);
        contentValues.put("IMG_SHOW",ImageAlbum);
        contentValues.put("N_CROMOS",n_cromos);
        long id=db.insert(DataBaseContract.AlbumsD.TABLE_NAME,null,contentValues);
        //the previous sentence return -1 if something went wrong, and the id of the last row inserted.
        return id;
    }

    public String[][] get_data(String Table_Name, String[] columns) {
        //select album_name and Image_name from where name = 'JUAN'
        SQLiteDatabase db = helper.getWritableDatabase();
        //The query it is gonna return a cursor object
        Cursor cursor1 = db.rawQuery("select * from "+ Table_Name,null);
        int count =cursor1.getCount();

        Cursor cursor2 = db.query(Table_Name, columns, null
                , null, null, null, null);
       String[][]information=new String[count][columns.length];
        while (cursor2.moveToNext()) {
            for (int i = 0; i < columns.length; i++) {
                information[j][i] = cursor2.getString(cursor2.getColumnIndex(columns[i]));
            }
            j++;
        }
        return information;
    }


    public int Update_Row(String old_name, String newImageName){

        // UPDATE albums  SET album_name='Juan' where album_name=? test
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //key name of the column we wanna update, and the value the value
        contentValues.put(DataBaseContract.Albums.ALBUM_NAME,newImageName);
        String[] whereArgs={old_name};

        // this function will return an integer of how many rows has been updated
        int count=db.update(DataBaseContract.Albums.TABLE_NAME,contentValues, DataBaseContract.Albums.ALBUM_NAME+" =? ",whereArgs);
        return count;
    }

    public int delete_Row(String albumID){
        //DELETE * FROM albums Where Name =
        SQLiteDatabase db=helper.getWritableDatabase();
        String[] whereArgs={albumID};
        int count=db.delete(DataBaseContract.AlbumsD.TABLE_NAME, DataBaseContract.AlbumsD.ALBUM_NAME + " =? ",whereArgs);
           db.delete(albumID,null,null);

        return count;
    }

    static class OpenDBHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        private static final int DATABASE_VERSION =1;
        private static final String DATABASE_NAME = "album.db";
        private SQLiteDatabase sqLiteDatabase;

        public OpenDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.sqLiteDatabase=this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            /* Create all the tables here because we dont have any problem of space and then we will fill them
             *   with the downloaded information.
             */
            final String SQL_CREATE_ALBUMS_TABLE = "CREATE TABLE " + DataBaseContract.Albums.TABLE_NAME + " (" +
                    DataBaseContract.Albums._ID + " INTEGER PRIMARY KEY," +
                    DataBaseContract.Albums.ALBUM_NAME + " TEXT UNIQUE NOT NULL, " +
                    DataBaseContract.Albums.ALBUM_IMAGE + " TEXT NOT NULL, " +
                    DataBaseContract.Albums.NUMBER_CROMOS + " INTEGER NOT NULL, " +
                    "UNIQUE (" + DataBaseContract.Albums.ALBUM_NAME +") ON CONFLICT IGNORE"+
                    " );";
            final String SQL_CREATE_ALBUMSD_TABLE = "CREATE TABLE " + DataBaseContract.AlbumsD.TABLE_NAME + " (" +
                    DataBaseContract.AlbumsD._ID + " INTEGER PRIMARY KEY," +
                    DataBaseContract.AlbumsD.ALBUM_NAME + " TEXT UNIQUE NOT NULL, " +
                    DataBaseContract.AlbumsD.ALBUM_SHOW + " TEXT NOT NULL, " +
                    DataBaseContract.AlbumsD.NUMBER_CROMOS + " TEXT NOT NULL, " +
                    "UNIQUE (" + DataBaseContract.Beers.BEER_NAME +") ON CONFLICT IGNORE"+
                    " );";

            final String SQL_CREATE_BEERS_TABLE = "CREATE TABLE " + DataBaseContract.Beers.TABLE_NAME + " (" +
                    DataBaseContract.Beers._ID + " INTEGER PRIMARY KEY," +
                    DataBaseContract.Beers.BEER_NAME + " TEXT UNIQUE NOT NULL, " +
                    DataBaseContract.Beers.BEER_SHOW + " TEXT NOT NULL, " +
                    "UNIQUE (" + DataBaseContract.Beers.BEER_NAME +") ON CONFLICT IGNORE"+
                    " );";


            final String SQL_CREATE_IMPORTANT_BUILDINGS_TABLE = "CREATE TABLE " + DataBaseContract.Important_buildings.TABLE_NAME + " (" +
                    DataBaseContract.Important_buildings._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                //    DataBaseContract.Important_buildings.ALBUM_ID + " INTEGER NOT NULL, " +
                    DataBaseContract.Important_buildings.BUILDING_NAME + " TEXT NOT NULL, " +
                    DataBaseContract.Important_buildings.BUILDING_IMAGE_OK + " TEXT NOT NULL, " +
                    "UNIQUE (" + DataBaseContract.Important_buildings.BUILDING_NAME +") ON CONFLICT IGNORE"+
                    " );";

                sqLiteDatabase.execSQL(SQL_CREATE_BEERS_TABLE);
                sqLiteDatabase.execSQL(SQL_CREATE_ALBUMS_TABLE);
                sqLiteDatabase.execSQL(SQL_CREATE_ALBUMSD_TABLE);
                sqLiteDatabase.execSQL(SQL_CREATE_IMPORTANT_BUILDINGS_TABLE);

                //The only table we are gonna initialize is the Albums table because is what we want in the first stage.

                Initialize_Albums(sqLiteDatabase);
        }

        public void Initialize_Albums(SQLiteDatabase sqLiteDatabase){
            sqLiteDatabase.execSQL("INSERT INTO "+ DataBaseContract.Albums.TABLE_NAME+" ("+ DataBaseContract.Albums.ALBUM_NAME+", "+ DataBaseContract.Albums.ALBUM_IMAGE+", "+ DataBaseContract.Albums.NUMBER_CROMOS+")values ('Beer','cerveza','7')");
            sqLiteDatabase.execSQL("INSERT INTO "+ DataBaseContract.Albums.TABLE_NAME+" ("+ DataBaseContract.Albums.ALBUM_NAME+", "+ DataBaseContract.Albums.ALBUM_IMAGE+", "+ DataBaseContract.Albums.NUMBER_CROMOS+")values ('Building','buildings','9')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.Albums.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.Beers.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBaseContract.Important_buildings.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
