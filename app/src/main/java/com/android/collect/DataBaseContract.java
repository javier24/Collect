package com.android.collect;

import android.provider.BaseColumns;


public class DataBaseContract {
    /**
     * Defines table and column names for the weather database.
     */

        /* Inner class that defines the table contents of the location table */
    public static final class Albums implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "albums";          // --TABLE NAME.
        // name of the album                                        ___________________________________________________________
        public static final String ALBUM_NAME = "album_name";      //  _ID......ALBUM_NAME.......ALBUM_IMAGE.....NUMERO_CROMOS
        // path to the image                                       //   1.......BEERS............PICTURE.............5......
        public static final String ALBUM_IMAGE = "album_image";
        public static final String NUMBER_CROMOS= "numero_cromos"; //___________________________________________________________

    }

    /* Inner class that defines the table contents of the albums we really have */
    public static final class AlbumsD implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "AlbumsD";
        public static final String ALBUM_NAME =  "NAME";
         //show how many cromos do you have like 20/25
        public static final String NUMBER_CROMOS= "N_CROMOS";
        public static final String ALBUM_SHOW="IMG_SHOW";
        //    public static final String OK = "ok";

    }

    /* Inner class that defines the table contents of the beers table */
    public static final class Beers implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "Beer";            // --TABLE NAME.
        // name of the album                                            _______________________
        public static final String BEER_NAME =  "NAME";           //  _ID...NAME...IMG_SHOW
        // path to the image                                     //   1....BEERS....picture....
        public static final String BEER_SHOW="IMG_SHOW";

    }

    /* Inner class that defines the table contents of places table */
    public static final class Important_buildings implements BaseColumns {

        // Table name
        public static final String TABLE_NAME = "Building";                     // --TABLE NAME.
        // name of the album                                                    ___________________________________________________________________
        public static final String BUILDING_NAME =  "NAME";                     //  _ID...BUILDING_NAME...BUILDING_IMAGE_GREY..BUILDING_IMAGE_OK..LATITUD....LONGITUD...OK.....ALBUM_ID
        // path to the image                                                    //   1....BUILDINGS.........PICTURE_grey.......picture............ 556........673.....FALSE......_id
                                                                                 //_________________________________________________________________________
        public static final String BUILDING_IMAGE_OK = "IMG_SHOW";
     //   public static final String COOR_LAT = "latitud";
     //   public static final String COOR_LONG = "longitud";
    }
}
