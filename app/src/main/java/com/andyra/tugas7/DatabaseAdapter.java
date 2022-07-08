package com.andyra.tugas7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DatabaseAdapter {
    private static final String TAG = "DBAdapter";
    private static final String DB_NAME = "gudang_void.db";
    private static final String DB_TABLE_USER = "user";
    private static final String DB_TABLE_BARANG = "barang";
    private static final int DB_VERSION = 1;
    private Context context = null;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DatabaseAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DB_NAME,
                    null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("CREATE TABLE " +DB_TABLE_USER+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username text NOT NULL, " +
                    "password text NOT NULL);");

            db.execSQL("CREATE TABLE "+DB_TABLE_BARANG+" (id_barang INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nama_barang VARCHAR(100) NOT NULL, " +
                    "jenis_barang VARCHAR(25) NOT NULL, " +
                    "stok_barang INT NOT NULL );");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " +DB_TABLE_USER);
            db.execSQL("DROP TABLE IF EXISTS " +DB_TABLE_BARANG);
            onCreate(db);
        }
    }


    public void open() throws SQLException {
        db = DBHelper.getWritableDatabase();
    }
    public void close() {
        DBHelper.close();
    }

    public boolean Register(String username, String password) throws SQLException {
        Cursor mCursor = db.rawQuery("INSERT INTO " + DB_TABLE_USER +
                        " (username, password) values (?,?)",
                new String[]{username,password});
        if (mCursor != null) {
            if(mCursor.getCount() > 0)
            {
                return true;
            }
        }
        return false;
    }
    public boolean Login(String username, String password) throws SQLException {
        Cursor mCursor = db.rawQuery("SELECT * FROM "
                        + DB_TABLE_USER + " WHERE username=? AND password=?"
                , new String[]{username,password});
        if (mCursor != null) {
            if(mCursor.getCount() > 0)
            {
                return true;
            }
        }
        return false;
    }

    public boolean tambah_barang(String nama_barang, String jenis_barang, String stok_barang) throws SQLException{
        Cursor tmbh_brg = db.rawQuery("INSERT INTO " +DB_TABLE_BARANG +
                        "(nama_barang, jenis_barang, stok_barang) VALUES (?,?,?)",
                new String[]{nama_barang, jenis_barang, stok_barang});

        if (tmbh_brg != null) {
            if(tmbh_brg.getCount() > 0)
            {
                return true;
            }
        }
        return false;
    }

    public Cursor tampil_barang(){
        Cursor tmpl_brg = db.rawQuery("SELECT * FROM " +DB_TABLE_BARANG ,null);
        return tmpl_brg;
    }

    public void update_barang(String idbarang, String jenis_barang, String stok){
        SQLiteDatabase database = this.DBHelper.getWritableDatabase();
        ContentValues mcontentValues = new ContentValues();
        mcontentValues.put("jenis_barang", jenis_barang);
        mcontentValues.put("stok_barang", stok);
        database.update(DB_TABLE_BARANG, mcontentValues, "id_barang = ?", new String[]{idbarang});
    }

    public void delete_barang(String idbarang){
        SQLiteDatabase database = this.DBHelper.getWritableDatabase();
        database.delete(DB_TABLE_BARANG, "id_barang = ?", new String[]{idbarang});
        database.close();
    }
}
