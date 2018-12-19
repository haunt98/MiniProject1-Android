package com.example.anon.miniproject1_cntn16;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myplaces.db";
    private static String DATABASE_PATH = "";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "MyPlaces";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_FAV = "fav";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private Context context;

    // singleton
    private static MyDatabaseHelper databaseInstance;

    public static synchronized MyDatabaseHelper getDatabaseInstance(Context context) {
        // chua ton tai moi tao moi
        if (databaseInstance == null) {
            databaseInstance = new MyDatabaseHelper(context);
        }
        return databaseInstance;
    }

    private MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // save for access assets
        this.context = context;

        // get database path
        DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";

        copyDatabaseFromAssets();
    }

    private void copyDatabaseFromAssets() {
        if (!existDatabase()) {
            // close database for copy
            getReadableDatabase();
            close();
            // actual copy
            try {
                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                OutputStream outputStream = new FileOutputStream(
                        DATABASE_PATH + DATABASE_NAME);
                byte[] buffer = new byte[1024];
                int len_actual_read;
                while ((len_actual_read = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len_actual_read);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                Log.e("DATABASE", e.toString());
            }
        }
    }

    private boolean existDatabase() {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        return file.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // khong lam gi ca vi copy database co san
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // khong lam gi ca vi thoi gian co han, chua co du dinh upgrade
    }

    public MyPlace getMyPlaceByID(Integer id) {
        MyPlace myPlace = null;

        String[] projection = {
                KEY_NAME,
                KEY_DESCRIPTION,
                KEY_IMAGE,
                KEY_ADDRESS,
                KEY_WEBSITE,
                KEY_EMAIL,
                KEY_PHONE,
                KEY_FAV,
                KEY_LAT,
                KEY_LNG
        };

        String selection = KEY_ID + " = ?";
        String[] selectionArgs = {
                id.toString()
        };

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        try (Cursor cursor = sqLiteDatabase.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        )) {
            if (cursor.moveToNext()) {
                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE));
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                myPlace = new MyPlace(
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        bitmap,
                        cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(KEY_WEBSITE)),
                        cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                        cursor.getInt(cursor.getColumnIndex(KEY_FAV)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LAT)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LNG))
                );
            }
        }

        return myPlace;
    }


    ArrayList<ShortMyPlace> getListShortMyPlaceByName(String name) {
        // projection - columns which need to use
        String[] projection = {
                KEY_NAME,
                KEY_ADDRESS,
                KEY_ID
        };

        // where clause
        String selection = KEY_NAME + " LIKE ? OR "
                + KEY_DESCRIPTION + " LIKE ? OR "
                + KEY_ADDRESS + " LIKE ?";
        String[] selectionArgs = {
                "%" + name + "%",
                "%" + name + "%",
                "%" + name + "%",
        };

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        // gioi han so ID tra ve
        final Integer MAX_ID = 5;

        // luu danh sach my place phu hop
        ArrayList<ShortMyPlace> listShortMyPlace = new ArrayList<>();

        try (Cursor cursor = sqLiteDatabase.query(
                true,
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null,
                MAX_ID.toString()
        )) {
            while (cursor.moveToNext()) {
                ShortMyPlace shortMyPlace = new ShortMyPlace(
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)),
                        cursor.getInt(cursor.getColumnIndex(KEY_ID))
                );
                listShortMyPlace.add(shortMyPlace);
            }
        }

        return listShortMyPlace;
    }

    public Integer getFavByID(Integer id) {
        String[] projection = {
                KEY_FAV
        };

        String selection = KEY_ID + " = ?";
        String[] selectionArgs = {
                id.toString()
        };

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        try (Cursor cursor = sqLiteDatabase.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        )) {
            if (cursor.moveToNext()) {
                return cursor.getInt(cursor.getColumnIndex(KEY_FAV));
            }
        }

        return null;
    }

    public Integer changFavByID(Integer id) {
        Integer fav_val = getFavByID(id);
        // khong ton tai id trong database
        if (fav_val == null) {
            return null;
        }

        // du lieu can sua
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FAV, 1 - fav_val);

        String selection = KEY_ID + " = ?";
        String[] selectionArgs = {
                id.toString()
        };

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update(
                TABLE_NAME,
                contentValues,
                selection,
                selectionArgs
        );

        return 1 - fav_val;
    }

    public ArrayList<ShortMyPlace> getListFavPlace() {
        String[] projection = {
                KEY_NAME,
                KEY_ADDRESS,
                KEY_ID
        };

        String selection = KEY_FAV + " = 1";

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        ArrayList<ShortMyPlace> listFavPlace = new ArrayList<>();

        try (Cursor cursor = sqLiteDatabase.query(
                TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        )) {
            while (cursor.moveToNext()){
                ShortMyPlace shortMyPlace = new ShortMyPlace(
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)),
                        cursor.getInt(cursor.getColumnIndex(KEY_ID))
                );
                listFavPlace.add(shortMyPlace);
            }
        }

        return listFavPlace;
    }
}
